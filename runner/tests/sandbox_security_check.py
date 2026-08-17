#!/usr/bin/env python3
"""Opt-in Podman integration checks for the untrusted-code boundary."""

from __future__ import annotations

import importlib.util
import pathlib
import sys
import uuid


MODULE_PATH = pathlib.Path(__file__).parents[1] / "worker.py"
SPEC = importlib.util.spec_from_file_location("preppilot_worker_security", MODULE_PATH)
worker = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
sys.modules[SPEC.name] = worker
SPEC.loader.exec_module(worker)


def job(language: str, source: str, test_input: str = "") -> dict:
    return {
        "id": str(uuid.uuid4()),
        "leaseToken": "integration-check-only-lease-token-000000000000",
        "language": language,
        "sourceCode": source,
        "cases": [{"position": 1, "input": test_input}],
    }


def require(condition: bool, message: str) -> None:
    if not condition:
        raise AssertionError(message)


def main() -> int:
    settings = worker.Settings.from_environment()
    sandbox = worker.PodmanSandbox(settings)
    sandbox.verify()

    programs = {
        "JAVA": """import java.io.*; public class Main { public static void main(String[] a) throws Exception { System.out.println(Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine()) * 2); } }""",
        "CPP": """#include <iostream>\nint main(){int x;std::cin>>x;std::cout<<x*2<<'\\n';}""",
        "PYTHON": """print(int(input()) * 2)""",
    }
    for language, source in programs.items():
        result = sandbox.execute(job(language, source, "21\n"))[0]
        require(result["status"] == "EXECUTED", f"{language} smoke status was {result['status']}")
        require(result["actualOutput"].strip() == "42", f"{language} smoke output was unexpected")

    timeout_result = sandbox.execute(job("PYTHON", "while True:\n    pass\n"))[0]
    require(timeout_result["status"] == "TIME_LIMIT", "Infinite loop escaped the per-test timeout")

    isolation_source = """
import pathlib
import socket

filesystem_blocked = False
network_blocked = False
try:
    pathlib.Path('/escape').write_text('no')
except OSError:
    filesystem_blocked = True
try:
    socket.create_connection(('1.1.1.1', 80), timeout=0.2)
except OSError:
    network_blocked = True
print('blocked' if filesystem_blocked and network_blocked else 'unsafe')
"""
    isolation_result = sandbox.execute(job("PYTHON", isolation_source))[0]
    require(isolation_result["actualOutput"].strip() == "blocked", "Filesystem or network isolation failed")

    output_result = sandbox.execute(job("PYTHON", "print('x' * 1000000)"))[0]
    require(len(output_result["actualOutput"].encode("utf-8")) <= settings.max_output_bytes,
            "Captured output exceeded the configured limit")

    fork_result = sandbox.execute(job("PYTHON", "import os\nwhile True:\n    os.fork()\n"))[0]
    require(fork_result["status"] in {"RUNTIME_ERROR", "TIME_LIMIT"}, "PID exhaustion was not contained")

    print("Sandbox security checks passed: languages, timeout, filesystem, network, output, and PID limits.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
