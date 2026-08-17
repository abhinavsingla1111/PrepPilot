#!/usr/bin/env python3
"""PrepPilot's dependency-free worker for isolated Java, C++ and Python runs."""

from __future__ import annotations

import argparse
import base64
import binascii
import http.client
import json
import logging
import os
import pathlib
import re
import signal
import ssl
import subprocess
import tempfile
import time
import urllib.error
import urllib.parse
import urllib.request
import uuid
from dataclasses import dataclass
from typing import Any


LOG = logging.getLogger("preppilot.runner")
LANGUAGES = {"JAVA": "java", "CPP": "cpp", "PYTHON": "python"}
TERMINAL_CASE_STATUSES = {
    "EXECUTED",
    "COMPILE_ERROR",
    "RUNTIME_ERROR",
    "TIME_LIMIT",
    "INTERNAL_ERROR",
}
SAFE_IMAGE = re.compile(r"^[a-zA-Z0-9][a-zA-Z0-9._/:@-]{0,254}$")


class RunnerError(RuntimeError):
    pass


class LeaseLost(RunnerError):
    pass


class RunnerAuthenticationError(RunnerError):
    pass


RECOVERABLE_WORKER_ERRORS = (
    RunnerError,
    urllib.error.URLError,
    http.client.HTTPException,
    OSError,
    TimeoutError,
)


@dataclass(frozen=True)
class Settings:
    api_url: str
    worker_token: str
    podman_binary: str
    poll_seconds: float
    request_timeout_seconds: float
    container_timeout_seconds: float
    compile_timeout_seconds: int
    test_timeout_seconds: int
    max_output_bytes: int
    memory_limit: str
    cpu_limit: str
    pids_limit: int
    images: dict[str, str]

    @classmethod
    def from_environment(cls) -> "Settings":
        settings = cls(
            api_url=os.getenv("RUNNER_API_URL", "http://127.0.0.1:8080").rstrip("/"),
            worker_token=os.getenv("RUNNER_WORKER_TOKEN", ""),
            podman_binary=os.getenv("RUNNER_PODMAN_BINARY", "podman"),
            poll_seconds=_bounded_float("RUNNER_POLL_SECONDS", 2.0, 0.25, 60.0),
            request_timeout_seconds=_bounded_float("RUNNER_REQUEST_TIMEOUT_SECONDS", 10.0, 1.0, 60.0),
            container_timeout_seconds=_bounded_float("RUNNER_CONTAINER_TIMEOUT_SECONDS", 30.0, 10.0, 120.0),
            compile_timeout_seconds=_bounded_int("RUNNER_COMPILE_TIMEOUT_SECONDS", 10, 2, 30),
            test_timeout_seconds=_bounded_int("RUNNER_TEST_TIMEOUT_SECONDS", 2, 1, 5),
            max_output_bytes=_bounded_int("RUNNER_MAX_OUTPUT_BYTES", 32_768, 1_000, 64_000),
            memory_limit=os.getenv("RUNNER_MEMORY_LIMIT", "512m"),
            cpu_limit=os.getenv("RUNNER_CPU_LIMIT", "1.0"),
            pids_limit=_bounded_int("RUNNER_PIDS_LIMIT", 64, 16, 128),
            images={
                "JAVA": os.getenv("RUNNER_IMAGE_JAVA", "localhost/preppilot-runner-java:21"),
                "CPP": os.getenv("RUNNER_IMAGE_CPP", "localhost/preppilot-runner-cpp:14"),
                "PYTHON": os.getenv("RUNNER_IMAGE_PYTHON", "localhost/preppilot-runner-python:3.13"),
            },
        )
        settings.validate()
        return settings

    def validate(self) -> None:
        parsed = urllib.parse.urlparse(self.api_url)
        local_http = parsed.scheme == "http" and parsed.hostname in {"127.0.0.1", "localhost"}
        if parsed.scheme != "https" and not local_http:
            raise RunnerError("RUNNER_API_URL must use HTTPS; HTTP is allowed only for localhost development.")
        if not parsed.hostname:
            raise RunnerError("RUNNER_API_URL must be an absolute URL.")
        if len(self.worker_token) < 32:
            raise RunnerError("RUNNER_WORKER_TOKEN must contain at least 32 characters.")
        if not re.fullmatch(r"[a-zA-Z0-9._/-]{1,200}", self.podman_binary):
            raise RunnerError("RUNNER_PODMAN_BINARY contains unsupported characters.")
        if not re.fullmatch(r"[0-9]+(?:[kKmMgG])?", self.memory_limit):
            raise RunnerError("RUNNER_MEMORY_LIMIT is invalid.")
        if not re.fullmatch(r"(?:0\.[1-9]|[1-4](?:\.0)?)", self.cpu_limit):
            raise RunnerError("RUNNER_CPU_LIMIT must be between 0.1 and 4.0.")
        if any(not SAFE_IMAGE.fullmatch(image) for image in self.images.values()):
            raise RunnerError("A runner image reference contains unsupported characters.")


