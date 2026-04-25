package automated_tests;

import Main_gui.MainFrame;
import gui_panels.Login;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class LoginPanelSmokeTest {
    private static final AutomatedTestSupport.TestReport REPORT =
            new AutomatedTestSupport.TestReport("LoginPanelSmokeTest");

    private static MainFrame frame;
    private static Login loginPanel;

    public static void main(String[] args) {
        try {
            setUp();

            testInitialEmailFieldIsEmpty();
            testInitialPasswordFieldIsEmpty();
            testClearFieldsEmptiesBothFields();
            testClearFieldsWorksAfterTypingValues();
        } catch (Throwable error) {
            REPORT.unexpected("Unexpected exception during login panel smoke test", error);
        } finally {
            tearDown();
        }

        REPORT.finish();
    }

    private static void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            frame = new MainFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        });
        loginPanel = (Login) SwingTestUtils.getField(frame, "loginPanel");
        SwingUtilities.invokeAndWait(frame::showLogin);
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
            REPORT.unexpected("Failed to dispose LoginPanel test frame", e);
        }
    }

    private static void testInitialEmailFieldIsEmpty() throws Exception {
        SwingUtilities.invokeAndWait(frame::showLogin);
        AutomatedTestSupport.sleep(150);

        String email = SwingTestUtils.getFieldText(loginPanel, "userField");
        REPORT.check(
                "Email field starts empty",
                email.isEmpty(),
                "Expected the email field to be empty, but found '" + email + "'.");
    }

    private static void testInitialPasswordFieldIsEmpty() throws Exception {
        SwingUtilities.invokeAndWait(frame::showLogin);
        AutomatedTestSupport.sleep(150);

        String password = SwingTestUtils.getFieldText(loginPanel, "passField");
        REPORT.check(
                "Password field starts empty",
                password.isEmpty(),
                "Expected the password field to be empty, but found '" + password + "'.");
    }

    private static void testClearFieldsEmptiesBothFields() throws Exception {
        SwingTestUtils.setTextField(loginPanel, "userField", "someone@example.com");
        SwingTestUtils.setTextField(loginPanel, "passField", "9999");

        SwingUtilities.invokeAndWait(loginPanel::clearFields);
        AutomatedTestSupport.sleep(150);

        String email = SwingTestUtils.getFieldText(loginPanel, "userField");
        String password = SwingTestUtils.getFieldText(loginPanel, "passField");

        REPORT.check(
                "clearFields() empties both login inputs",
                email.isEmpty() && password.isEmpty(),
                "Expected both fields to be empty, but found email='" + email + "', password='" + password + "'.");
    }

    private static void testClearFieldsWorksAfterTypingValues() throws Exception {
        SwingTestUtils.setTextField(loginPanel, "userField", "admin@company.com");
        SwingTestUtils.setTextField(loginPanel, "passField", "1234");
        AutomatedTestSupport.sleep(100);

        boolean hasContent = !SwingTestUtils.getFieldText(loginPanel, "userField").isEmpty()
                && !SwingTestUtils.getFieldText(loginPanel, "passField").isEmpty();
        REPORT.check(
                "Login fields keep typed values before clearing",
                hasContent,
                "Expected both fields to contain the test values before calling clearFields().");

        SwingUtilities.invokeAndWait(loginPanel::clearFields);
        AutomatedTestSupport.sleep(150);

        String email = SwingTestUtils.getFieldText(loginPanel, "userField");
        String password = SwingTestUtils.getFieldText(loginPanel, "passField");
        REPORT.check(
                "clearFields() still works after the user typed values",
                email.isEmpty() && password.isEmpty(),
                "Expected both fields to be empty after clearing, but found email='" + email + "', password='" + password + "'.");
    }
}
