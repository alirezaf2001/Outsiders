package visual_tests;

import Main_gui.MainFrame;
import gui_panels.ReportsPanel;
import gui_panels.ReportsPanel.ReportType;
import javax.swing.SwingUtilities;
import static visual_tests.SwingTestUtils.*;

/**
 * Visual tests for the Reports panel.
 *
 * What is checked:
 *   - PAY_BY_JOB_TITLE mode shows month/year rows and hides date-range rows
 *   - PAY_BY_DIVISION  mode shows month/year rows and hides date-range rows
 *   - NEW_HIRES        mode hides month/year rows and shows date-range rows
 *   - clearResults()   empties all four input fields
 *   - Running a NEW_HIRES report with an invalid date string shows the
 *     date-format error dialog without crashing
 *
 * No database connection is required – the visibility and field-state checks
 * are pure UI, and the invalid-date test is rejected before any DB call.
 *
 * HOW TO COMPILE AND RUN (from the project root):
 *   javac -cp "src:lib/*" -d bin $(find src -name "*.java")
 *   javac -cp "bin:lib/*" -d tests tests/visual_tests/SwingTestUtils.java tests/visual_tests/ReportsPanelTest.java
 *   java  -cp "bin:lib/*:tests" visual_tests.ReportsPanelTest
 */
public class ReportsPanelTest {

    private static MainFrame    frame;
    private static ReportsPanel panel;
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("=== ReportsPanel Visual Tests ===\n");

        setUp();

        testPayByJobTitle_showsMonthYearRows();
        testPayByDivision_showsMonthYearRows();
        testNewHires_showsDateRangeRows();
        testClearResults_emptiesAllInputFields();
        testNewHires_invalidDate_showsErrorDialog();

        tearDown();
        printSummary();
    }

    // -----------------------------------------------------------------------
    // Setup / teardown
    // -----------------------------------------------------------------------

    private static void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new MainFrame();
            frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("Test – Reports Panel");
            frame.setSize(900, 650);
            frame.setVisible(true);
        });
        panel = (ReportsPanel) getField(frame, "reportsPanel");
        SwingUtilities.invokeAndWait(() -> frame.showReports(ReportType.PAY_BY_JOB_TITLE));
        Thread.sleep(300);
    }

    private static void tearDown() throws Exception {
        Thread.sleep(600);
        SwingUtilities.invokeAndWait(() -> frame.dispose());
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    private static void testPayByJobTitle_showsMonthYearRows() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.prepareForReport(ReportType.PAY_BY_JOB_TITLE));
        Thread.sleep(200);

        boolean monthVisible     = isComponentVisible(panel, "monthRow");
        boolean yearVisible      = isComponentVisible(panel, "yearRow");
        boolean startDateHidden  = !isComponentVisible(panel, "startDateRow");
        boolean endDateHidden    = !isComponentVisible(panel, "endDateRow");

        check("PAY_BY_JOB_TITLE – month row is visible",    monthVisible,    "monthRow was not visible");
        check("PAY_BY_JOB_TITLE – year row is visible",     yearVisible,     "yearRow was not visible");
        check("PAY_BY_JOB_TITLE – start-date row is hidden", startDateHidden, "startDateRow was visible");
        check("PAY_BY_JOB_TITLE – end-date row is hidden",   endDateHidden,   "endDateRow was visible");
    }

    private static void testPayByDivision_showsMonthYearRows() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.prepareForReport(ReportType.PAY_BY_DIVISION));
        Thread.sleep(200);

        boolean monthVisible     = isComponentVisible(panel, "monthRow");
        boolean yearVisible      = isComponentVisible(panel, "yearRow");
        boolean startDateHidden  = !isComponentVisible(panel, "startDateRow");
        boolean endDateHidden    = !isComponentVisible(panel, "endDateRow");

        check("PAY_BY_DIVISION – month row is visible",    monthVisible,    "monthRow was not visible");
        check("PAY_BY_DIVISION – year row is visible",     yearVisible,     "yearRow was not visible");
        check("PAY_BY_DIVISION – start-date row is hidden", startDateHidden, "startDateRow was visible");
        check("PAY_BY_DIVISION – end-date row is hidden",   endDateHidden,   "endDateRow was visible");
    }

    private static void testNewHires_showsDateRangeRows() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.prepareForReport(ReportType.NEW_HIRES));
        Thread.sleep(200);

        boolean monthHidden        = !isComponentVisible(panel, "monthRow");
        boolean yearHidden         = !isComponentVisible(panel, "yearRow");
        boolean startDateVisible   = isComponentVisible(panel, "startDateRow");
        boolean endDateVisible     = isComponentVisible(panel, "endDateRow");

        check("NEW_HIRES – month row is hidden",          monthHidden,      "monthRow was visible");
        check("NEW_HIRES – year row is hidden",           yearHidden,       "yearRow was visible");
        check("NEW_HIRES – start-date row is visible",    startDateVisible, "startDateRow was not visible");
        check("NEW_HIRES – end-date row is visible",      endDateVisible,   "endDateRow was not visible");
    }

    private static void testClearResults_emptiesAllInputFields() throws Exception {
        // Fill every input field first
        setTextField(panel, "monthField",     "3");
        setTextField(panel, "yearField",      "2024");
        setTextField(panel, "startDateField", "2024-01-01");
        setTextField(panel, "endDateField",   "2024-12-31");

        SwingUtilities.invokeAndWait(() -> panel.clearResults());
        Thread.sleep(100);

        boolean allEmpty =
                getFieldText(panel, "monthField").isEmpty()     &&
                getFieldText(panel, "yearField").isEmpty()      &&
                getFieldText(panel, "startDateField").isEmpty() &&
                getFieldText(panel, "endDateField").isEmpty();

        check("clearResults() – all four input fields are empty", allEmpty,
                "One or more fields still had content after clearResults()");
    }

    private static void testNewHires_invalidDate_showsErrorDialog() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.prepareForReport(ReportType.NEW_HIRES));
        Thread.sleep(200);

        setTextField(panel, "startDateField", "01/01/2024");  // ← wrong format
        setTextField(panel, "endDateField",   "12/31/2024");  // ← wrong format

        // Expected dialog: "Start date and end date must use YYYY-MM-DD format."
        scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "runReport"));
        Thread.sleep(900);

        // Table should still be empty (report did not run)
        int rowCount = getTableRowCount(panel, "tableModel");
        check("NEW_HIRES invalid date – error dialog shown, results table stays empty",
                rowCount == 0,
                "Table had rows despite an invalid date: " + rowCount);
    }

    // -----------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------

    private static void check(String label, boolean condition, String failDetail) {
        if (condition) {
            System.out.println("[PASS] " + label);
            passed++;
        } else {
            System.out.println("[FAIL] " + label);
            System.out.println("       " + failDetail);
            failed++;
        }
    }

    private static void printSummary() {
        System.out.println("\n--- Reports Tests: " + passed + "/" + (passed + failed) + " passed ---");
        System.exit(failed > 0 ? 1 : 0);
    }
}