class ApiClient:
    def __init__(self, settings: Settings):
        self._settings = settings
        self._ssl_context = ssl.create_default_context()

    def claim(self) -> dict[str, Any] | None:
        status, body = self._request("POST", "/api/internal/runner/jobs/claim", None)
        if status == 204:
            return None
        if status != 200 or not isinstance(body, dict):
            raise RunnerError(f"Claim returned unexpected HTTP status {status}.")
        return body

    def complete(self, job_id: str, lease: str, results: list[dict[str, Any]]) -> None:
        self._lease_request("POST", f"/api/internal/runner/jobs/{job_id}/complete", lease, {"results": results})

    def fail(self, job_id: str, lease: str, message: str) -> None:
        self._lease_request("POST", f"/api/internal/runner/jobs/{job_id}/fail", lease, {"message": message[:500]})

    def _lease_request(self, method: str, path: str, lease: str, payload: dict[str, Any]) -> None:
        try:
            status, _ = self._request(method, path, payload, {"X-Runner-Lease": lease})
        except urllib.error.HTTPError as exception:
            if exception.code == 409:
                raise LeaseLost("The job lease is no longer active.") from exception
            raise
        if status not in {200, 204}:
            raise RunnerError(f"Runner callback returned unexpected HTTP status {status}.")

    def _request(
        self,
        method: str,
        path: str,
        payload: dict[str, Any] | None,
        extra_headers: dict[str, str] | None = None,
    ) -> tuple[int, Any]:
        data = None if payload is None else json.dumps(payload, separators=(",", ":")).encode("utf-8")
        headers = {
            "Authorization": f"Bearer {self._settings.worker_token}",
            "Accept": "application/json",
        }
        if data is not None:
            headers["Content-Type"] = "application/json"
        if extra_headers:
            headers.update(extra_headers)
        request = urllib.request.Request(
            self._settings.api_url + path,
            data=data if data is not None else b"",
            headers=headers,
            method=method,
        )
        try:
            with urllib.request.urlopen(
                request,
                timeout=self._settings.request_timeout_seconds,
                context=self._ssl_context,
            ) as response:
                raw = response.read(1_000_000)
                body = json.loads(raw) if raw else None
                return response.status, body
        except urllib.error.HTTPError as exception:
            if exception.code in {401, 403}:
                raise RunnerAuthenticationError(
                    "The API rejected RUNNER_WORKER_TOKEN. Synchronize .env and runner/.env, then restart the worker."
                ) from exception
            raise


class PodmanSandbox:
    def __init__(self, settings: Settings):
        self._settings = settings

    def verify(self) -> None:
        _run_checked([self._settings.podman_binary, "version", "--format", "{{.Client.Version}}"], timeout=10)
        for image in self._settings.images.values():
            _run_checked([self._settings.podman_binary, "image", "exists", image], timeout=10)

    def execute(self, job: dict[str, Any]) -> list[dict[str, Any]]:
        job_id, language, source, cases = _validate_job(job)
        image = self._settings.images[language]
        container_name = f"preppilot-job-{job_id}"
        with tempfile.TemporaryDirectory(prefix="preppilot-run-") as temporary:
            input_dir = pathlib.Path(temporary)
            tests_dir = input_dir / "tests"
            tests_dir.mkdir(mode=0o755)
            (input_dir / "source").write_text(source, encoding="utf-8")
            for case in cases:
                (tests_dir / f"{case['position']:03d}.in").write_text(case["input"], encoding="utf-8")
            os.chmod(input_dir / "source", 0o444)
            for test_file in tests_dir.iterdir():
                os.chmod(test_file, 0o444)
            os.chmod(input_dir, 0o755)

            command = self._container_command(container_name, image, input_dir, language)
            try:
                completed = subprocess.run(
                    command,
                    stdin=subprocess.DEVNULL,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.PIPE,
                    check=False,
                    timeout=self._settings.container_timeout_seconds,
                )
            except subprocess.TimeoutExpired as exception:
                self._remove_container(container_name)
                raise RunnerError("The sandbox exceeded its whole-job deadline.") from exception
            if completed.returncode != 0:
                diagnostic = _decode_process_output(completed.stderr)
                raise RunnerError(f"The sandbox exited unexpectedly: {diagnostic[:300]}")
            return _parse_protocol(_decode_process_output(completed.stdout), len(cases))

    def _container_command(
        self,
        name: str,
        image: str,
        input_dir: pathlib.Path,
        language: str,
    ) -> list[str]:
        return [
            self._settings.podman_binary,
            "run",
            "--rm",
            "--name",
            name,
            "--network=none",
            "--ipc=none",
            "--read-only",
            "--cap-drop=ALL",
            "--security-opt=no-new-privileges",
            f"--pids-limit={self._settings.pids_limit}",
            f"--memory={self._settings.memory_limit}",
            f"--memory-swap={self._settings.memory_limit}",
            f"--cpus={self._settings.cpu_limit}",
            "--ulimit=nofile=64:64",
            "--ulimit=fsize=131072:131072",
            "--tmpfs=/tmp:rw,nosuid,nodev,size=192m,mode=1777",
            "--env",
            f"PREPPILOT_LANGUAGE={LANGUAGES[language]}",
            "--env",
            f"COMPILE_TIMEOUT_SECONDS={self._settings.compile_timeout_seconds}",
            "--env",
            f"TEST_TIMEOUT_SECONDS={self._settings.test_timeout_seconds}",
            "--env",
            f"MAX_OUTPUT_BYTES={self._settings.max_output_bytes}",
            "--volume",
            f"{input_dir}:/input:ro,Z",
            "--pull=never",
            image,
        ]

    def _remove_container(self, name: str) -> None:
        subprocess.run(
            [self._settings.podman_binary, "rm", "--force", name],
            stdin=subprocess.DEVNULL,
            stdout=subprocess.DEVNULL,
            stderr=subprocess.DEVNULL,
            check=False,
            timeout=10,
        )


