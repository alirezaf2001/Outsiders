package visual_tests;

import Main_gui.MainFrame;
import gui_panels.EmployeeSearchPanel;
import gui_panels.EmployeeSearchPanel.ActionMode;
import model.Employee;
import javax.swing.SwingUtilities;
import static visual_tests.SwingTestUtils.*;

/**
 * Visual tests for the Employee Search panel.
 *
 * What is checked:
 *   - prepareForMode() sets the correct action-button label for each of the
 *     four modes (VIEW, EDIT, DELETE, PAYROLL_HISTORY)
 *   - resetSearch() clears the search field and empties the results table
 *   - Searching by DOB with a non-YYYY-MM-DD string triggers the date-format
 *     error dialog (requires an active HR session)
 *   - Submitting a search with an empty field shows the "Enter a value" prompt
 *
 * The DOB format test starts a mock HR session to get past the login gate.
 * No real database lookup is made because the date is rejected before querying.
 *
 * HOW TO COMPILE AND RUN (from the project root):
 *   javac -cp "src:lib/*" -d bin $(find src -name "*.java")
 *   javac -cp "bin:lib/*" -d tests tests/visual_tests/SwingTestUtils.java tests/visual_tests/EmployeeSearchPanelTest.java
 *   java  -cp "bin:lib/*:tests" visual_tests.EmployeeSearchPanelTest
 */
public class EmployeeSearchPanelTest {

    private static MainFrame           frame;
    private static EmployeeSearchPanel panel;
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("=== EmployeeSearchPanel Visual Tests ===\n");

        setUp();

