FROM docker.io/library/eclipse-temurin:21-jdk-jammy@sha256:658cc18c0d655599347261989aab3b9e8e8a43fecb4ab8a987d13422d21a9009

RUN groupadd --gid 10001 sandbox \
    && useradd --uid 10001 --gid 10001 --no-create-home --shell /usr/sbin/nologin sandbox
COPY --chmod=0555 runner/sandbox/run.sh /opt/preppilot/run.sh
USER 10001:10001
ENTRYPOINT ["/opt/preppilot/run.sh"]
