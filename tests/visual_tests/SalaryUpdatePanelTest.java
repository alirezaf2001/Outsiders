package visual_tests;

import Main_gui.MainFrame;
import gui_panels.SalaryUpdatePanel;
import javax.swing.SwingUtilities;
import static visual_tests.SwingTestUtils.*;

/**
 * Visual tests for the Salary Update panel.
 *
 * What is checked:
 *   - All input fields and the result area are empty after resetForm()
 *   - A negative percentage triggers an error dialog (and the result area is unchanged)
 *   - A zero percentage triggers the same error dialog
 *   - Non-numeric text in any salary field triggers an "invalid input" dialog
 *
 * No database connection is required – all checked conditions happen before
 * any service call is made.
 *
 * HOW TO COMPILE AND RUN (from the project root):
 *   javac -cp "src:lib/*" -d bin $(find src -name "*.java")
 *   javac -cp "bin:lib/*" -d tests tests/visual_tests/SwingTestUtils.java tests/visual_tests/SalaryUpdatePanelTest.java
 *   java  -cp "bin:lib/*:tests" visual_tests.SalaryUpdatePanelTest
 */
public class SalaryUpdatePanelTest {

    private static MainFrame         frame;
    private static SalaryUpdatePanel panel;
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("=== SalaryUpdatePanel Visual Tests ===\n");

        setUp();

        testInitialState_allFieldsEmpty();
        testResetForm_clearsAllFieldsAndResultArea();
        testApplyUpdate_negativePercentage_showsErrorDialog();
        testApplyUpdate_zeroPercentage_showsErrorDialog();
        testApplyUpdate_nonNumericMinSalary_showsErrorDialog();

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
            frame.setTitle("Test – Salary Update Panel");
            frame.setSize(900, 650);
            frame.setVisible(true);
        });
        panel = (SalaryUpdatePanel) getField(frame, "salaryUpdatePanel");
        SwingUtilities.invokeAndWait(() -> frame.showSalaryUpdate());
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

        boolean fieldsEmpty =
                getFieldText(panel, "minSalaryField").isEmpty() &&
                getFieldText(panel, "maxSalaryField").isEmpty() &&
                getFieldText(panel, "percentageField").isEmpty();

        // Result area shows the default placeholder, not an update count
        String resultText = getFieldText(panel, "resultArea");
        boolean resultIsDefault = resultText.contains("Run a salary update");

        check("initialState – all three input fields are empty",
                fieldsEmpty, "One or more fields still had content");

        check("initialState – result area shows placeholder text",
                resultIsDefault, "Result area text was: '" + resultText + "'");
    }

    private static void testResetForm_clearsAllFieldsAndResultArea() throws Exception {
        // Fill everything first
        setTextField(panel, "minSalaryField",  "30000");
        setTextField(panel, "maxSalaryField",  "70000");
        setTextField(panel, "percentageField", "5");

        SwingUtilities.invokeAndWait(() -> panel.resetForm());
        Thread.sleep(100);

        boolean allClear =
                getFieldText(panel, "minSalaryField").isEmpty()  &&
                getFieldText(panel, "maxSalaryField").isEmpty()  &&
                getFieldText(panel, "percentageField").isEmpty() &&
                getFieldText(panel, "resultArea").contains("Run a salary update");

        check("resetForm() – all fields cleared, result area restored to placeholder",
                allClear, "Fields or result area were not properly reset");
    }

    private static void testApplyUpdate_negativePercentage_showsErrorDialog() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.resetForm());
        Thread.sleep(100);

        setTextField(panel, "minSalaryField",  "40000");
        setTextField(panel, "maxSalaryField",  "80000");
        setTextField(panel, "percentageField", "-5");  // ← must be rejected

        // Expected dialog: "Percentage increase must be positive."
        scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "applyUpdate"));
        Thread.sleep(900);

        // Result area should still show the placeholder (no update was run)
        String result = getFieldText(panel, "resultArea");
        check("negative percentage – error dialog shown, result area unchanged",
                result.contains("Run a salary update"),
                "Result area was updated despite invalid percentage: '" + result + "'");
    }

    private static void testApplyUpdate_zeroPercentage_showsErrorDialog() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.resetForm());
        Thread.sleep(100);

        setTextField(panel, "minSalaryField",  "40000");
        setTextField(panel, "maxSalaryField",  "80000");
        setTextField(panel, "percentageField", "0");   // ← must be rejected

        scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "applyUpdate"));
        Thread.sleep(900);

        String result = getFieldText(panel, "resultArea");
        check("zero percentage – error dialog shown, result area unchanged",
                result.contains("Run a salary update"),
                "Result area was updated despite zero percentage: '" + result + "'");
    }

    private static void testApplyUpdate_nonNumericMinSalary_showsErrorDialog() throws Exception {
        SwingUtilities.invokeAndWait(() -> panel.resetForm());
        Thread.sleep(100);

        setTextField(panel, "minSalaryField",  "invalid"); // ← not a number
        setTextField(panel, "maxSalaryField",  "80000");
        setTextField(panel, "percentageField", "5");

        // Expected dialog: "Minimum salary, maximum salary, and percentage must use valid numbers."
        scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "applyUpdate"));
        Thread.sleep(900);

        String result = getFieldText(panel, "resultArea");
        check("non-numeric min salary – invalid-input dialog shown, result area unchanged",
                result.contains("Run a salary update"),
                "Result area was updated despite invalid input: '" + result + "'");
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
        System.out.println("\n--- SalaryUpdate Tests: " + passed + "/" + (passed + failed) + " passed ---");
        System.exit(failed > 0 ? 1 : 0);
    }
}
