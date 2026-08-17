# OCI Always Free deployment: two ARM VMs

This is the intended hosted shape for fewer than ten users and no paid code-execution provider.

Oracle's Always Free documentation currently provides 1,500 Ampere A1 OCPU-hours and 9,000 GB-hours each month—equivalent to **2 OCPUs and 12 GB RAM total**—and permits one or two A1 instances in the tenancy's home region. Free capacity is not guaranteed and the limits can change, so confirm the [official Always Free resources](https://docs.oracle.com/en-us/iaas/Content/FreeTier/freetier_topic-Always_Free_Resources.htm) immediately before provisioning.

## Allocation

| VM | Shape | Workload | Public ingress |
|---|---|---|---|
| App | `VM.Standard.A1.Flex`, 1 OCPU, 6 GB | Caddy, web, Spring API, PostgreSQL | 80/443; SSH only from your IP |
| Runner | `VM.Standard.A1.Flex`, 1 OCPU, 6 GB | Python worker, rootless Podman, three language images | SSH only from your IP; no app port |

Use Ubuntu's current OCI ARM image. Allocate at least the minimum boot volume to each VM while remaining inside the tenancy's Always Free block-storage allowance.

## Network rules

1. Put both VMs in one VCN. Assign public IPs only if needed for administration and outbound access.
2. App VM ingress: TCP 80/443 from the internet; TCP 22 only from your fixed IP/CIDR.
3. Runner VM ingress: TCP 22 only from your fixed IP/CIDR. Do not open a runner or container-engine port.
4. Never expose PostgreSQL 5432, Spring 8080, web 8081, or a Podman/Docker socket publicly.
5. The worker calls `https://your-domain.example/api/internal/runner/...` outbound. It requires a publicly trusted TLS certificate and validates it normally.

## App VM

1. Install Git, Podman, podman-compose, and Caddy from trusted OS/vendor repositories. Apply OS updates first.
2. Clone the repository into a dedicated unprivileged account.
3. Copy `.env.example` to `.env`, restrict it to that account (`chmod 600`), and set production values:

   ```dotenv
   AUTH_COOKIE_SECURE=true
   AUTH_COOKIE_SAME_SITE=Strict
   EXPOSE_DEV_OTP=false
   ALLOWED_ORIGINS=https://your-domain.example
   APP_PUBLIC_URL=https://your-domain.example
   FORWARD_HEADERS_STRATEGY=FRAMEWORK
   MAIL_ENABLED=true
   MAIL_PROVIDER=resend
   RESEND_API_KEY=store-a-restricted-resend-key-here
   MAIL_FROM=PrepPilot <login@mail.your-domain.example>
   FEEDBACK_RECIPIENT_EMAIL=you@your-domain.example
   RUNNER_ENABLED=true
   RUNNER_WORKER_TOKEN=generate-a-dedicated-48-byte-random-value
   RUNNER_DAILY_LIMIT=5
   RUNNER_LEASE_DURATION=60s
   RUNNER_MAX_ATTEMPTS=3
   ```

   Use different random values for the database password, authentication pepper, and worker token. Do not copy examples into production. Verify the `MAIL_FROM` domain in Resend and publish its SPF and DKIM records before testing OTP delivery.

4. Run `make up`. Keep the local container ports blocked by the host firewall/security list.
5. Configure Caddy to terminate HTTPS and proxy to the web container:

   ```caddyfile
   your-domain.example {
       encode zstd gzip
       reverse_proxy 127.0.0.1:8081
   }
   ```

6. Enable Caddy and the application stack at boot. Confirm `/actuator/health/readiness` through HTTPS before enabling the worker.

PostgreSQL stays on the private Compose network. Schedule encrypted database backups and periodically test a restore; a free service is still capable of disk or operator failure.

## Runner VM

1. Install Git, Python 3, and rootless Podman. Do not install or expose a privileged Docker daemon.
2. Create a dedicated unprivileged Linux account, clone only the required repository revision, and build the ARM64 images:

   ```bash
   make runner-images
   ```

3. Copy `runner/.env.example` to `runner/.env`, set mode `600`, and configure:

   ```dotenv
   RUNNER_API_URL=https://your-domain.example
   RUNNER_WORKER_TOKEN=the-exact-token-from-the-app-vm
   RUNNER_POLL_SECONDS=2
   RUNNER_MEMORY_LIMIT=512m
   RUNNER_CPU_LIMIT=1.0
   RUNNER_PIDS_LIMIT=64
   ```

4. Run `make runner-health`, then one foreground `make runner` while watching the first controlled job.
5. Install `deploy/oci/preppilot-runner.service` as the runner account's user service, adjust its paths, and enable lingering so it starts after reboot:

   ```bash
   mkdir -p ~/.config/systemd/user
   cp deploy/oci/preppilot-runner.service ~/.config/systemd/user/
   systemctl --user daemon-reload
   systemctl --user enable --now preppilot-runner
   loginctl enable-linger "$USER"
   ```

6. Read logs with `journalctl --user -u preppilot-runner -f`. Logs contain job UUIDs and error classes, never source, test inputs, outputs, worker tokens, or user email addresses.

## Updates

On each application release:

1. Back up PostgreSQL.
2. Pull the reviewed revision on both VMs.
3. App VM: `make restart`, then check readiness and Flyway migration state.
4. Runner VM: `make runner-test && make runner-images && make runner-health`.
5. Restart the user service and run one Java, C++, and Python smoke submission.
6. Prune only unused, reviewed images after the new version is healthy. Never delete the PostgreSQL volume during an update.

The checked-in sandbox base images are pinned to the ARM64 digests validated by the local security suite. Refresh those digests deliberately after reviewing upstream changes, then rebuild and rerun `make runner-security-test` so pinning does not prevent regular OS/compiler security updates.

## Free-tier cautions

- Create Always Free resources only in the home region and choose the Always Free-eligible A1 shape.
- OCI can report temporary capacity exhaustion. Retry another availability domain or later; do not accidentally select a paid shape.
- Set tenancy budgets/alerts and compartment quotas even when using Always Free resources.
- Oracle documents reclamation of idle Always Free compute under stated utilization criteria. Keep backups and treat the deployment as replaceable.
- Two 1-OCPU VMs intentionally consume the current 2-OCPU total; do not add another A1 VM without rechecking the allowance.
