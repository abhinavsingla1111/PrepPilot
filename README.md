# PrepPilot

PrepPilot is a focused coding-interview workspace: structured Fundamentals, a curated DSA library, end-to-end system-design notes, passwordless sign-in, personal progress tracking, timed knowledge checks, scheduled revision, and a coding Interview Lab.

This repository contains the working full-stack application described in [DESIGN.md](DESIGN.md).

## What works

- Responsive React experience with persistent light and dark color schemes.
- Searchable, filterable, paginated library of all 145 questions in the source CSV.
- Direct LeetCode link on every question.
- Email-based six-digit OTP flow with short-lived challenges and PostgreSQL-backed resend/email/IP throttling.
- Secure random, hashed, revocable sessions in an HttpOnly cookie—no auth token in browser storage.
- Per-user solved/not-started progress persisted in PostgreSQL.
- Eight interview-focused Fundamentals tracks, including complete Computer Networks and Operating Systems curricula with visual flows and interview revision.
- Detailed HLD and LLD examples with requirements, estimates, APIs, data models, diagrams, trade-offs, and FAQs.
- Nine interview-focused Knowledge Check topics: OOPS, Java, Spring Boot, React, C++, Python, MySQL, Computer Networks, and Operating Systems.
- A 900-question assessment bank with four options, answer keys, difficulty levels, and concise explanations.
- Server-timed 20-question tests with topic-aware 10–15 minute limits, autosaved answers, balanced difficulty, five non-repeating attempts per topic, scoring, answer review, and attempt history.
- A private Mistake Book that automatically schedules incorrect and unanswered knowledge-check questions using adaptive recall intervals.
- A useful coding Interview Lab with 20 curated prompts, full descriptions and constraints, problem-specific runnable starters for Java/C++/Python, public and hidden tests, a server-enforced 45-minute timer, autosave, verified test-case scoring, a secondary three-part self-assessment, execution history, and post-round reference guidance.
- Optional asynchronous execution through a self-hosted, rootless Podman worker for Java, C++, and Python, with durable leased jobs, network-disabled sandboxes, resource limits, per-user quotas, private hidden tests, and optional completion email.
- Authenticated issue and improvement requests with validated image attachments, PostgreSQL persistence, and email notifications.
- Spring Boot controller → service → repository layering by domain.
- Flyway-managed schema and automatic idempotent dataset seed.
- Typed Spring Cloud OpenFeign client for LeetCode's public GraphQL endpoint.
- H2-backed integration tests for the API boundary; PostgreSQL is the application database.

The OTP delivery contract is isolated behind `OtpSender`, so the configured mail adapter can change without affecting the controller or authentication service. Keep `EXPOSE_DEV_OTP=false` whenever email delivery is enabled; local preview codes are an explicit development-only option.

## Structure

```text
PrepPilot/
├── frontend/                 React 19 + TypeScript + Vite
│   ├── src/components/       Learning, checks, revision, lab, auth, and feedback UI
│   ├── src/lib/              API client and offline preview data
│   └── public/og.png         Site-specific social card
├── backend/                  Java 21 + Spring Boot 3.5
│   ├── auth/                 OTP, cookie sessions, security filter
│   ├── assessment/           Test lifecycle, scoring, history, question seeding
│   ├── review/               Mistake Book and spaced-review scheduling
│   ├── interview/            Timed sessions, 20 prompts, execution, rubrics
│   ├── problem/              Catalog, filtering, CSV seeding
│   ├── progress/             User progress API
│   ├── integration/leetcode/ Feign client and integration service
│   └── db/migration/         PostgreSQL schema
├── runner/                   Dependency-free worker + hardened language images
├── docker-compose.yml        Local PostgreSQL
├── Dockerfile.api            Deployable API image
└── DESIGN.md                 Product and system design
```

The backend is organized by domain rather than by one giant technical-layer folder. Inside each domain the controller, service, repository, entity, and DTO responsibilities remain explicit and small.

