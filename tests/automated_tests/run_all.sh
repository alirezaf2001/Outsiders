#!/usr/bin/env bash

set -u
set -o pipefail

ROOT="$(cd "$(dirname "$0")/../.." && pwd)"
MYSQL_JAR="$ROOT/lib/mysql-connector-j-9.6.0.jar"
APP_OUT="$ROOT/out/automated_app"
TEST_OUT="$ROOT/out/automated_tests"
TEST_SRC="$ROOT/tests/automated_tests/java"
RESULTS_DIR="$ROOT/tests/automated_tests/results"
RUN_ID="$(date '+%Y-%m-%d %H:%M:%S')"
RUN_STAMP="$(date '+%Y%m%d_%H%M%S')"
DETAIL_RESULTS_CSV="$RESULTS_DIR/automated_test_results_$RUN_STAMP.csv"
SUITE_SUMMARY_CSV="$RESULTS_DIR/automated_test_suite_summary_$RUN_STAMP.csv"
LATEST_DETAIL_CSV="$RESULTS_DIR/automated_test_results_latest.csv"
LATEST_SUMMARY_CSV="$RESULTS_DIR/automated_test_suite_summary_latest.csv"

if [ ! -f "$MYSQL_JAR" ]; then
    echo "Missing MySQL JDBC jar: $MYSQL_JAR"
    exit 1
fi

APP_SOURCES=$(find "$ROOT/src" -name '*.java' | sort)
TEST_SOURCES=$(find "$TEST_SRC" -name '*.java' | sort)

if [ -z "$APP_SOURCES" ] || [ -z "$TEST_SOURCES" ]; then
    echo "Application sources or automated test sources were not found."
    exit 1
fi

rm -rf "$APP_OUT" "$TEST_OUT"
mkdir -p "$APP_OUT" "$TEST_OUT"
mkdir -p "$RESULTS_DIR"

printf 'Run ID,Executed At,Suite,Check,Status,Detail\n' > "$DETAIL_RESULTS_CSV"
printf 'Run ID,Executed At,Suite,Passed,Failed,Overall Status\n' > "$SUITE_SUMMARY_CSV"

export OUTSIDERS_RUN_ID="$RUN_ID"
export OUTSIDERS_RESULTS_CSV="$DETAIL_RESULTS_CSV"
export OUTSIDERS_SUMMARY_CSV="$SUITE_SUMMARY_CSV"

echo "Compiling application sources..."
if ! javac -cp "$MYSQL_JAR" -d "$APP_OUT" $APP_SOURCES; then
    echo "Application compilation failed."
    exit 1
fi

echo "Compiling automated test sources..."
if ! javac -cp "$APP_OUT:$MYSQL_JAR" -d "$TEST_OUT" $TEST_SOURCES; then
    echo "Automated test compilation failed."
    exit 1
fi

UI_TESTS=(
    automated_tests.MainLaunchSmokeTest
    automated_tests.LoginPanelSmokeTest
    automated_tests.AddEmployeePanelValidationTest
    automated_tests.SalaryUpdatePanelValidationTest
    automated_tests.EmployeeSearchPanelValidationTest
    automated_tests.ReportsPanelValidationTest
)

DB_TESTS=(
    automated_tests.DatabaseReadSmokeTest
    automated_tests.DatabaseEmployeeRoundTripTest
)

FAILURES=0

run_test() {
    local class_name="$1"

    echo
    echo "== $class_name =="

    if ! java -cp "$APP_OUT:$TEST_OUT:$MYSQL_JAR" "$class_name"; then
        FAILURES=$((FAILURES + 1))
    fi
}

for class_name in "${UI_TESTS[@]}"; do
    run_test "$class_name"
done

if [ "${OUTSIDERS_SKIP_DB:-0}" = "1" ]; then
    echo
    echo "Skipping DB smoke tests because OUTSIDERS_SKIP_DB=1."
else
    for class_name in "${DB_TESTS[@]}"; do
        run_test "$class_name"
    done
fi

echo
if [ "$FAILURES" -eq 0 ]; then
    echo "All automated test suites passed."
else
    echo "$FAILURES automated test suite(s) failed."
fi

cp "$DETAIL_RESULTS_CSV" "$LATEST_DETAIL_CSV"
cp "$SUITE_SUMMARY_CSV" "$LATEST_SUMMARY_CSV"

echo "Detailed results: $DETAIL_RESULTS_CSV"
echo "Suite summary: $SUITE_SUMMARY_CSV"

exit "$FAILURES"
