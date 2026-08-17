# Self-hosted code execution

PrepPilot executes Java, C++, and Python without RapidAPI or another paid execution provider. The Spring API only validates requests and persists jobs. A separate worker claims those jobs and invokes rootless Podman; submitted code runs only inside short-lived sandbox containers.

## Architecture

```mermaid
sequenceDiagram
    actor User
    participant UI as React Interview Lab
    participant API as Spring Boot API
    participant DB as PostgreSQL queue
    participant Worker as Python worker
    participant Podman as Rootless Podman
    participant Box as Language sandbox

    User->>UI: Run tests
    UI->>API: Source + language + owned session
    API->>API: Validate login, ownership, timer, size, quota, cooldown
    API->>DB: Insert QUEUED submission
    API-->>UI: 202 Accepted + run ID
    loop Poll while idle
        Worker->>API: Authenticated claim request
        API->>DB: SELECT ... FOR UPDATE SKIP LOCKED
    end
    API->>DB: Mark RUNNING + issue 60-second lease
    API-->>Worker: Source + ordered test inputs
    Worker->>Podman: Fixed run arguments and resource policy
    Podman->>Box: Compile once, execute six cases
    Box-->>Worker: Bounded line protocol
    Worker->>API: Results + per-job lease token
    API->>API: Compare outputs to server-held answers
    API->>DB: Save PASSED, FAILED, or ERROR
    UI->>API: Poll owned run ID
    API-->>UI: Visible details + hidden status only
```

Expected outputs never appear in the worker claim. The API performs the final comparison and stores no hidden input, expected output, or actual output in the user-visible result JSON.

## Durable queue behavior

`coding_execution_submission` is both the user history and the queue:

- New runs are committed as `QUEUED`; there is no in-memory event to lose during an API restart.
- A worker atomically claims the oldest eligible row using `FOR UPDATE SKIP LOCKED`.
- Each claim receives a random 256-bit lease token. PostgreSQL stores only its SHA-256 hash.
- The default lease is 60 seconds; one sandbox job is capped at 30 seconds.
- A crashed worker leaves the row recoverable after the lease expires.
- A job is tried at most three times, then becomes `ERROR` with a safe generic message.
- Repeating a completion with an expired or already-used lease returns `409 Conflict`.

This design supports one worker now and multiple workers later without double-claiming a row.

## Sandbox policy

The worker never builds a shell command from source code, language names, image names, or job IDs. It passes a fixed argument list to Podman and mounts a generated input directory read-only.

Each job has:

- rootless Podman and a non-root UID inside the image;
- no network and no shared host network namespace;
- read-only root filesystem and a bounded temporary filesystem;
- all Linux capabilities dropped and `no-new-privileges`;
- Podman's default seccomp profile;
- one CPU, 512 MB memory/swap, 64 PIDs, and 64 open files;
- 10-second compile timeout and 2-second timeout per test;
- 30-second whole-container timeout;
- 32 KB returned output and bounded diagnostic/file sizes;
- Java annotation processing disabled during compilation;
- C++ stack protector, fortified libc calls, RELRO, immediate binding, and non-executable stack flags;
- Python isolated mode (`-I`).

The API additionally limits source to 50,000 characters, allows one active run per interview, enforces a three-second cooldown, and defaults to five clicks per user per rolling 24 hours.

Container isolation is a strong boundary, not a promise of perfect safety. Keep Podman, the Linux kernel, compilers, and base images patched. Do not run this worker on the app/database VM in production.

## Local setup with Podman

1. Generate one worker credential:

   ```bash
   openssl rand -base64 48
   ```

2. Put it in the root `.env`:

   ```dotenv
   RUNNER_ENABLED=true
   RUNNER_WORKER_TOKEN=the-generated-value
   RUNNER_DAILY_LIMIT=5
   RUNNER_MINIMUM_INTERVAL=3s
   RUNNER_LEASE_DURATION=60s
   RUNNER_MAX_ATTEMPTS=3
   ```

3. Copy `runner/.env.example` to `runner/.env`, put the same token there, and keep the local API URL:

   ```dotenv
   RUNNER_API_URL=http://127.0.0.1:8080
   RUNNER_WORKER_TOKEN=the-same-generated-value
   ```

4. Start/rebuild the application, build the three ARM64-compatible images, and verify them:

   ```bash
   make restart
   make runner-images
   make runner-health
   ```

5. In a second terminal, start the worker:

   ```bash
   make runner
   ```

6. Sign in, start an Interview Lab round, and click **Run tests**. The browser polls the PrepPilot API; it never talks to Podman.

Use `make runner-once` when debugging one queued job. `make runner-test` runs the dependency-free protocol unit tests.

## Worker configuration

| Variable | Default | Purpose |
|---|---:|---|
| `RUNNER_API_URL` | `http://127.0.0.1:8080` | API base URL; non-local URLs must use HTTPS |
| `RUNNER_WORKER_TOKEN` | none | Shared worker credential, at least 32 characters |
| `RUNNER_POLL_SECONDS` | `2` | Delay when no work is available |
| `RUNNER_CONTAINER_TIMEOUT_SECONDS` | `30` | Whole sandbox deadline |
| `RUNNER_COMPILE_TIMEOUT_SECONDS` | `10` | Compiler deadline |
| `RUNNER_TEST_TIMEOUT_SECONDS` | `2` | Deadline for each test input |
| `RUNNER_MAX_OUTPUT_BYTES` | `32768` | Captured stdout/diagnostic bound |
| `RUNNER_MEMORY_LIMIT` | `512m` | Container memory and swap ceiling |
| `RUNNER_CPU_LIMIT` | `1.0` | Container CPU ceiling |
| `RUNNER_PIDS_LIMIT` | `64` | Process/thread ceiling |
| `RUNNER_IMAGE_*` | local tagged images | Fixed Java/C++/Python image references |

The worker accepts plain HTTP only for `localhost` or `127.0.0.1`. The OCI runner must use the public HTTPS application URL with normal certificate validation.

## Failure behavior

| Symptom | Meaning | Action |
|---|---|---|
| UI says runner is not configured | API has `RUNNER_ENABLED=false` | Set the root environment and recreate the API |
| API refuses startup | Worker token is shorter than 32 characters or limits are unsafe | Generate a longer token and review runner variables |
| `make runner-health` fails on Podman | Podman is stopped or an image is missing | Start the Podman machine and run `make runner-images` |
| Job stays `QUEUED` | No worker can reach/authenticate to the API | Check worker service logs, URL, TLS, and token |
| Job returns to the queue | Worker crashed or exceeded its lease | Inspect Podman/worker logs; the retry is intentional |
| Job becomes `ERROR` | Three infrastructure attempts expired | Fix the runner, then let the user create a new run |
| `COMPILE_ERROR` | Submitted program did not compile | Fix the source shown in the editor |
| `TIME_LIMIT` | A test exceeded two seconds | Improve the algorithm or remove an infinite loop |

## Security regression checklist

Run these after changing images, Podman flags, or the sandbox script:

- correct and incorrect solutions in all three languages;
- compile errors and uncaught runtime errors;
- infinite loop and sleep beyond two seconds;
- fork/thread bomb attempt;
- allocation beyond the memory ceiling;
- stdout/stderr flood;
- write attempts outside `/tmp`;
- outbound DNS and HTTP connection attempts;
- worker termination after claim, followed by lease recovery;
- duplicate completion and an invalid worker/lease token.

Deployment instructions are in [OCI_FREE_DEPLOYMENT.md](OCI_FREE_DEPLOYMENT.md).
