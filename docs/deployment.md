# Deploying PrepPilot on AWS

This guide covers installation and operation of PrepPilot on Ubuntu EC2 with PostgreSQL on Amazon RDS. Docker Compose runs the frontend, backend, and HTTPS proxy. It is intended for a single-server deployment with a new application database.

## Architecture

```text
GitHub ── source code ──► EC2 running Ubuntu

Browser ── DNS lookup ──► DuckDNS returns the EC2 Elastic IP
Browser ── HTTPS ───────► Caddy on EC2
                          ├── /api/* ──► Spring Boot API ── TLS ──► RDS
                          └── /* ──────► Nginx ──► React application

Spring Boot API ── HTTPS ──► Brevo ──► OTP and feedback emails
```

| Component | Responsibility |
|---|---|
| **Amazon EC2** | A virtual server running the application. EC2 stands for Elastic Compute Cloud; an individual server is an instance. |
| **Ubuntu 24.04** | The server's Linux operating system. `apt` installs packages; `sudo` runs a command with administrator privileges. |
| **EBS** | Persistent disk storage for Ubuntu, source code, container images, and logs. |
| **Elastic IP** | A fixed public IPv4 address assigned to EC2. DNS points the website name to this address. |
| **VPC and security groups** | The private AWS network and firewall rules controlling access to EC2 and RDS. |
| **Amazon RDS** | Managed PostgreSQL storage for users, sessions, progress, assessments, and interview records. |
| **Docker Engine and Compose** | Build and run the application containers. An image is a packaged application; a container is a running instance of that image. |
| **Nginx** | Serves the compiled React files from the `web` container. |
| **Caddy** | Terminates HTTPS and forwards requests to the frontend or backend. |
| **DuckDNS** | Provides a free subdomain and DNS records. Application hosting remains on EC2. |
| **Brevo** | Delivers transactional emails through its HTTPS API. |

A **reverse proxy** receives public requests and forwards them to internal services. Caddy performs this role, giving the frontend and API a shared HTTPS address. The database is accessible through the private network, not through the public website.

