import importlib.util
import http.client
import pathlib
import sys
import types
import unittest
import urllib.error
from unittest import mock


MODULE_PATH = pathlib.Path(__file__).parents[1] / "worker.py"
SPEC = importlib.util.spec_from_file_location("preppilot_worker", MODULE_PATH)
worker = importlib.util.module_from_spec(SPEC)
assert SPEC.loader is not None
sys.modules[SPEC.name] = worker
SPEC.loader.exec_module(worker)


class ProtocolTest(unittest.TestCase):
    def test_parses_successful_cases(self):
        output = "\n".join(
            [
                "COMPILE\tOK\t",
                "CASE\t1\tEXECUTED\t12\tNDIK\t\t",
                "CASE\t2\tTIME_LIMIT\t2001\t\tVGltZWQgb3V0Lg==\t",
            ]
        )

        results = worker._parse_protocol(output, 2)

        self.assertEqual(results[0]["actualOutput"], "42\n")
        self.assertEqual(results[0]["timeSeconds"], "0.012")
        self.assertEqual(results[1]["status"], "TIME_LIMIT")
        self.assertEqual(results[1]["diagnostic"], "Timed out.")

    def test_expands_compile_error_to_every_case(self):
        results = worker._parse_protocol("COMPILE\tCOMPILE_ERROR\tQmFkIGNvZGUu\n", 3)

        self.assertEqual(len(results), 3)
        self.assertTrue(all(result["status"] == "COMPILE_ERROR" for result in results))
        self.assertTrue(all(result["diagnostic"] == "Bad code." for result in results))

    def test_rejects_missing_case(self):
        with self.assertRaises(worker.RunnerError):
            worker._parse_protocol("COMPILE\tOK\t\nCASE\t1\tEXECUTED\t1\t\t\t\n", 2)


class JobValidationTest(unittest.TestCase):
    def test_accepts_only_contiguous_positions(self):
        job = {
            "id": "12345678-1234-1234-1234-123456789abc",
            "leaseToken": "x" * 32,
            "language": "PYTHON",
            "sourceCode": "print(input())",
            "cases": [{"position": 2, "input": "hello\n"}],
        }

        with self.assertRaises(worker.RunnerError):
            worker._validate_job(job)


class NetworkResilienceTest(unittest.TestCase):
    def test_remote_disconnect_is_a_recoverable_http_failure(self):
        exception = http.client.RemoteDisconnected("API restarted")

        self.assertIsInstance(exception, worker.RECOVERABLE_WORKER_ERRORS)

    def test_authentication_failure_is_explicit_and_contains_no_token(self):
        settings = types.SimpleNamespace(
            api_url="http://127.0.0.1:8080",
            worker_token="sensitive-runner-token-that-must-not-appear",
            request_timeout_seconds=2,
        )
        response = urllib.error.HTTPError(settings.api_url, 401, "Unauthorized", {}, None)

        with mock.patch.object(worker.urllib.request, "urlopen", side_effect=response):
            with self.assertRaises(worker.RunnerAuthenticationError) as caught:
                worker.ApiClient(settings).claim()

        self.assertNotIn(settings.worker_token, str(caught.exception))


if __name__ == "__main__":
    unittest.main()
