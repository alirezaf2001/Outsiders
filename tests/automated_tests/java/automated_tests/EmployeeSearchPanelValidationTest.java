package automated_tests;

import Main_gui.MainFrame;
import gui_panels.EmployeeSearchPanel;
import gui_panels.EmployeeSearchPanel.ActionMode;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import model.Employee;

public class EmployeeSearchPanelValidationTest {
    private static final AutomatedTestSupport.TestReport REPORT =
            new AutomatedTestSupport.TestReport("EmployeeSearchPanelValidationTest");

    private static MainFrame frame;
    private static EmployeeSearchPanel panel;

    public static void main(String[] args) {
        try {
            setUp();

            testViewModeButtonLabel();
            testEditModeButtonLabel();
            testDeleteModeButtonLabel();
            testPayrollHistoryModeButtonLabel();
            testResetSearchClearsFieldAndTable();
            testInvalidDobFormatKeepsTableEmpty();
            testEmptySearchKeepsTableEmpty();
        } catch (Throwable error) {
            REPORT.unexpected("Unexpected exception during EmployeeSearchPanel validation test", error);
        } finally {
            tearDown();
        }

        REPORT.finish();
    }

    private static void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.showEmployeeSearch(ActionMode.VIEW);
        });
        panel = (EmployeeSearchPanel) SwingTestUtils.getField(frame, "employeeSearchPanel");
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
            REPORT.unexpected("Failed to dispose EmployeeSearchPanel test frame", e);
        }
    }

    private static void testViewModeButtonLabel() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.prepareForMode(ActionMode.VIEW));
        AutomatedTestSupport.sleep(100);

        REPORT.check(
                "VIEW mode uses 'View Details' action text",
                "View Details".equals(SwingTestUtils.getFieldText(panel, "primaryActionBtn")),
                "Unexpected VIEW mode action text.");
    }

    private static void testEditModeButtonLabel() throws Exception {
        startMockHrSession();
        SwingUtilities.invokeAndWait(() -> panel.prepareForMode(ActionMode.EDIT));
        AutomatedTestSupport.sleep(100);

        REPORT.check(
                "EDIT mode uses 'Edit Selected' action text",
                "Edit Selected".equals(SwingTestUtils.getFieldText(panel, "primaryActionBtn")),
                "Unexpected EDIT mode action text.");
    }

    private static void testDeleteModeButtonLabel() throws Exception {
        startMockHrSession();
        SwingUtilities.invokeAndWait(() -> panel.prepareForMode(ActionMode.DELETE));
        AutomatedTestSupport.sleep(100);

        REPORT.check(
                "DELETE mode uses 'Delete Selected' action text",
                "Delete Selected".equals(SwingTestUtils.getFieldText(panel, "primaryActionBtn")),
                "Unexpected DELETE mode action text.");
    }

    private static void testPayrollHistoryModeButtonLabel() throws Exception {
        startMockHrSession();
        SwingUtilities.invokeAndWait(() -> panel.prepareForMode(ActionMode.PAYROLL_HISTORY));
        AutomatedTestSupport.sleep(100);

        REPORT.check(
                "PAYROLL_HISTORY mode uses 'View Pay History' action text",
                "View Pay History".equals(SwingTestUtils.getFieldText(panel, "primaryActionBtn")),
                "Unexpected PAYROLL_HISTORY mode action text.");
    }

    private static void testResetSearchClearsFieldAndTable() throws Exception {
        SwingTestUtils.setTextField(panel, "searchValueField", "test query");
        AutomatedTestSupport.sleep(100);

        SwingUtilities.invokeAndWait(panel::resetSearch);
        AutomatedTestSupport.sleep(100);

        REPORT.check(
                "resetSearch() clears the search input",
                SwingTestUtils.getFieldText(panel, "searchValueField").isEmpty(),
                "The search input still contained text after resetSearch().");
        REPORT.check(
                "resetSearch() clears the result table",
                SwingTestUtils.getTableRowCount(panel, "tableModel") == 0,
                "The result table still had rows after resetSearch().");
    }

    private static void testInvalidDobFormatKeepsTableEmpty() throws Exception {
        startMockHrSession();
        SwingUtilities.invokeAndWait(() -> panel.prepareForMode(ActionMode.VIEW));
        AutomatedTestSupport.sleep(100);

        SwingTestUtils.setComboSelection(panel, "searchTypeCombo", "DOB");
        SwingTestUtils.setTextField(panel, "searchValueField", "12/25/1990");

        SwingTestUtils.scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> SwingTestUtils.invokePrivateMethod(panel, "performSearch"));
        AutomatedTestSupport.sleep(900);

        REPORT.check(
                "Invalid DOB input leaves Employee Search results empty",
                SwingTestUtils.getTableRowCount(panel, "tableModel") == 0,
                "The result table changed even though DOB validation should have failed.");
    }

    private static void testEmptySearchKeepsTableEmpty() throws Exception {
        startMockHrSession();
        SwingUtilities.invokeAndWait(panel::resetSearch);
        AutomatedTestSupport.sleep(100);

        SwingTestUtils.scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> SwingTestUtils.invokePrivateMethod(panel, "performSearch"));
        AutomatedTestSupport.sleep(900);

        REPORT.check(
                "Empty Employee Search input leaves results empty",
                SwingTestUtils.getTableRowCount(panel, "tableModel") == 0,
                "The result table changed even though the search value was empty.");
    }

    private static void startMockHrSession() throws Exception {
        Employee mockHr = new Employee(1, "HR", "Admin", "hradmin@test.com", "2020-01-01", 60000, "0001", 1);
        SwingUtilities.invokeAndWait(() -> frame.startSession(mockHr, true));
    }
}