Docker Engine installed directly on Ubuntu is open source and requires no Docker subscription for this setup. Administration uses the Docker **command-line interface (CLI)** and Compose plugin. AWS compute, database, disk, public IPv4, and applicable transfer charges still apply. [Docker Engine documentation](https://docs.docker.com/engine/).

## Before installation

Prepare the following:

- An AWS account with MFA and a separate identity for routine administration.
- An Ubuntu **24.04 x86_64** EC2 instance and RDS PostgreSQL **17**, in the same region and VPC. The examples use Mumbai, `ap-south-1`.
- An EC2 key pair downloaded to the administrator's laptop.
- Access to the PrepPilot GitHub repository.
- A Brevo API key and verified sender address.
- A domain or available DuckDNS subdomain.

A `t3.small` has 2 GiB RAM and can support a small installation. Building on the server requires additional memory and disk headroom; use a larger instance or build elsewhere if builds exhaust resources. A 10 GiB root disk leaves limited space for build caches and logs. Monitor free space before every release.

Protect the root account with MFA. For an IAM console user, register MFA separately and grant the permissions required for administration; `AdministratorAccess` gives broad control. IAM Identity Center is another access option, but enabling AWS Organizations can affect free-tier credits. Review the [AWS account-access guidance](https://docs.aws.amazon.com/singlesignon/latest/userguide/enable-identity-center.html) before enabling it.

### Values used in this guide

| Placeholder | Value to supply |
|---|---|
| `YOUR_ELASTIC_IP` | EC2's allocated Elastic IP address |
| `YOUR_KEY.pem` | EC2 private key file on the laptop |
| `YOUR_GITHUB_USERNAME/YOUR_REPOSITORY` | GitHub repository owner and name |
| `YOUR_RDS_ENDPOINT` | Full RDS hostname, without a protocol or port |
| `YOUR_MASTER_USERNAME` | RDS master username |
| `YOUR_DOMAIN` | Domain under your control, such as `your-project.duckdns.org` |

Replace placeholders before running commands or saving configuration. Unless marked **Laptop** or **PostgreSQL**, commands run in the EC2 terminal. In Nano, save with **Ctrl+O**, press **Enter**, and exit with **Ctrl+X**.

## 1. Configure AWS networking

Place EC2 in a public subnet with internet access. Allocate an Elastic IP in the same region and associate it with the instance. Keep RDS **Publicly accessible: No**. Its DB subnet group must cover at least two Availability Zones, including for a Single-AZ instance. [RDS network configuration](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_VPC.WorkingWithRDSInstanceinaVPC.html).

Configure inbound security-group rules:

| Resource | Port | Source |
|---|---|---|
| EC2 | TCP 22 — SSH | Administrator's public IP, using a `/32` range |
| EC2 | TCP 80 — HTTP | `0.0.0.0/0` |
| EC2 | TCP 443 — HTTPS | `0.0.0.0/0` |
| RDS | TCP 5432 — PostgreSQL | EC2's security group ID |

Leave application ports 8080 and 8081 closed publicly. EC2 needs outbound connectivity for package downloads, GitHub, container images, email delivery, and certificate issuance, plus access to RDS on 5432.

Enable EBS and RDS encryption, RDS deletion protection, and automatic backups with a retention period appropriate for the application. Set an AWS budget alert before accepting public traffic.

## 2. Connect to EC2

**Laptop:**

```bash
chmod 400 ~/Downloads/YOUR_KEY.pem
ssh -i ~/Downloads/YOUR_KEY.pem ubuntu@YOUR_ELASTIC_IP
```

Verify the server fingerprint when connecting for the first time. A prompt beginning with `ubuntu@` indicates that subsequent commands run on EC2.

The EC2 `.pem` file contains a private SSH key. It authenticates access to Ubuntu and must remain private. AWS console credentials and database passwords serve separate purposes. [AWS SSH instructions](https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/connect-to-linux-instance.html).

## 3. Install Docker and supporting tools

On a fresh Ubuntu 24.04 instance:

```bash
sudo apt update
sudo apt install -y ca-certificates curl git postgresql-client
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
```

Add Docker's package repository:

```bash
sudo tee /etc/apt/sources.list.d/docker.sources > /dev/null <<EOF
Types: deb
URIs: https://download.docker.com/linux/ubuntu
Suites: noble
Components: stable
Architectures: $(dpkg --print-architecture)
Signed-By: /etc/apt/keyrings/docker.asc
EOF

sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo systemctl enable --now docker
sudo docker compose version
```

The last command should report the installed Compose version. For an existing Docker installation, follow the vendor's upgrade procedure instead. [Docker installation reference](https://docs.docker.com/engine/install/ubuntu/).

## 4. Download the repository

Create a dedicated GitHub deploy key on EC2:

```bash
ssh-keygen -t ed25519 -f ~/.ssh/preppilot_github -C "PrepPilot deployment"
cat ~/.ssh/preppilot_github.pub
```

For unattended repository downloads, leave the passphrase empty and restrict this key to the repository. Do not overwrite an existing key without checking its use.

In GitHub, open **Repository → Settings → Deploy keys → Add deploy key**. Paste the `.pub` file contents and leave **Allow write access** unchecked. The private key stays on EC2. [Deploy-key documentation](https://docs.github.com/en/authentication/connecting-to-github-with-ssh/managing-deploy-keys).

```bash
cd ~
GIT_SSH_COMMAND="ssh -i $HOME/.ssh/preppilot_github -o IdentitiesOnly=yes" git clone git@github.com:YOUR_GITHUB_USERNAME/YOUR_REPOSITORY.git PrepPilot
cd ~/PrepPilot
git config core.sshCommand "ssh -i $HOME/.ssh/preppilot_github -o IdentitiesOnly=yes"
```

Compare any first-connection fingerprint with [GitHub's published fingerprints](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/githubs-ssh-key-fingerprints). If `~/PrepPilot` already exists, check `git -C ~/PrepPilot remote -v` before reusing it.

## 5. Create the application database

Download the RDS trust certificate:

```bash
cd ~/PrepPilot
mkdir -p certs
curl -fsSL https://truststore.pki.rds.amazonaws.com/ap-south-1/ap-south-1-bundle.pem -o certs/rds-ca.pem
chmod 644 certs/rds-ca.pem
```

This `.pem` file contains public certificate authorities used to verify RDS. It is not a private login key. For another region, download the corresponding [AWS certificate bundle](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/UsingWithRDS.SSL.html).

Connect with the RDS master credentials:

```bash
PGSSLMODE=verify-full PGSSLROOTCERT="$PWD/certs/rds-ca.pem" \
psql -h YOUR_RDS_ENDPOINT -U YOUR_MASTER_USERNAME -d postgres -W
```

For AWS-managed credentials, retrieve the password through **RDS → Configuration → Manage in Secrets Manager**. Otherwise use the password set during RDS creation.

**PostgreSQL:** run these commands once for a new installation. Enter a strong application password at the prompt and save it privately.

```sql
CREATE ROLE preppilot_app LOGIN;
\password preppilot_app
GRANT preppilot_app TO CURRENT_USER WITH SET TRUE;
CREATE DATABASE preppilot OWNER preppilot_app;
GRANT preppilot_app TO CURRENT_USER WITH SET FALSE;
\q
```

The temporary role permission allows the master login to assign database ownership. `preppilot_app` owns the application's database because the backend runs Flyway migrations at startup. If either the role or database already exists, verify its ownership and intended use before changing it.

Test the application credentials:

```bash
PGSSLMODE=verify-full PGSSLROOTCERT="$PWD/certs/rds-ca.pem" \
psql -h YOUR_RDS_ENDPOINT -U preppilot_app -d preppilot -W
```

A `preppilot=>` prompt confirms access. Exit with `\q`. Tables and initial content are created by the backend during its first successful startup.

## 6. Configure DNS and email

For a free subdomain, sign in to [DuckDNS](https://www.duckdns.org/), register an available name, and set its current IPv4 address to the EC2 Elastic IP. For an existing domain, create an A record pointing to the same address.

Check resolution before starting Caddy:

```bash
getent ahostsv4 YOUR_DOMAIN
```

The returned address should match the Elastic IP. DNS provides the address lookup; EC2 serves the application.

In Brevo, create a transactional API key and verify the sending identity. The backend uses Brevo's HTTPS API to deliver OTP and feedback emails. An API key authorizes delivery; PrepPilot generates and verifies the OTP itself. [Brevo email documentation](https://developers.brevo.com/docs/send-a-transactional-email).

## 7. Configure the backend

Generate an authentication secret:

```bash
openssl rand -hex 32
```

Create or edit the environment file without overwriting existing secrets:

```bash
cd ~/PrepPilot
touch .env
chmod 600 .env
nano .env
```

Use the following configuration, replacing every placeholder:

```dotenv
DATABASE_URL='jdbc:postgresql://YOUR_RDS_ENDPOINT:5432/preppilot?sslmode=verify-full&sslrootcert=/app/certs/rds-ca.pem'
DATABASE_USERNAME=preppilot_app
DATABASE_PASSWORD='YOUR_APPLICATION_DATABASE_PASSWORD'

AUTH_TOKEN_PEPPER='YOUR_GENERATED_AUTHENTICATION_SECRET'
AUTH_COOKIE_SECURE=true
AUTH_COOKIE_SAME_SITE=Strict
EXPOSE_DEV_OTP=false

ALLOWED_ORIGINS=https://YOUR_DOMAIN
APP_PUBLIC_URL=https://YOUR_DOMAIN
FORWARD_HEADERS_STRATEGY=FRAMEWORK

MAIL_ENABLED=true
MAIL_PROVIDER=brevo
BREVO_API_KEY='YOUR_BREVO_API_KEY'
MAIL_FROM='PrepPilot <YOUR_VERIFIED_SENDER_EMAIL>'
FEEDBACK_RECIPIENT_EMAIL=YOUR_FEEDBACK_EMAIL

RUNNER_ENABLED=false
```

Use unique values for each secret and keep `.env` out of version control. The root file is already covered by the repository's `.gitignore`.

| Setting | Purpose |
|---|---|
| `DATABASE_URL` | Identifies the RDS host, port, and database. `verify-full` enables TLS with server-identity verification. The certificate path is inside the API container. |
| `AUTH_TOKEN_PEPPER` | Server secret used when hashing session tokens and related identifiers. Changing it invalidates existing sessions. |
| `AUTH_COOKIE_SECURE` | Requires HTTPS when the browser sends the session cookie. |
| `ALLOWED_ORIGINS` | Browser origins permitted by the API's CORS configuration. An origin consists of protocol, hostname, and port; it has no path. |
| `APP_PUBLIC_URL` | Base address used in application-generated links. It does not configure DNS or publish the website. |
| `FORWARD_HEADERS_STRATEGY` | Allows Spring to use request information forwarded by Caddy. The backend must remain behind the trusted proxy. |
| `RUNNER_ENABLED` | Controls the submitted-code execution queue. Enable only after deploying a separate worker. |

CORS rules are browser controls, not a substitute for authentication or firewall rules. Keep `VITE_API_URL` empty when building the frontend so API requests use the same public domain. Frontend `VITE_` variables are public and must never contain secrets.

## 8. Define the containers and HTTPS routing

Create `compose.aws.yml` in the repository root:

```yaml
services:
  api:
    build:
      context: .
      dockerfile: Dockerfile.api
    env_file:
      - .env
    environment:
      PORT: "8080"
    volumes:
      - ./certs/rds-ca.pem:/app/certs/rds-ca.pem:ro
    ports:
      - "127.0.0.1:8080:8080"
    mem_limit: 1g
    restart: unless-stopped
    logging:
      driver: json-file
      options:
        max-size: "10m"
        max-file: "3"

  web:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "127.0.0.1:8081:80"
    depends_on:
      - api
    restart: unless-stopped
    logging:
      driver: json-file
      options:
        max-size: "10m"
        max-file: "3"

  caddy:
    image: caddy:2
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./Caddyfile:/etc/caddy/Caddyfile:ro
      - caddy_data:/data
      - caddy_config:/config
    depends_on:
      - api
      - web
    restart: unless-stopped
    logging:
      driver: json-file
      options:
        max-size: "10m"
        max-file: "3"

volumes:
  caddy_data:
  caddy_config:
```

This file uses RDS. The repository's `docker-compose.yml` starts a local PostgreSQL service and is intended for the local stack. Use `-f compose.aws.yml` for the commands in this guide.

Create `Caddyfile` beside the Compose file and replace `YOUR_DOMAIN`:

```caddyfile
YOUR_DOMAIN {
    handle /api/* {
        reverse_proxy api:8080
    }

    handle /actuator/* {
        respond "Not found" 404
    }

    handle {
        reverse_proxy web:80
    }
}
```

Caddy obtains and renews HTTPS certificates automatically when DNS and ports 80/443 are reachable. The names `api` and `web` resolve on Docker's internal network. Certificate state persists in `caddy_data` and `caddy_config`. [Caddy HTTPS documentation](https://caddyserver.com/docs/automatic-https).

Commit `compose.aws.yml` and `Caddyfile` after review. Keep passwords and private keys out of the repository. Maintain the public RDS trust bundle from AWS rather than substituting a private certificate or disabling verification.

## 9. Build, start, and verify

Run each command after the previous one succeeds:

```bash
cd ~/PrepPilot
sudo docker compose -f compose.aws.yml config --quiet
sudo docker compose -f compose.aws.yml build api
sudo docker compose -f compose.aws.yml build web
sudo docker compose -f compose.aws.yml up -d
```

The quiet configuration check avoids printing resolved credentials. Sequential builds reduce memory pressure. `up -d` starts services in the background; closing the SSH session leaves them running.

Allow the backend to finish migrations and initial data loading, then check:

```bash
sudo docker compose -f compose.aws.yml ps
curl http://127.0.0.1:8080/actuator/health
```

All services should remain running, and health should report `UP`. Open `https://YOUR_DOMAIN` and verify:

- The browser accepts the HTTPS certificate.
- OTP login succeeds.
- A saved problem retains its status after refresh.
- Fundamentals load, and an assessment can be completed and reviewed.
- An interview session can be started and resumed.

Submitted-code execution requires a separate rootless Podman worker. See [Code execution](docs/CODE_EXECUTION.md) for its requirements. Keep `RUNNER_ENABLED=false` until that worker is configured and tested.

## Private access from a laptop

### SSH terminal and port forwarding

A normal SSH session opens a terminal on EC2. A tunnel forwards a laptop port to a service accessible from EC2:

```bash
# Laptop
ssh -i ~/Downloads/YOUR_KEY.pem -N -L 8081:127.0.0.1:8081 ubuntu@YOUR_ELASTIC_IP
```

While this command runs, `http://localhost:8081` reaches the frontend container through encrypted SSH. `-N` starts no remote shell; `-L` specifies port forwarding. The application still runs on EC2.

Use the public HTTPS address for normal login. The production origin and secure-cookie settings are configured for that address, not for localhost. The tunnel is useful for private diagnostics and frontend inspection.

### Database clients

DBeaver and similar clients must reach private RDS through EC2. Configure the database host as the RDS endpoint, port `5432`, database `preppilot`, and user `preppilot_app`. In the SSH settings, use the EC2 Elastic IP, user `ubuntu`, port `22`, and EC2 private key.

Configure PostgreSQL TLS using the AWS CA bundle downloaded to the laptop. The API container's `/app/certs/rds-ca.pem` path is not available there. When using hostname verification, ensure the client validates the RDS hostname rather than the tunnel's local address. Keep the database private while troubleshooting. [DBeaver SSH configuration](https://dbeaver.com/docs/dbeaver/SSH-Configuration/).

## Updates and maintenance

Before an application release, record the deployed Git revision and take an RDS snapshot. Then run from `~/PrepPilot`:

```bash
git rev-parse HEAD
git pull --ff-only
sudo docker compose -f compose.aws.yml config --quiet
sudo docker compose -f compose.aws.yml build api
sudo docker compose -f compose.aws.yml build web
sudo docker compose -f compose.aws.yml up -d --force-recreate
```

Verify health, login, and saved data after each release. Container recreation briefly interrupts service. Changes to `.env` require recreation; an ordinary restart retains the old environment. Update container base images and the Caddy image deliberately, with the same verification checks.

A code rollback must account for database migrations. An older application may not support a newer schema. Preserve the previous revision and snapshot; restore a snapshot to a separate RDS instance when recovery is necessary, then verify it before switching the application endpoint.

Monitor the server:

```bash
sudo docker compose -f compose.aws.yml logs --tail=50 api
sudo docker compose -f compose.aws.yml logs --tail=50 caddy
df -h /
sudo docker system df
```

Review unused build caches and images before removing them. Keep the Caddy volumes when replacing containers. Back up `.env` and required access keys in a secure location separate from GitHub. Enable RDS automated backups and periodically test restoration. [RDS backup documentation](https://docs.aws.amazon.com/AmazonRDS/latest/UserGuide/USER_WorkingWithAutomatedBackups.html).

Budget alerts notify administrators; they do not cap spending. Check for unused EC2 instances, EBS volumes, RDS instances, snapshots, and allocated Elastic IPs across regions.

## Troubleshooting

| Symptom | Check |
|---|---|
| SSH times out | Current Elastic IP, instance status, internet route, and port 22 rule for the laptop's current public IP. |
| Repository directory already exists | Inspect its contents and Git remote before cloning again. |
| RDS connection times out | Matching VPCs, RDS endpoint, and port 5432 access from the EC2 security group. |
| `must be able to SET ROLE` | Apply the temporary role grant in the database-creation step. |
| Database password rejected | Application connections use `preppilot_app` and its password, not the RDS master credentials. |
| Certificate file not found | Check the downloaded file, container mount, and path used by the client. |
| Caddy keeps restarting | Inspect Caddy logs. Each opening `{` must be on the same line as its directive. |
| Public website cannot be reached | DNS must resolve to the Elastic IP; ports 80/443 must be open; Caddy must be running. |
| OTP email fails | Brevo API key, verified sender, delivery limits, backend logs, and spam folder. |
| Login fails only on the public domain | Exact allowed origin, HTTPS secure-cookie setting, proxy headers, and recreated containers. |
| Build is killed or disk fills | Available RAM, disk space, and Docker cache usage. Use more resources or build images on another machine. |
