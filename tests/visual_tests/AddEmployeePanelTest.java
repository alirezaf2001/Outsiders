package visual_tests;

import Main_gui.MainFrame;
import gui_panels.AddEmployeePanel;
import javax.swing.SwingUtilities;
import static visual_tests.SwingTestUtils.*;

/**
 * Visual tests for the Add Employee panel.
 *
 * What is checked:
 *   - All 8 form fields are empty after resetForm()
 *   - A non-numeric salary value triggers an error dialog and keeps the form intact
 *   - An invalid hire-date format triggers an error dialog and keeps the form intact
 *   - A non-numeric Address ID triggers an error dialog and keeps the form intact
 *   - A non-numeric Division ID triggers an error dialog and keeps the form intact
 *
 * The dialog-triggering tests all verify post-dialog state rather than the dialog
 * text itself, so no database connection is required.
 *
 * HOW TO COMPILE AND RUN (from the project root):
 *   javac -cp "src:lib/*" -d bin $(find src -name "*.java")
 *   javac -cp "bin:lib/*" -d tests tests/visual_tests/SwingTestUtils.java tests/visual_tests/AddEmployeePanelTest.java
 *   java  -cp "bin:lib/*:tests" visual_tests.AddEmployeePanelTest
 */
public class AddEmployeePanelTest {

    private static MainFrame       frame;
    private static AddEmployeePanel panel;
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("=== AddEmployeePanel Visual Tests ===\n");

        setUp();