## Run locally

Prerequisites: Java 21, Maven 3.9+, Node 20.19+, pnpm 10+, and Podman or Docker.

1. Start PostgreSQL:

   ```bash
   docker compose up -d postgres
   ```

2. Start the API with the local OTP preview enabled:

   ```bash
   cd backend
   SPRING_PROFILES_ACTIVE=local mvn spring-boot:run
   ```

3. In a second terminal, start the web app:

   ```bash
   cd frontend
   pnpm install
   pnpm dev
   ```

4. Open `http://localhost:5173`. Enter any valid email. The local one-time code appears inside the sign-in dialog and in the API log.

The Vite development server proxies `/api` to `http://localhost:8080`, so no frontend environment variable is needed locally.

### Full containerized stack (podman)

To build and run everything (postgres + api + web) in containers, use the Makefile:

```bash
make up      # build (incremental) and (re)deploy the latest code — app on http://localhost:8081
make logs    # tail logs
make down    # stop containers (keeps the postgres volume)
```

`make up` wraps `podman-compose up -d --build --force-recreate`. The Dockerfiles use
build cache mounts for the Maven `.m2` repo and the pnpm store, so rebuilds are incremental
and fast (no more `--no-cache`). `--force-recreate` is required because podman-compose does
not recreate a container when only its image changes.

## Verify

```bash
cd backend
mvn test

cd ../frontend
pnpm lint
pnpm build
```

The backend tests migrate a fresh H2 database in PostgreSQL compatibility mode, seed all 145 DSA rows, 900 assessment questions, and 20 coding prompts. They cover OTP login, owner-scoped data, solved progress, five non-repeating knowledge checks, topic-specific deadlines, mistake scheduling, the coding-interview lifecycle, durable runner claims and leases, hidden-case redaction, and authenticated feedback uploads. `make runner-test` covers the worker protocol separately.

## API surface

| Method | Route | Access | Purpose |
|---|---|---|---|
| `POST` | `/api/v1/auth/otp/request` | Public | Create and deliver a six-digit OTP |
| `POST` | `/api/v1/auth/otp/verify` | Public | Verify OTP and issue an HttpOnly session |
| `GET` | `/api/v1/auth/me` | Signed in | Return the current user |
| `POST` | `/api/v1/auth/logout` | Signed in | Revoke and clear the session |
| `GET` | `/api/v1/problems` | Public | Search/filter/paginate questions |
| `GET` | `/api/v1/problems/topics` | Public | List available topics |
| `GET` | `/api/v1/progress` | Signed in | List the user's saved progress |
| `PUT` | `/api/v1/progress/{problemId}` | Signed in | Update a question's status |
| `GET` | `/api/v1/leetcode/{username}/recent` | Signed in | Read recent public accepted submissions through Feign |
| `GET` | `/api/v1/assessments/topics` | Signed in | List topics, availability, and the user's progress |
| `POST` | `/api/v1/assessments/topics/{topic}/attempts` | Signed in | Start or resume a timed attempt |
| `GET` | `/api/v1/assessments/topics/{topic}/history` | Signed in | List the user's earlier attempts for one topic |
| `GET` | `/api/v1/assessments/attempts/{attemptId}` | Signed in | Resume or review an owned attempt |
| `PUT` | `/api/v1/assessments/attempts/{attemptId}/questions/{questionId}` | Signed in | Autosave one selected option |
| `POST` | `/api/v1/assessments/attempts/{attemptId}/submit` | Signed in | Score and complete an attempt |
| `GET` | `/api/v1/reviews/summary` | Signed in | Return due, saved, and mastered revision counts |
| `GET` | `/api/v1/reviews` | Signed in | Load due or complete Mistake Book items |
| `POST` | `/api/v1/reviews/{reviewId}/rating` | Signed in | Rate recall and schedule the next review |
| `POST` | `/api/v1/interviews/coding/sessions` | Signed in | Start or resume a timed coding interview |
| `GET` | `/api/v1/interviews/coding/sessions` | Signed in | List the user's coding-interview history |
| `GET` | `/api/v1/interviews/coding/sessions/{sessionId}` | Signed in | Resume or review an owned coding session |
| `PUT` | `/api/v1/interviews/coding/sessions/{sessionId}/draft` | Signed in | Autosave language, code, approach, and complexity |
| `POST` | `/api/v1/interviews/coding/sessions/{sessionId}/submit` | Signed in | Save the rubric and reveal reference guidance |
| `GET` | `/api/v1/interviews/coding/runner` | Signed in | Read runner availability and remaining quota |
| `POST` | `/api/v1/interviews/coding/sessions/{sessionId}/runs` | Signed in | Queue sandboxed execution against public and hidden cases |
| `GET` | `/api/v1/interviews/coding/sessions/{sessionId}/runs` | Signed in | Read run history for an owned session |
| `GET` | `/api/v1/interviews/coding/runs/{runId}` | Signed in | Poll one owned asynchronous execution |
| `GET` | `/api/v1/fundamentals/{docId}` | Public | Load one curated Fundamentals chapter |
| `POST` | `/api/v1/feedback` | Signed in | Save an issue/improvement, validate an optional image, and notify the configured inbox |

