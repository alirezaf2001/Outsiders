package automated_tests;

import db.ConnectionManager;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

final class AutomatedTestSupport {
    static final class TestReport {
        private static final String RESULTS_CSV_ENV = "OUTSIDERS_RESULTS_CSV";
        private static final String SUMMARY_CSV_ENV = "OUTSIDERS_SUMMARY_CSV";
        private static final String RUN_ID_ENV = "OUTSIDERS_RUN_ID";

        private final String suiteName;
        private final List<ResultRow> rows = new ArrayList<>();
        private int passed;
        private int failed;

        TestReport(String suiteName) {
            this.suiteName = suiteName;
        }

        void check(String label, boolean condition, String failDetail) {
            if (condition) {
                System.out.println("[PASS] " + label);
                rows.add(ResultRow.pass(runId(), suiteName, label));
                passed++;
            } else {
                System.out.println("[FAIL] " + label);
                System.out.println("       " + failDetail);
                rows.add(ResultRow.fail(runId(), suiteName, label, failDetail));
                failed++;
            }
        }

        void info(String message) {
            System.out.println("[INFO] " + message);
            rows.add(ResultRow.info(runId(), suiteName, "Info", message));
        }

        void unexpected(String label, Throwable error) {
            System.out.println("[FAIL] " + label);
            error.printStackTrace(System.out);
            rows.add(ResultRow.fail(
                    runId(),
                    suiteName,
                    label,
                    error.getClass().getSimpleName() + ": " + String.valueOf(error.getMessage())));
            failed++;
        }

        void finish() {
            writeResultsIfConfigured();
            System.out.println();
            System.out.println("--- " + suiteName + ": " + passed + "/" + (passed + failed) + " passed ---");
            System.exit(failed > 0 ? 1 : 0);
        }

        private void writeResultsIfConfigured() {
            String resultsCsv = System.getenv(RESULTS_CSV_ENV);
            String summaryCsv = System.getenv(SUMMARY_CSV_ENV);

            if (resultsCsv != null && !resultsCsv.isBlank()) {
                appendDetailRows(Paths.get(resultsCsv));
            }

            if (summaryCsv != null && !summaryCsv.isBlank()) {
                appendSummaryRow(Paths.get(summaryCsv));
            }
        }

        private void appendDetailRows(Path path) {
            StringBuilder builder = new StringBuilder();

            for (ResultRow row : rows) {
                builder.append(csv(row.runId))
                        .append(',')
                        .append(csv(row.executedAt))
                        .append(',')
                        .append(csv(row.suiteName))
                        .append(',')
                        .append(csv(row.checkLabel))
                        .append(',')
                        .append(csv(row.status))
                        .append(',')
                        .append(csv(row.detail))
                        .append('\n');
            }

            appendToFile(path, builder.toString());
        }

        private void appendSummaryRow(Path path) {
            String overallStatus = failed > 0 ? "FAIL" : "PASS";
            String line = csv(runId()) + ','
                    + csv(timestampNow()) + ','
                    + csv(suiteName) + ','
                    + passed + ','
                    + failed + ','
                    + csv(overallStatus) + '\n';
            appendToFile(path, line);
        }

        private void appendToFile(Path path, String text) {
            try {
                Files.writeString(
                        path,
                        text,
                        StandardCharsets.UTF_8,
                        java.nio.file.StandardOpenOption.CREATE,
                        java.nio.file.StandardOpenOption.APPEND);
            } catch (IOException e) {
                System.out.println("[WARN] Could not write automated test results to " + path + ": " + e.getMessage());
            }
        }

        private String runId() {
            String runId = System.getenv(RUN_ID_ENV);
            if (runId == null || runId.isBlank()) {
                return timestampNow();
            }

            return runId;
        }

        private String timestampNow() {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }

        private String csv(String value) {
            if (value == null) {
                return "\"\"";
            }

            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
    }

    private static final class ResultRow {
        private final String runId;
        private final String executedAt;
        private final String suiteName;
        private final String checkLabel;
        private final String status;
        private final String detail;

        private ResultRow(String runId, String executedAt, String suiteName, String checkLabel, String status, String detail) {
            this.runId = runId;
            this.executedAt = executedAt;
            this.suiteName = suiteName;
            this.checkLabel = checkLabel;
            this.status = status;
            this.detail = detail;
        }

        private static ResultRow pass(String runId, String suiteName, String checkLabel) {
            return new ResultRow(runId, now(), suiteName, checkLabel, "PASS", "");
        }

        private static ResultRow fail(String runId, String suiteName, String checkLabel, String detail) {
            return new ResultRow(runId, now(), suiteName, checkLabel, "FAIL", detail);
        }

        private static ResultRow info(String runId, String suiteName, String checkLabel, String detail) {
            return new ResultRow(runId, now(), suiteName, checkLabel, "INFO", detail);
        }

        private static String now() {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
    }

    static final class MonthYear {
        private final int month;
        private final int year;

        MonthYear(int month, int year) {
            this.month = month;
            this.year = year;
        }

        int getMonth() {
            return month;
        }

        int getYear() {
            return year;
        }
    }

    static final class DateRange {
        private final String startDate;
        private final String endDate;

        DateRange(String startDate, String endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }

        String getStartDate() {
            return startDate;
        }

        String getEndDate() {
            return endDate;
        }
    }

    private AutomatedTestSupport() {
    }

    static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static Connection requireConnection() throws SQLException {
        Connection connection = new ConnectionManager().getConnection();
        if (connection == null) {
            throw new SQLException("ConnectionManager returned null. Check DatabaseConfig and MySQL.");
        }
        return connection;
    }

    static Integer findAnyEmployeeId() throws SQLException {
        return querySingleInt("SELECT empid AS value FROM employees ORDER BY empid LIMIT 1");
    }

    static Integer findAnyAddressId() throws SQLException {
        return querySingleInt("SELECT addressID AS value FROM addresses ORDER BY addressID LIMIT 1");
    }

    static Integer findAnyDivisionId() throws SQLException {
        return querySingleInt("SELECT divID AS value FROM division ORDER BY divID LIMIT 1");
    }

    static Integer findAnyPayrollEmployeeId() throws SQLException {
        return querySingleInt("SELECT empid AS value FROM payroll ORDER BY pay_date DESC, payID DESC LIMIT 1");
    }

    static MonthYear findAnyPayrollMonthYear() throws SQLException {
        String sql = """
                SELECT MONTH(pay_date) AS monthValue, YEAR(pay_date) AS yearValue
                FROM payroll
                ORDER BY pay_date DESC, payID DESC
                LIMIT 1
                """;

        try (Connection connection = requireConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return new MonthYear(
                        resultSet.getInt("monthValue"),
                        resultSet.getInt("yearValue"));
            }
        }

        return null;
    }

    static DateRange findEmployeeHireDateRange() throws SQLException {
        String sql = """
                SELECT MIN(HireDate) AS startDate, MAX(HireDate) AS endDate
                FROM employees
                """;

        try (Connection connection = requireConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next() && resultSet.getDate("startDate") != null && resultSet.getDate("endDate") != null) {
                return new DateRange(
                        resultSet.getDate("startDate").toString(),
                        resultSet.getDate("endDate").toString());
            }
        }

        return null;
    }

    private static Integer querySingleInt(String sql) throws SQLException {
        try (Connection connection = requireConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("value");
            }
        }

        return null;
    }
}
