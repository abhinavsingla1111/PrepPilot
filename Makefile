.PHONY: up down restart logs ps rebuild clean runner-images runner runner-once runner-health runner-test runner-security-test

# Build (incrementally, using Docker cache mounts) and (re)deploy the latest code.
# --build          rebuilds images; thanks to the cache mounts in the Dockerfiles
#                  the Maven/pnpm dependencies are cached, so this is fast.
# --force-recreate ensures the containers actually pick up the freshly built image
#                  (podman-compose will NOT recreate a container on image change alone).
up:
	podman-compose up -d --build --force-recreate

# Stop and remove the containers (keeps the postgres data volume).
down:
	podman-compose down

restart: down up

# Tail logs for all services (Ctrl-C to stop).
logs:
	podman-compose logs -f

ps:
	podman ps --format '{{.Names}}\t{{.Status}}'

# Force a clean rebuild that ignores the layer cache (rarely needed now).
rebuild:
	podman-compose build --no-cache && podman-compose up -d --force-recreate

# Full teardown including the postgres data volume.
clean:
	podman-compose down -v

# Build the three local, multi-architecture sandbox images. Run this once after
# installing/updating Podman or after changing runner/sandbox/run.sh.
runner-images:
	podman build --file runner/images/Containerfile.java --tag localhost/preppilot-runner-java:21 .
	podman build --file runner/images/Containerfile.cpp --tag localhost/preppilot-runner-cpp:14 .
	podman build --file runner/images/Containerfile.python --tag localhost/preppilot-runner-python:3.13 .

# The worker deliberately runs on the host. Only it can invoke rootless Podman;
# neither the API nor any application container receives a container-engine socket.
runner:
	@test -f runner/.env || (echo "Copy runner/.env.example to runner/.env first." && exit 1)
	set -a; . ./runner/.env; set +a; exec python3 runner/worker.py

runner-once:
	@test -f runner/.env || (echo "Copy runner/.env.example to runner/.env first." && exit 1)
	set -a; . ./runner/.env; set +a; exec python3 runner/worker.py --once

runner-health:
	@test -f runner/.env || (echo "Copy runner/.env.example to runner/.env first." && exit 1)
	set -a; . ./runner/.env; set +a; python3 runner/worker.py --healthcheck

runner-test:
	python3 -m unittest discover -s runner/tests -p 'test_*.py'

runner-security-test:
	@test -f runner/.env || (echo "Copy runner/.env.example to runner/.env first." && exit 1)
	set -a; . ./runner/.env; set +a; python3 runner/tests/sandbox_security_check.py