## Database

PostgreSQL is the correct choice for this project. It keeps users, OTP challenges, sessions, questions, and progress relational now, and leaves a clean upgrade path to `pgvector` for RAG later.

Flyway owns the schema. Hibernate runs with `ddl-auto: validate`, so entity drift fails early instead of silently changing production tables.

Current tables:

- `app_user`
- `otp_challenge`
- `auth_session`
- `problem`
- `user_progress`
- `assessment_question`
- `assessment_attempt`
- `assessment_attempt_question`
- `feedback_request`
- `review_item`
- `coding_interview_prompt`
- `coding_interview_session`
- `coding_execution_submission`

## Configuration

Copy [.env.example](.env.example) as a reference and set hosted values in the deployment platform, not in Git.

Important production settings:

- Set a long random `AUTH_TOKEN_PEPPER`.
- Keep `EXPOSE_DEV_OTP=false`.
- Set `AUTH_COOKIE_SECURE=true` on HTTPS.
- Set `ALLOWED_ORIGINS` to the exact frontend origin.
- Set `FEEDBACK_RECIPIENT_EMAIL` to the inbox that should receive feedback. SMTP falls back to `MAIL_USERNAME`; Resend requires an explicit recipient.
- For hosted delivery, set `MAIL_ENABLED=true`, `MAIL_PROVIDER=resend`, `RESEND_API_KEY`, and a `MAIL_FROM` address on a domain verified in Resend. The same HTTPS adapter sends OTP, feedback, and optional runner-result emails. SMTP remains available with `MAIL_PROVIDER=smtp` for local or private infrastructure.
- Keep the default OTP limits unless usage justifies changing them: one request per email per 60 seconds, five per email per 15 minutes, and twenty per client IP per 15 minutes. Set `FORWARD_HEADERS_STRATEGY=FRAMEWORK` only when all public requests pass through your trusted reverse proxy; otherwise client-supplied forwarding headers must remain untrusted.
- Keep frontend and API same-origin through a proxy when possible; this gives the HttpOnly session the most reliable browser behavior.

Minimal hosted mail configuration:

```dotenv
MAIL_ENABLED=true
MAIL_PROVIDER=resend
RESEND_API_KEY=store-this-only-in-the-hosted-secret-environment
MAIL_FROM=PrepPilot <login@mail.your-domain.example>
FEEDBACK_RECIPIENT_EMAIL=you@your-domain.example
EXPOSE_DEV_OTP=false
FORWARD_HEADERS_STRATEGY=FRAMEWORK
```

