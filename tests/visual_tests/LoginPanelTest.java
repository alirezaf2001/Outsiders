package visual_tests;

import Main_gui.MainFrame;
import gui_panels.Login;
import javax.swing.SwingUtilities;
import static visual_tests.SwingTestUtils.*;

/**
 * Visual tests for the Login panel.
 *
 * What is checked:
 *   - Email and password fields are empty when the screen is first shown
 *   - clearFields() empties both fields regardless of their current content
 *
 * No database connection is required for these tests.
 *
 * HOW TO COMPILE AND RUN (from the project root):
 *   javac -cp "src:lib/*" -d bin $(find src -name "*.java")
 *   javac -cp "bin:lib/*" -d tests tests/visual_tests/SwingTestUtils.java tests/visual_tests/LoginPanelTest.java
 *   java  -cp "bin:lib/*:tests" visual_tests.LoginPanelTest
 */
public class LoginPanelTest {

    private static MainFrame frame;
    private static Login     loginPanel;
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        System.out.println("=== LoginPanel Visual Tests ===\n");

        setUp();

        testInitialEmailFieldIsEmpty();
        testInitialPasswordFieldIsEmpty();
        testClearFields_emptiesBothFields();
        testClearFields_worksAfterTypingValues();

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
            frame.setTitle("Test – Login Panel");
            frame.setSize(900, 650);
            frame.setVisible(true);
        });
        loginPanel = (Login) getField(frame, "loginPanel");
        SwingUtilities.invokeAndWait(() -> frame.showLogin());
        Thread.sleep(300);
    }

    private static void tearDown() throws Exception {
        Thread.sleep(600);
        SwingUtilities.invokeAndWait(() -> frame.dispose());
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    private static void testInitialEmailFieldIsEmpty() throws Exception {
        SwingUtilities.invokeAndWait(() -> frame.showLogin());
        Thread.sleep(200);
        String email = getFieldText(loginPanel, "userField");
        check("initialState – email field is empty after showLogin()",
                email.isEmpty(),
                "Expected empty string, got: '" + email + "'");
    }

    private static void testInitialPasswordFieldIsEmpty() throws Exception {
        SwingUtilities.invokeAndWait(() -> frame.showLogin());
        Thread.sleep(200);
        String pass = getFieldText(loginPanel, "passField");
        check("initialState – password field is empty after showLogin()",
                pass.isEmpty(),
                "Expected empty string, got: '" + pass + "'");
    }

    private static void testClearFields_emptiesBothFields() throws Exception {
        setTextField(loginPanel, "userField", "someone@example.com");
        setTextField(loginPanel, "passField", "9999");
        Thread.sleep(100);

        SwingUtilities.invokeAndWait(() -> loginPanel.clearFields());
        Thread.sleep(200);

        String email = getFieldText(loginPanel, "userField");
        String pass  = getFieldText(loginPanel, "passField");
        check("clearFields() – both fields become empty",
                email.isEmpty() && pass.isEmpty(),
                "email='" + email + "'  pass='" + pass + "'");
    }

    private static void testClearFields_worksAfterTypingValues() throws Exception {
        // Type values into both fields
        setTextField(loginPanel, "userField", "admin@company.com");
        setTextField(loginPanel, "passField", "1234");
        Thread.sleep(100);

        // Confirm they have content
        boolean hasContent = !getFieldText(loginPanel, "userField").isEmpty()
                          && !getFieldText(loginPanel, "passField").isEmpty();
        check("fields retain typed values before clearing",
                hasContent,
                "Fields should contain the values just typed");

        // Now clear and verify
        SwingUtilities.invokeAndWait(() -> loginPanel.clearFields());
        Thread.sleep(200);

        String email = getFieldText(loginPanel, "userField");
        String pass  = getFieldText(loginPanel, "passField");
        check("clearFields() after typing – both fields are empty",
                email.isEmpty() && pass.isEmpty(),
                "email='" + email + "'  pass='" + pass + "'");
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
        System.out.println("\n--- Login Tests: " + passed + "/" + (passed + failed) + " passed ---");
        System.exit(failed > 0 ? 1 : 0);
    }
}