class Worker:
    def __init__(self, api: ApiClient, sandbox: PodmanSandbox):
        self._api = api
        self._sandbox = sandbox

    def run_once(self) -> bool:
        job = self._api.claim()
        if job is None:
            return False
        job_id = str(job.get("id", ""))
        lease = str(job.get("leaseToken", ""))
        try:
            results = self._sandbox.execute(job)
            self._api.complete(job_id, lease, results)
            LOG.info("job_complete job_id=%s", _safe_log_id(job_id))
        except LeaseLost:
            LOG.warning("job_lease_lost job_id=%s", _safe_log_id(job_id))
        except RunnerError as exception:
            # Infrastructure failures are deliberately not acknowledged. The database lease
            # expires and the job is retried by this or another worker.
            LOG.error("job_retryable_failure job_id=%s error=%s", _safe_log_id(job_id), type(exception).__name__)
            raise
        return True


def _validate_job(job: dict[str, Any]) -> tuple[str, str, str, list[dict[str, Any]]]:
    try:
        job_id = str(uuid.UUID(str(job["id"])))
        language = str(job["language"])
        source = str(job["sourceCode"])
        lease = str(job["leaseToken"])
        raw_cases = job["cases"]
    except (KeyError, TypeError, ValueError) as exception:
        raise RunnerError("The API returned an invalid job envelope.") from exception
    if language not in LANGUAGES or not 1 <= len(source) <= 50_000 or len(lease) < 32:
        raise RunnerError("The API returned an invalid job definition.")
    if not isinstance(raw_cases, list) or not 1 <= len(raw_cases) <= 20:
        raise RunnerError("The API returned an invalid test-case collection.")
    cases: list[dict[str, Any]] = []
    for expected_position, case in enumerate(raw_cases, 1):
        if not isinstance(case, dict) or case.get("position") != expected_position:
            raise RunnerError("Test-case positions must be contiguous and ordered.")
        test_input = case.get("input")
        if not isinstance(test_input, str) or len(test_input) > 100_000:
            raise RunnerError("A test input is invalid or too large.")
        cases.append({"position": expected_position, "input": test_input})
    return job_id, language, source, cases


def _parse_protocol(output: str, expected_cases: int) -> list[dict[str, Any]]:
    lines = [line for line in output.splitlines() if line]
    if not lines or not lines[0].startswith("COMPILE\t"):
        raise RunnerError("The sandbox returned an invalid protocol header.")
    compile_parts = lines[0].split("\t", 2)
    if len(compile_parts) != 3:
        raise RunnerError("The sandbox returned an invalid compile result.")
    compile_status, compile_diagnostic = compile_parts[1], _decode_base64(compile_parts[2])
    if compile_status != "OK":
        if compile_status != "COMPILE_ERROR":
            raise RunnerError("The sandbox returned an unknown compile status.")
        return [
            _case_result(position, "COMPILE_ERROR", "", compile_diagnostic, "0")
            for position in range(1, expected_cases + 1)
        ]

    results: list[dict[str, Any]] = []
    for line in lines[1:]:
        parts = line.split("\t")
        if len(parts) != 7 or parts[0] != "CASE":
            raise RunnerError("The sandbox returned a malformed case result.")
        try:
            position = int(parts[1])
            elapsed_seconds = f"{int(parts[3]) / 1000:.3f}"
        except ValueError as exception:
            raise RunnerError("The sandbox returned invalid numeric fields.") from exception
        status = parts[2]
        if status not in TERMINAL_CASE_STATUSES:
            raise RunnerError("The sandbox returned an unknown case status.")
        results.append(
            _case_result(position, status, _decode_base64(parts[4]), _decode_base64(parts[5]), elapsed_seconds)
        )
    if len(results) != expected_cases or [result["position"] for result in results] != list(range(1, expected_cases + 1)):
        raise RunnerError("The sandbox returned an incomplete result set.")
    return results


