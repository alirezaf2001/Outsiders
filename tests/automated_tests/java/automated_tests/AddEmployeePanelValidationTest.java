package automated_tests;

import Main_gui.MainFrame;
import gui_panels.AddEmployeePanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class AddEmployeePanelValidationTest {
    private static final AutomatedTestSupport.TestReport REPORT =
            new AutomatedTestSupport.TestReport("AddEmployeePanelValidationTest");

    private static MainFrame frame;
    private static AddEmployeePanel panel;

    public static void main(String[] args) {
        try {
            setUp();

            testInitialStateFieldsAreEmpty();
            testResetFormClearsAllFields();
            testNonNumericSalaryShowsValidationError();
            testInvalidHireDateShowsValidationError();
            testNonNumericAddressIdShowsValidationError();
            testNonNumericDivisionIdShowsValidationError();
        } catch (Throwable error) {
            REPORT.unexpected("Unexpected exception during AddEmployeePanel validation test", error);
        } finally {
            tearDown();
        }

        REPORT.finish();
    }

    private static void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.showAddEmployee();
        });
        panel = (AddEmployeePanel) SwingTestUtils.getField(frame, "addEmployeePanel");
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
            REPORT.unexpected("Failed to dispose AddEmployeePanel test frame", e);
        }
    }

    private static void testInitialStateFieldsAreEmpty() throws Exception {
        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        boolean allEmpty =
                SwingTestUtils.getFieldText(panel, "firstNameField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "lastNameField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "emailField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "hireDateField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "salaryField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "ssnField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "addressIdField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "divisionIdField").isEmpty();

        REPORT.check(
                "resetForm() clears all Add Employee fields",
                allEmpty,
                "One or more Add Employee fields still contained text after resetForm().");
    }

    private static void testResetFormClearsAllFields() throws Exception {
        SwingTestUtils.setTextField(panel, "firstNameField", "John");
        SwingTestUtils.setTextField(panel, "lastNameField", "Doe");
        SwingTestUtils.setTextField(panel, "emailField", "john.doe@company.com");
        SwingTestUtils.setTextField(panel, "hireDateField", "2024-01-15");
        SwingTestUtils.setTextField(panel, "salaryField", "55000");
        SwingTestUtils.setTextField(panel, "ssnField", "123-45-6789");
        SwingTestUtils.setTextField(panel, "addressIdField", "42");
        SwingTestUtils.setTextField(panel, "divisionIdField", "2");

        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        boolean allEmpty =
                SwingTestUtils.getFieldText(panel, "firstNameField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "lastNameField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "emailField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "hireDateField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "salaryField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "ssnField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "addressIdField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "divisionIdField").isEmpty();

        REPORT.check(
                "Add Employee fields can be reset after typing data",
                allEmpty,
                "One or more Add Employee fields still contained text after resetForm().");
    }

    private static void testNonNumericSalaryShowsValidationError() throws Exception {
        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        SwingTestUtils.setTextField(panel, "firstNameField", "Jane");
        SwingTestUtils.setTextField(panel, "lastNameField", "Smith");
        SwingTestUtils.setTextField(panel, "emailField", "jane.smith@company.com");
        SwingTestUtils.setTextField(panel, "hireDateField", "2024-06-01");
        SwingTestUtils.setTextField(panel, "salaryField", "notanumber");
        SwingTestUtils.setTextField(panel, "ssnField", "987-65-4321");
        SwingTestUtils.setTextField(panel, "addressIdField", "5");
        SwingTestUtils.setTextField(panel, "divisionIdField", "1");

        SwingTestUtils.scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> SwingTestUtils.invokePrivateMethod(panel, "addEmployee"));
        AutomatedTestSupport.sleep(900);

        REPORT.check(
                "Non-numeric salary does not clear the Add Employee form",
                !SwingTestUtils.getFieldText(panel, "firstNameField").isEmpty(),
                "The form was cleared even though salary validation should have failed.");
    }

    private static void testInvalidHireDateShowsValidationError() throws Exception {
        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        SwingTestUtils.setTextField(panel, "firstNameField", "Bob");
        SwingTestUtils.setTextField(panel, "lastNameField", "Jones");
        SwingTestUtils.setTextField(panel, "emailField", "bob.jones@company.com");
        SwingTestUtils.setTextField(panel, "hireDateField", "15-06-2024");
        SwingTestUtils.setTextField(panel, "salaryField", "48000");
        SwingTestUtils.setTextField(panel, "ssnField", "111-22-3333");
        SwingTestUtils.setTextField(panel, "addressIdField", "3");
        SwingTestUtils.setTextField(panel, "divisionIdField", "2");

        SwingTestUtils.scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> SwingTestUtils.invokePrivateMethod(panel, "addEmployee"));
        AutomatedTestSupport.sleep(900);

        REPORT.check(
                "Invalid hire-date format does not clear the Add Employee form",
                !SwingTestUtils.getFieldText(panel, "firstNameField").isEmpty(),
                "The form was cleared even though hire-date validation should have failed.");
    }

    private static void testNonNumericAddressIdShowsValidationError() throws Exception {
        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        SwingTestUtils.setTextField(panel, "firstNameField", "Alice");
        SwingTestUtils.setTextField(panel, "lastNameField", "Brown");
        SwingTestUtils.setTextField(panel, "emailField", "alice.brown@company.com");
        SwingTestUtils.setTextField(panel, "hireDateField", "2024-03-10");
        SwingTestUtils.setTextField(panel, "salaryField", "52000");
        SwingTestUtils.setTextField(panel, "ssnField", "444-55-6666");
        SwingTestUtils.setTextField(panel, "addressIdField", "badid");
        SwingTestUtils.setTextField(panel, "divisionIdField", "1");

        SwingTestUtils.scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> SwingTestUtils.invokePrivateMethod(panel, "addEmployee"));
        AutomatedTestSupport.sleep(900);

        REPORT.check(
                "Non-numeric address ID does not clear the Add Employee form",
                !SwingTestUtils.getFieldText(panel, "firstNameField").isEmpty(),
                "The form was cleared even though address ID validation should have failed.");
    }

    private static void testNonNumericDivisionIdShowsValidationError() throws Exception {
        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        SwingTestUtils.setTextField(panel, "firstNameField", "Charlie");
        SwingTestUtils.setTextField(panel, "lastNameField", "White");
        SwingTestUtils.setTextField(panel, "emailField", "charlie.white@company.com");
        SwingTestUtils.setTextField(panel, "hireDateField", "2024-07-20");
        SwingTestUtils.setTextField(panel, "salaryField", "61000");
        SwingTestUtils.setTextField(panel, "ssnField", "777-88-9999");
        SwingTestUtils.setTextField(panel, "addressIdField", "7");
        SwingTestUtils.setTextField(panel, "divisionIdField", "notanumber");

        SwingTestUtils.scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> SwingTestUtils.invokePrivateMethod(panel, "addEmployee"));
        AutomatedTestSupport.sleep(900);

        REPORT.check(
                "Non-numeric division ID does not clear the Add Employee form",
                !SwingTestUtils.getFieldText(panel, "firstNameField").isEmpty(),
                "The form was cleared even though division ID validation should have failed.");
    }
}