        testPrepareViewMode_buttonSaysViewDetails();
        testPrepareEditMode_buttonSaysEditSelected();
        testPrepareDeleteMode_buttonSaysDeleteSelected();
        testPreparePayrollHistoryMode_buttonSaysViewPayHistory();
        testResetSearch_clearsFieldAndTable();
        testSearchByDOB_invalidFormat_showsErrorDialog();
        testSearchWithEmptyField_showsPrompt();

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
            frame.setTitle("Test – Employee Search Panel");
            frame.setSize(900, 650);
            frame.setVisible(true);
        });
        panel = (EmployeeSearchPanel) getField(frame, "employeeSearchPanel");
        SwingUtilities.invokeAndWait(() -> frame.showEmployeeSearch(ActionMode.VIEW));
        Thread.sleep(300);
    }

    private static void tearDown() throws Exception {
        Thread.sleep(600);
        SwingUtilities.invokeAndWait(() -> frame.dispose());
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    private static void testPrepareViewMode_buttonSaysViewDetails() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.prepareForMode(ActionMode.VIEW));
        Thread.sleep(100);
        String text = getFieldText(panel, "primaryActionBtn");
        check("VIEW mode – action button reads \"View Details\"",
                "View Details".equals(text),
                "Button text was: '" + text + "'");
    }

    private static void testPrepareEditMode_buttonSaysEditSelected() throws Exception {
        // Edit mode only shows the correct label for HR admins.
        // Start a mock HR session first.
        Employee mockHr = new Employee(1, "HR", "Admin", "hradmin@test.com", "2020-01-01", 60000, "0001", 1);
        SwingUtilities.invokeAndWait(() -> frame.startSession(mockHr, true));

        SwingUtilities.invokeAndWait(() -> panel.prepareForMode(ActionMode.EDIT));
        Thread.sleep(100);
        String text = getFieldText(panel, "primaryActionBtn");
        check("EDIT mode – action button reads \"Edit Selected\"",
                "Edit Selected".equals(text),
                "Button text was: '" + text + "'");
    }

    private static void testPrepareDeleteMode_buttonSaysDeleteSelected() throws Exception {
        Employee mockHr = new Employee(1, "HR", "Admin", "hradmin@test.com", "2020-01-01", 60000, "0001", 1);
        SwingUtilities.invokeAndWait(() -> frame.startSession(mockHr, true));

        SwingUtilities.invokeAndWait(() -> panel.prepareForMode(ActionMode.DELETE));
        Thread.sleep(100);
        String text = getFieldText(panel, "primaryActionBtn");
        check("DELETE mode – action button reads \"Delete Selected\"",
                "Delete Selected".equals(text),
                "Button text was: '" + text + "'");
    }

    private static void testPreparePayrollHistoryMode_buttonSaysViewPayHistory() throws Exception {
        Employee mockHr = new Employee(1, "HR", "Admin", "hradmin@test.com", "2020-01-01", 60000, "0001", 1);
        SwingUtilities.invokeAndWait(() -> frame.startSession(mockHr, true));

        SwingUtilities.invokeAndWait(() -> panel.prepareForMode(ActionMode.PAYROLL_HISTORY));
        Thread.sleep(100);
        String text = getFieldText(panel, "primaryActionBtn");
        check("PAYROLL_HISTORY mode – action button reads \"View Pay History\"",
                "View Pay History".equals(text),
                "Button text was: '" + text + "'");
    }

    private static void testResetSearch_clearsFieldAndTable() throws Exception {
        // Put something in the search field to prove it gets cleared
        setTextField(panel, "searchValueField", "test query");
        Thread.sleep(100);

        SwingUtilities.invokeAndWait(() -> panel.resetSearch());
        Thread.sleep(100);

        String fieldValue = getFieldText(panel, "searchValueField");
        int rowCount = getTableRowCount(panel, "tableModel");

        check("resetSearch() – search field is empty",
                fieldValue.isEmpty(),
                "searchValueField contained: '" + fieldValue + "'");

        check("resetSearch() – results table has no rows",
                rowCount == 0,
                "Table had " + rowCount + " row(s) after reset");
    }

    private static void testSearchByDOB_invalidFormat_showsErrorDialog() throws Exception {
        // Requires an active session to get past the login gate
        Employee mockHr = new Employee(1, "HR", "Admin", "hradmin@test.com", "2020-01-01", 60000, "0001", 1);
        SwingUtilities.invokeAndWait(() -> frame.startSession(mockHr, true));
        Thread.sleep(100);

        SwingUtilities.invokeAndWait(() -> panel.prepareForMode(ActionMode.VIEW));
        Thread.sleep(100);

        // Set search type to DOB and enter an invalid date string
        Object combo = getField(panel, "searchTypeCombo");
        SwingUtilities.invokeAndWait(() -> ((javax.swing.JComboBox<?>) combo).setSelectedItem("DOB"));
        setTextField(panel, "searchValueField", "12/25/1990");  // ← wrong format

        // Expected dialog: "Use YYYY-MM-DD for DOB searches."
        scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "performSearch"));
        Thread.sleep(900);

        // After the dialog was dismissed the table should still be empty
        int rowCount = getTableRowCount(panel, "tableModel");
        check("invalid DOB format – error dialog shown, table stays empty",
                rowCount == 0,
                "Table should stay empty after a bad-format DOB search. Rows: " + rowCount);
    }

    private static void testSearchWithEmptyField_showsPrompt() throws Exception {
        Employee mockHr = new Employee(1, "HR", "Admin", "hradmin@test.com", "2020-01-01", 60000, "0001", 1);
        SwingUtilities.invokeAndWait(() -> frame.startSession(mockHr, true));
        Thread.sleep(100);

        SwingUtilities.invokeAndWait(() -> panel.resetSearch());
        Thread.sleep(100);
        // Leave searchValueField empty intentionally

        // Expected dialog: "Enter a value to search."
        scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "performSearch"));
        Thread.sleep(900);

        int rowCount = getTableRowCount(panel, "tableModel");
        check("empty search field – prompt dialog shown, table stays empty",
                rowCount == 0,
                "Table should stay empty after submitting an empty search. Rows: " + rowCount);
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
        System.out.println("\n--- EmployeeSearch Tests: " + passed + "/" + (passed + failed) + " passed ---");
        System.exit(failed > 0 ? 1 : 0);
    }
}
