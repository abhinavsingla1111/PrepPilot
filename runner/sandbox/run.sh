#!/bin/sh
set -eu

case "${PREPPILOT_LANGUAGE:-}" in
    java|cpp|python) ;;
    *) echo "COMPILE\tCOMPILE_ERROR\tVW5zdXBwb3J0ZWQgbGFuZ3VhZ2Uu"; exit 0 ;;
esac

case "${COMPILE_TIMEOUT_SECONDS:-}" in
    ''|*[!0-9]*) exit 70 ;;
esac
case "${TEST_TIMEOUT_SECONDS:-}" in
    ''|*[!0-9]*) exit 70 ;;
esac
case "${MAX_OUTPUT_BYTES:-}" in
    ''|*[!0-9]*) exit 70 ;;
esac

mkdir -p /tmp/job
ulimit -f 256

encode_file() {
    head -c "$MAX_OUTPUT_BYTES" "$1" | base64 -w 0
}

compile_status=0
case "$PREPPILOT_LANGUAGE" in
    java)
        cp /input/source /tmp/job/Main.java
        timeout -k 1s "${COMPILE_TIMEOUT_SECONDS}s" \
            javac -encoding UTF-8 -proc:none -d /tmp/job /tmp/job/Main.java \
            >/dev/null 2>/tmp/job/compile.err || compile_status=$?
        ;;
    cpp)
        cp /input/source /tmp/job/Main.cpp
        timeout -k 1s "${COMPILE_TIMEOUT_SECONDS}s" \
            g++ -std=c++20 -O2 -pipe -fstack-protector-strong -D_FORTIFY_SOURCE=2 \
            -Wl,-z,relro,-z,now,-z,noexecstack -o /tmp/job/main /tmp/job/Main.cpp \
            >/dev/null 2>/tmp/job/compile.err || compile_status=$?
        ;;
    python)
        cp /input/source /tmp/job/main.py
        timeout -k 1s "${COMPILE_TIMEOUT_SECONDS}s" \
            python3 -I -m py_compile /tmp/job/main.py \
            >/dev/null 2>/tmp/job/compile.err || compile_status=$?
        ;;
esac

if [ "$compile_status" -ne 0 ]; then
    if [ "$compile_status" -eq 124 ]; then
        printf 'Compilation timed out.\n' >/tmp/job/compile.err
    fi
    printf 'COMPILE\tCOMPILE_ERROR\t'
    encode_file /tmp/job/compile.err
    printf '\n'
    exit 0
fi
printf 'COMPILE\tOK\t\n'

for input_file in /input/tests/*.in; do
    position=$(basename "$input_file" .in)
    position=$(printf '%s' "$position" | sed 's/^0*//')
    [ -n "$position" ] || position=0
    : >/tmp/job/actual.out
    : >/tmp/job/runtime.err
    started=$(date +%s%N)
    run_status=0
    case "$PREPPILOT_LANGUAGE" in
        java)
            timeout -k 1s "${TEST_TIMEOUT_SECONDS}s" \
                java -Xms16m -Xmx160m -XX:ActiveProcessorCount=1 -XX:UseSVE=0 -cp /tmp/job Main \
                <"$input_file" >/tmp/job/actual.out 2>/tmp/job/runtime.err || run_status=$?
            ;;
        cpp)
            timeout -k 1s "${TEST_TIMEOUT_SECONDS}s" \
                /tmp/job/main <"$input_file" >/tmp/job/actual.out 2>/tmp/job/runtime.err || run_status=$?
            ;;
        python)
            timeout -k 1s "${TEST_TIMEOUT_SECONDS}s" \
                python3 -I /tmp/job/main.py <"$input_file" >/tmp/job/actual.out 2>/tmp/job/runtime.err || run_status=$?
            ;;
    esac
    finished=$(date +%s%N)
    elapsed_ms=$(((finished - started) / 1000000))
    case "$run_status" in
        0) case_status=EXECUTED ;;
        124) case_status=TIME_LIMIT ;;
        *) case_status=RUNTIME_ERROR ;;
    esac
    printf 'CASE\t%s\t%s\t%s\t' "$position" "$case_status" "$elapsed_ms"
    encode_file /tmp/job/actual.out
    printf '\t'
    encode_file /tmp/job/runtime.err
    printf '\t\n'
done