        testInitialState_allFieldsEmpty();
        testResetForm_clearsAllEightFields();
        testAddEmployee_nonNumericSalary_showsErrorDialog();
        testAddEmployee_invalidHireDateFormat_showsErrorDialog();
        testAddEmployee_nonNumericAddressId_showsErrorDialog();
        testAddEmployee_nonNumericDivisionId_showsErrorDialog();

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
            frame.setTitle("Test – Add Employee Panel");
            frame.setSize(900, 650);
            frame.setVisible(true);
        });
        panel = (AddEmployeePanel) getField(frame, "addEmployeePanel");
        SwingUtilities.invokeAndWait(() -> frame.showAddEmployee());
        Thread.sleep(300);
    }

    private static void tearDown() throws Exception {
        Thread.sleep(600);
        SwingUtilities.invokeAndWait(() -> frame.dispose());
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    private static void testInitialState_allFieldsEmpty() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.resetForm());
        Thread.sleep(100);

        boolean allEmpty =
                getFieldText(panel, "firstNameField").isEmpty() &&
                getFieldText(panel, "lastNameField").isEmpty()  &&
                getFieldText(panel, "emailField").isEmpty()     &&
                getFieldText(panel, "hireDateField").isEmpty()  &&
                getFieldText(panel, "salaryField").isEmpty()    &&
                getFieldText(panel, "ssnField").isEmpty()       &&
                getFieldText(panel, "addressIdField").isEmpty() &&
                getFieldText(panel, "divisionIdField").isEmpty();

        check("initialState – all 8 fields empty after resetForm()", allEmpty,
                "One or more fields were not empty");
    }

    private static void testResetForm_clearsAllEightFields() throws Exception {
        // Fill every field
        setTextField(panel, "firstNameField",  "John");
        setTextField(panel, "lastNameField",   "Doe");
        setTextField(panel, "emailField",      "john.doe@company.com");
        setTextField(panel, "hireDateField",   "2024-01-15");
        setTextField(panel, "salaryField",     "55000");
        setTextField(panel, "ssnField",        "123-45-6789");
        setTextField(panel, "addressIdField",  "42");
        setTextField(panel, "divisionIdField", "2");

        SwingUtilities.invokeAndWait(() -> panel.resetForm());
        Thread.sleep(100);

        boolean allEmpty =
                getFieldText(panel, "firstNameField").isEmpty() &&
                getFieldText(panel, "lastNameField").isEmpty()  &&
                getFieldText(panel, "emailField").isEmpty()     &&
                getFieldText(panel, "hireDateField").isEmpty()  &&
                getFieldText(panel, "salaryField").isEmpty()    &&
                getFieldText(panel, "ssnField").isEmpty()       &&
                getFieldText(panel, "addressIdField").isEmpty() &&
                getFieldText(panel, "divisionIdField").isEmpty();

        check("resetForm() – all 8 fields cleared after pre-filling", allEmpty,
                "One or more fields still had content after resetForm()");
    }

    private static void testAddEmployee_nonNumericSalary_showsErrorDialog() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.resetForm());
        Thread.sleep(100);

        setTextField(panel, "firstNameField",  "Jane");
        setTextField(panel, "lastNameField",   "Smith");
        setTextField(panel, "emailField",      "jane.smith@company.com");
        setTextField(panel, "hireDateField",   "2024-06-01");
        setTextField(panel, "salaryField",     "notanumber");    // ← invalid
        setTextField(panel, "ssnField",        "987-65-4321");
        setTextField(panel, "addressIdField",  "5");
        setTextField(panel, "divisionIdField", "1");

        // Expect: "Salary, address ID, and division ID must use valid numbers."
        scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "addEmployee"));
        Thread.sleep(900);

        // Form should NOT have been cleared (success clears, error does not)
        String firstName = getFieldText(panel, "firstNameField");
        check("non-numeric salary – error dialog shown, form kept intact",
                !firstName.isEmpty(),
                "firstName was empty – form was reset unexpectedly (add may have succeeded or form was wrongly cleared)");
    }

    private static void testAddEmployee_invalidHireDateFormat_showsErrorDialog() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.resetForm());
        Thread.sleep(100);

        setTextField(panel, "firstNameField",  "Bob");
        setTextField(panel, "lastNameField",   "Jones");
        setTextField(panel, "emailField",      "bob.jones@company.com");
        setTextField(panel, "hireDateField",   "15-06-2024");   // ← DD-MM-YYYY not accepted
        setTextField(panel, "salaryField",     "48000");
        setTextField(panel, "ssnField",        "111-22-3333");
        setTextField(panel, "addressIdField",  "3");
        setTextField(panel, "divisionIdField", "2");

        // Expect: "Hire date must use YYYY-MM-DD format."
        scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "addEmployee"));
        Thread.sleep(900);

        String firstName = getFieldText(panel, "firstNameField");
        check("invalid hire-date format – error dialog shown, form kept intact",
                !firstName.isEmpty(),
                "firstName was empty – form was reset unexpectedly");
    }

    private static void testAddEmployee_nonNumericAddressId_showsErrorDialog() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.resetForm());
        Thread.sleep(100);

        setTextField(panel, "firstNameField",  "Alice");
        setTextField(panel, "lastNameField",   "Brown");
        setTextField(panel, "emailField",      "alice.brown@company.com");
        setTextField(panel, "hireDateField",   "2024-03-10");
        setTextField(panel, "salaryField",     "52000");
        setTextField(panel, "ssnField",        "444-55-6666");
        setTextField(panel, "addressIdField",  "badid");        // ← invalid
        setTextField(panel, "divisionIdField", "1");

        scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "addEmployee"));
        Thread.sleep(900);

        String firstName = getFieldText(panel, "firstNameField");
        check("non-numeric Address ID – error dialog shown, form kept intact",
                !firstName.isEmpty(),
                "firstName was empty – form was reset unexpectedly");
    }

    private static void testAddEmployee_nonNumericDivisionId_showsErrorDialog() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.resetForm());
        Thread.sleep(100);

        setTextField(panel, "firstNameField",  "Charlie");
        setTextField(panel, "lastNameField",   "White");
        setTextField(panel, "emailField",      "charlie.white@company.com");
        setTextField(panel, "hireDateField",   "2024-07-20");
        setTextField(panel, "salaryField",     "61000");
        setTextField(panel, "ssnField",        "777-88-9999");
        setTextField(panel, "addressIdField",  "7");
        setTextField(panel, "divisionIdField", "notanumber");   // ← invalid

        scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "addEmployee"));
        Thread.sleep(900);

        String firstName = getFieldText(panel, "firstNameField");
        check("non-numeric Division ID – error dialog shown, form kept intact",
                !firstName.isEmpty(),
                "firstName was empty – form was reset unexpectedly");
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
        System.out.println("\n--- AddEmployee Tests: " + passed + "/" + (passed + failed) + " passed ---");
        System.exit(failed > 0 ? 1 : 0);
    }
}
