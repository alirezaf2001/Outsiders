package automated_tests;

import Main_gui.MainFrame;
import gui_panels.ReportsPanel;
import gui_panels.ReportsPanel.ReportType;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ReportsPanelValidationTest {
    private static final AutomatedTestSupport.TestReport REPORT =
            new AutomatedTestSupport.TestReport("ReportsPanelValidationTest");

    private static MainFrame frame;
    private static ReportsPanel panel;

    public static void main(String[] args) {
        try {
            setUp();

            testPayByJobTitleVisibility();
            testPayByDivisionVisibility();
            testNewHiresVisibility();
            testClearResultsEmptiesAllInputs();
            testInvalidNewHireDateKeepsTableEmpty();
        } catch (Throwable error) {
            REPORT.unexpected("Unexpected exception during ReportsPanel validation test", error);
        } finally {
            tearDown();
        }

        REPORT.finish();
    }

    private static void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.showReports(ReportType.PAY_BY_JOB_TITLE);
        });
        panel = (ReportsPanel) SwingTestUtils.getField(frame, "reportsPanel");
        AutomatedTestSupport.sleep(250);
    }

    private static void tearDown() {
        if (frame == null) {
            return;
        }

        try {
            AutomatedTestSupport.sleep(300);
            SwingUtilities.invokeAndWait(frame::dispose);
        } catch (Exception e) {
            REPORT.unexpected("Failed to dispose ReportsPanel test frame", e);
        }
    }

    private static void testPayByJobTitleVisibility() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.prepareForReport(ReportType.PAY_BY_JOB_TITLE));
        AutomatedTestSupport.sleep(100);

        REPORT.check("PAY_BY_JOB_TITLE shows the month row",
                SwingTestUtils.isComponentVisible(panel, "monthRow"),
                "monthRow was not visible.");
        REPORT.check("PAY_BY_JOB_TITLE shows the year row",
                SwingTestUtils.isComponentVisible(panel, "yearRow"),
                "yearRow was not visible.");
        REPORT.check("PAY_BY_JOB_TITLE hides the start-date row",
                !SwingTestUtils.isComponentVisible(panel, "startDateRow"),
                "startDateRow was visible.");
        REPORT.check("PAY_BY_JOB_TITLE hides the end-date row",
                !SwingTestUtils.isComponentVisible(panel, "endDateRow"),
                "endDateRow was visible.");
    }

    private static void testPayByDivisionVisibility() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.prepareForReport(ReportType.PAY_BY_DIVISION));
        AutomatedTestSupport.sleep(100);

        REPORT.check("PAY_BY_DIVISION shows the month row",
                SwingTestUtils.isComponentVisible(panel, "monthRow"),
                "monthRow was not visible.");
        REPORT.check("PAY_BY_DIVISION shows the year row",
                SwingTestUtils.isComponentVisible(panel, "yearRow"),
                "yearRow was not visible.");
        REPORT.check("PAY_BY_DIVISION hides the start-date row",
                !SwingTestUtils.isComponentVisible(panel, "startDateRow"),
                "startDateRow was visible.");
        REPORT.check("PAY_BY_DIVISION hides the end-date row",
                !SwingTestUtils.isComponentVisible(panel, "endDateRow"),
                "endDateRow was visible.");
    }

    private static void testNewHiresVisibility() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.prepareForReport(ReportType.NEW_HIRES));
        AutomatedTestSupport.sleep(100);

        REPORT.check("NEW_HIRES hides the month row",
                !SwingTestUtils.isComponentVisible(panel, "monthRow"),
                "monthRow was visible.");
        REPORT.check("NEW_HIRES hides the year row",
                !SwingTestUtils.isComponentVisible(panel, "yearRow"),
                "yearRow was visible.");
        REPORT.check("NEW_HIRES shows the start-date row",
                SwingTestUtils.isComponentVisible(panel, "startDateRow"),
                "startDateRow was not visible.");
        REPORT.check("NEW_HIRES shows the end-date row",
                SwingTestUtils.isComponentVisible(panel, "endDateRow"),
                "endDateRow was not visible.");
    }

    private static void testClearResultsEmptiesAllInputs() throws Exception {
        SwingTestUtils.setTextField(panel, "monthField", "3");
        SwingTestUtils.setTextField(panel, "yearField", "2024");
        SwingTestUtils.setTextField(panel, "startDateField", "2024-01-01");
        SwingTestUtils.setTextField(panel, "endDateField", "2024-12-31");

        SwingUtilities.invokeAndWait(panel::clearResults);
        AutomatedTestSupport.sleep(100);

        boolean allEmpty =
                SwingTestUtils.getFieldText(panel, "monthField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "yearField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "startDateField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "endDateField").isEmpty();

        REPORT.check(
                "clearResults() empties all report inputs",
                allEmpty,
                "One or more Reports inputs still contained text after clearResults().");
    }

    private static void testInvalidNewHireDateKeepsTableEmpty() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.prepareForReport(ReportType.NEW_HIRES));
        AutomatedTestSupport.sleep(100);

        SwingTestUtils.setTextField(panel, "startDateField", "01/01/2024");
        SwingTestUtils.setTextField(panel, "endDateField", "12/31/2024");

        SwingTestUtils.scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> SwingTestUtils.invokePrivateMethod(panel, "runReport"));
        AutomatedTestSupport.sleep(900);

        REPORT.check(
                "Invalid New Hires date input leaves the report table empty",
                SwingTestUtils.getTableRowCount(panel, "tableModel") == 0,
                "The report table changed even though date validation should have failed.");
    }
}
