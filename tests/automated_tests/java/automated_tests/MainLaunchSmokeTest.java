package automated_tests;

import Main_gui.MainFrame;
import java.awt.Frame;
import java.lang.reflect.Method;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class MainLaunchSmokeTest {
    private static final AutomatedTestSupport.TestReport REPORT =
            new AutomatedTestSupport.TestReport("MainLaunchSmokeTest");

    public static void main(String[] args) {
        MainFrame frame = null;

        try {
            Class<?> mainClass = Class.forName("Main");
            Method mainMethod = mainClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) new String[0]);

            AutomatedTestSupport.sleep(600);
            frame = findMainFrame();

            REPORT.check("Main.main launches a Swing MainFrame", frame != null, "No MainFrame was found after invoking Main.main().");

            if (frame != null) {
                final MainFrame activeFrame = frame;
                SwingUtilities.invokeAndWait(() -> activeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE));

                REPORT.check(
                        "MainFrame becomes visible",
                        frame.isVisible(),
                        "MainFrame was created but was not visible.");
                REPORT.check(
                        "Window title is set",
                        frame.getTitle() != null && frame.getTitle().contains("Employee Management System"),
                        "Unexpected title: '" + frame.getTitle() + "'");
                REPORT.check(
                        "Session starts logged out",
                        !frame.getSession().isLoggedIn(),
                        "The app should start without an active user session.");
            }
        } catch (Throwable error) {
            REPORT.unexpected("Unexpected exception during startup smoke test", error);
        } finally {
            if (frame != null) {
                try {
                    final MainFrame activeFrame = frame;
                    SwingUtilities.invokeAndWait(activeFrame::dispose);
                } catch (Exception e) {
                    REPORT.unexpected("Failed to dispose MainFrame during cleanup", e);
                }
            }
        }

        REPORT.finish();
    }

    private static MainFrame findMainFrame() {
        for (Frame frame : Frame.getFrames()) {
            if (frame instanceof MainFrame && frame.isDisplayable()) {
                return (MainFrame) frame;
            }
        }

        return null;
    }
}
