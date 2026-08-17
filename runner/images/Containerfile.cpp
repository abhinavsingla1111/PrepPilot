FROM docker.io/library/gcc:14-bookworm@sha256:66035d353338cb93b64f621393dc6fecde85258651ca454f0cf36ff2639b1352

RUN groupadd --gid 10001 sandbox \
    && useradd --uid 10001 --gid 10001 --no-create-home --shell /usr/sbin/nologin sandbox
COPY --chmod=0555 runner/sandbox/run.sh /opt/preppilot/run.sh
USER 10001:10001
ENTRYPOINT ["/opt/preppilot/run.sh"]