def _case_result(position: int, status: str, actual: str, diagnostic: str, seconds: str) -> dict[str, Any]:
    return {
        "position": position,
        "status": status,
        "actualOutput": actual,
        "diagnostic": diagnostic,
        "timeSeconds": seconds,
        "memoryKilobytes": None,
    }


def _decode_base64(value: str) -> str:
    if not value:
        return ""
    if len(value) > 100_000:
        raise RunnerError("The sandbox output exceeded its protocol limit.")
    try:
        return base64.b64decode(value, validate=True).decode("utf-8", errors="replace")
    except (ValueError, binascii.Error) as exception:
        raise RunnerError("The sandbox returned malformed encoded output.") from exception


def _run_checked(command: list[str], timeout: float) -> None:
    completed = subprocess.run(
        command,
        stdin=subprocess.DEVNULL,
        stdout=subprocess.DEVNULL,
        stderr=subprocess.PIPE,
        check=False,
        timeout=timeout,
    )
    if completed.returncode != 0:
        raise RunnerError(f"Required Podman resource is unavailable: {_decode_process_output(completed.stderr)[:200]}")


def _decode_process_output(value: bytes) -> str:
    return value[:1_000_000].decode("utf-8", errors="replace").replace("\x00", "")


def _safe_log_id(value: str) -> str:
    return re.sub(r"[^a-fA-F0-9-]", "", value)[:36]


def _bounded_int(name: str, default: int, minimum: int, maximum: int) -> int:
    try:
        value = int(os.getenv(name, str(default)))
    except ValueError as exception:
        raise RunnerError(f"{name} must be an integer.") from exception
    if not minimum <= value <= maximum:
        raise RunnerError(f"{name} must be between {minimum} and {maximum}.")
    return value


def _bounded_float(name: str, default: float, minimum: float, maximum: float) -> float:
    try:
        value = float(os.getenv(name, str(default)))
    except ValueError as exception:
        raise RunnerError(f"{name} must be numeric.") from exception
    if not minimum <= value <= maximum:
        raise RunnerError(f"{name} must be between {minimum} and {maximum}.")
    return value


def main() -> int:
    parser = argparse.ArgumentParser(description="Claim and execute PrepPilot code-run jobs with Podman.")
    parser.add_argument("--once", action="store_true", help="Claim at most one job and then exit.")
    parser.add_argument("--healthcheck", action="store_true", help="Validate configuration, Podman and images.")
    arguments = parser.parse_args()
    logging.basicConfig(level=os.getenv("RUNNER_LOG_LEVEL", "INFO"), format="%(asctime)s %(levelname)s %(message)s")
    settings = Settings.from_environment()
    sandbox = PodmanSandbox(settings)
    sandbox.verify()
    if arguments.healthcheck:
        LOG.info("runner_healthcheck_ok")
        return 0

    api = ApiClient(settings)
    worker = Worker(api, sandbox)
    stopping = False

    def stop(_signum: int, _frame: Any) -> None:
        nonlocal stopping
        stopping = True

    signal.signal(signal.SIGTERM, stop)
    signal.signal(signal.SIGINT, stop)
    while not stopping:
        try:
            processed = worker.run_once()
        except RunnerAuthenticationError:
            # Authentication failures cannot recover without a configuration change.
            # Exit instead of creating an infinite retry loop that leaves the UI queued.
            LOG.error("runner_authentication_failed check=RUNNER_WORKER_TOKEN")
            return 1
        except RECOVERABLE_WORKER_ERRORS as exception:
            # API restarts and short network interruptions are expected when the app and
            # worker live on separate VMs. Log only the exception class so request data,
            # source code, credentials and test cases can never leak into runner logs.
            LOG.warning("api_temporarily_unavailable error=%s", type(exception).__name__)
            processed = False
        if arguments.once:
            return 0 if processed else 2
        if not processed:
            time.sleep(settings.poll_seconds)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
