package automated_tests;

import Main_gui.MainFrame;
import gui_panels.SalaryUpdatePanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class SalaryUpdatePanelValidationTest {
    private static final AutomatedTestSupport.TestReport REPORT =
            new AutomatedTestSupport.TestReport("SalaryUpdatePanelValidationTest");

    private static MainFrame frame;
    private static SalaryUpdatePanel panel;

    public static void main(String[] args) {
        try {
            setUp();

            testInitialStateIsClear();
            testResetFormRestoresPlaceholder();
            testNegativePercentageShowsValidationError();
            testZeroPercentageShowsValidationError();
            testNonNumericMinSalaryShowsValidationError();
        } catch (Throwable error) {
            REPORT.unexpected("Unexpected exception during SalaryUpdatePanel validation test", error);
        } finally {
            tearDown();
        }

        REPORT.finish();
    }

    private static void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.showSalaryUpdate();
        });
        panel = (SalaryUpdatePanel) SwingTestUtils.getField(frame, "salaryUpdatePanel");
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
            REPORT.unexpected("Failed to dispose SalaryUpdatePanel test frame", e);
        }
    }

    private static void testInitialStateIsClear() throws Exception {
        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        boolean fieldsEmpty =
                SwingTestUtils.getFieldText(panel, "minSalaryField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "maxSalaryField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "percentageField").isEmpty();
        boolean placeholderVisible = SwingTestUtils.getFieldText(panel, "resultArea")
                .contains("Run a salary update");

        REPORT.check(
                "Salary Update fields start empty",
                fieldsEmpty,
                "One or more Salary Update fields still contained text after reset.");
        REPORT.check(
                "Salary Update result area shows placeholder text",
                placeholderVisible,
                "The Salary Update placeholder text was not present.");
    }

    private static void testResetFormRestoresPlaceholder() throws Exception {
        SwingTestUtils.setTextField(panel, "minSalaryField", "30000");
        SwingTestUtils.setTextField(panel, "maxSalaryField", "70000");
        SwingTestUtils.setTextField(panel, "percentageField", "5");

        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        boolean resetWorked =
                SwingTestUtils.getFieldText(panel, "minSalaryField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "maxSalaryField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "percentageField").isEmpty()
                && SwingTestUtils.getFieldText(panel, "resultArea").contains("Run a salary update");

        REPORT.check(
                "resetForm() clears Salary Update fields and restores result placeholder",
                resetWorked,
                "resetForm() did not restore the Salary Update panel to its default state.");
    }

    private static void testNegativePercentageShowsValidationError() throws Exception {
        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        SwingTestUtils.setTextField(panel, "minSalaryField", "40000");
        SwingTestUtils.setTextField(panel, "maxSalaryField", "80000");
        SwingTestUtils.setTextField(panel, "percentageField", "-5");

        SwingTestUtils.scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> SwingTestUtils.invokePrivateMethod(panel, "applyUpdate"));
        AutomatedTestSupport.sleep(900);

        REPORT.check(
                "Negative percentage leaves Salary Update result area unchanged",
                SwingTestUtils.getFieldText(panel, "resultArea").contains("Run a salary update"),
                "The result area changed even though percentage validation should have failed.");
    }

    private static void testZeroPercentageShowsValidationError() throws Exception {
        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        SwingTestUtils.setTextField(panel, "minSalaryField", "40000");
        SwingTestUtils.setTextField(panel, "maxSalaryField", "80000");
        SwingTestUtils.setTextField(panel, "percentageField", "0");

        SwingTestUtils.scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> SwingTestUtils.invokePrivateMethod(panel, "applyUpdate"));
        AutomatedTestSupport.sleep(900);

        REPORT.check(
                "Zero percentage leaves Salary Update result area unchanged",
                SwingTestUtils.getFieldText(panel, "resultArea").contains("Run a salary update"),
                "The result area changed even though zero percentage should be rejected.");
    }

    private static void testNonNumericMinSalaryShowsValidationError() throws Exception {
        SwingUtilities.invokeAndWait(panel::resetForm);
        AutomatedTestSupport.sleep(100);

        SwingTestUtils.setTextField(panel, "minSalaryField", "invalid");
        SwingTestUtils.setTextField(panel, "maxSalaryField", "80000");
        SwingTestUtils.setTextField(panel, "percentageField", "5");

        SwingTestUtils.scheduleDialogClose(300);
        SwingUtilities.invokeLater(() -> SwingTestUtils.invokePrivateMethod(panel, "applyUpdate"));
        AutomatedTestSupport.sleep(900);

        REPORT.check(
                "Non-numeric salary input leaves Salary Update result area unchanged",
                SwingTestUtils.getFieldText(panel, "resultArea").contains("Run a salary update"),
                "The result area changed even though numeric parsing should have failed.");
    }
}