Create a restricted sending API key in Resend, verify the sending domain/subdomain, and publish its SPF and DKIM records. Resend's current free tier documents 3,000 emails per month and 100 per day; verify the [official pricing](https://resend.com/docs/knowledge-base/what-is-resend-pricing) before deployment because provider limits can change.

### Optional self-hosted code runner

The Spring process and API container never compile user code or receive access to Podman. `RUNNER_ENABLED=false` is the safe default. To enable verified runs, generate one dedicated worker credential and put the same value in the root `.env` and `runner/.env`:

```dotenv
RUNNER_ENABLED=true
RUNNER_WORKER_TOKEN=generate-with-openssl-rand-base64-48
RUNNER_DAILY_LIMIT=5
RUNNER_LEASE_DURATION=60s
RUNNER_MAX_ATTEMPTS=3
RUNNER_EMAIL_RESULTS=false
```

Then build and start the worker in its own terminal:

```bash
cp runner/.env.example runner/.env
make runner-images
make runner-health
make runner
```

The worker polls a private authenticated API, claims PostgreSQL-backed jobs with expiring leases, and launches one short-lived sandbox container per run. Submitted code never reaches a shell command. Every container has no network, a read-only root filesystem, a non-root user, dropped capabilities, default seccomp, `no-new-privileges`, and bounded CPU, memory, processes, files, output, compilation, test, and whole-job time. The default limit remains five run clicks per user per rolling day. See [Code execution](docs/CODE_EXECUTION.md) for local setup and [OCI deployment](docs/OCI_FREE_DEPLOYMENT.md) for the two-VM free-tier layout.

### UI theme

The header theme control switches between accessible light and dark color schemes. A visitor's choice is kept in browser storage; on the first visit, PrepPilot follows the operating-system preference. The default `ocean` palette uses neutral surfaces, an Apple-inspired blue action color, and restrained topic accents. Set `VITE_UI_THEME=lime` before `pnpm dev`, `pnpm build`, or `make up` to restore the legacy lime global accent.

All scheme and palette values live in [`frontend/src/theme.css`](frontend/src/theme.css). Edit the semantic surface, text, action, and accent tokens there instead of scattering color values through components. Container builds receive `VITE_UI_THEME` through the web image build argument, because Vite embeds public environment values at build time.

## $0 deployment path for fewer than 10 users

Use two OCI Ampere A1 Always Free VMs in the home region, each with 1 OCPU and 6 GB RAM:

- **App VM:** web proxy, React static build, Spring API, and PostgreSQL. Only ports 80/443 are public; SSH is restricted to your IP and PostgreSQL is never public.
- **Runner VM:** the worker plus rootless Podman and the three ARM64 sandbox images. It has no inbound application port; it only makes outbound HTTPS requests to the app VM.

Oracle's current Always Free documentation equates the Ampere allowance to 2 OCPUs and 12 GB RAM total, and permits splitting it across two VMs. Capacity and free-tier terms can change, so verify the [official Always Free resource page](https://docs.oracle.com/en-us/iaas/Content/FreeTier/freetier_topic-Always_Free_Resources.htm) before provisioning. The full secure setup and systemd service are in [docs/OCI_FREE_DEPLOYMENT.md](docs/OCI_FREE_DEPLOYMENT.md).

## Next iteration

1. Extend the Mistake Book to DSA confidence ratings and user-created review notes.
2. Add a system-design Interview Lab using requirements, capacity, API, data-model, architecture, and trade-off rubrics.
3. Add behavioral STAR-story practice and project/resume deep-dive sessions.
4. Add administrator-visible runner health/usage metrics and a cleanup policy for old source submissions.
5. Add reviewed approach templates and deterministic evals before optional AI coaching.

PrepPilot is independent and is not affiliated with LeetCode. It stores factual question metadata and links out; it does not redistribute problem statements or editorials.
