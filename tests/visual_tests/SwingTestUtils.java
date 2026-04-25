package visual_tests;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.lang.reflect.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Shared reflection-based helpers for all visual Swing tests.
 *
 * Key idea: panels keep their fields private, so we use getDeclaredField()
 * to reach inside them.  All EDT-touching calls go through invokeAndWait /
 * invokeLater so we never touch Swing from the test thread directly.
 */
public class SwingTestUtils {

    // -----------------------------------------------------------------------
    // Field access
    // -----------------------------------------------------------------------

    /** Returns the value of a (possibly private) field, searching up the hierarchy. */
    public static Object getField(Object obj, String fieldName) throws Exception {
        Class<?> clazz = obj.getClass();
        while (clazz != null) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f.get(obj);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in " + obj.getClass().getName());
    }

    // -----------------------------------------------------------------------
    // Text helpers (always run on EDT)
    // -----------------------------------------------------------------------

    /** Sets text in a JTextField (or subclass) located by private field name. */
    public static void setTextField(Object panel, String fieldName, String text) throws Exception {
        Object f = getField(panel, fieldName);
        SwingUtilities.invokeAndWait(() -> {
            if (f instanceof JTextField) ((JTextField) f).setText(text);
        });
    }

    /**
     * Reads text from a JTextField, JTextArea, JLabel, or JButton
     * located by private field name.
     */
    public static String getFieldText(Object panel, String fieldName) throws Exception {
        Object f = getField(panel, fieldName);
        final String[] result = {""};
        SwingUtilities.invokeAndWait(() -> {
            if      (f instanceof JTextField) result[0] = ((JTextField) f).getText();
            else if (f instanceof JTextArea)  result[0] = ((JTextArea)  f).getText();
            else if (f instanceof JLabel)     result[0] = ((JLabel)     f).getText();
            else if (f instanceof JButton)    result[0] = ((JButton)    f).getText();
        });
        return result[0];
    }

    // -----------------------------------------------------------------------
    // Component state helpers
    // -----------------------------------------------------------------------

    /** Returns true if the component located by private field name is visible. */
    public static boolean isComponentVisible(Object panel, String fieldName) throws Exception {
        Object f = getField(panel, fieldName);
        if (!(f instanceof Component)) return false;
        final boolean[] v = {false};
        SwingUtilities.invokeAndWait(() -> v[0] = ((Component) f).isVisible());
        return v[0];
    }

    /** Returns the row count of a DefaultTableModel located by private field name. */
    public static int getTableRowCount(Object panel, String modelFieldName) throws Exception {
        Object model = getField(panel, modelFieldName);
        if (!(model instanceof DefaultTableModel)) return -1;
        final int[] count = {0};
        SwingUtilities.invokeAndWait(() -> count[0] = ((DefaultTableModel) model).getRowCount());
        return count[0];
    }

    // -----------------------------------------------------------------------
    // Private method invocation (runs on the calling thread)
    // -----------------------------------------------------------------------

    /**
     * Invokes a no-argument private method by name, walking up the hierarchy.
     * Meant to be wrapped in invokeLater when the method touches Swing.
     * InvocationTargetException is swallowed because dialogs interrupt the
     * call stack in ways that look like exceptions to the reflective caller.
     */
    public static void invokePrivateMethod(Object obj, String methodName) {
        try {
            Class<?> clazz = obj.getClass();
            while (clazz != null) {
                try {
                    Method m = clazz.getDeclaredMethod(methodName);
                    m.setAccessible(true);
                    m.invoke(obj);
                    return;
                } catch (NoSuchMethodException e) {
                    clazz = clazz.getSuperclass();
                }
            }
            System.err.println("[SwingTestUtils] Method not found: " + methodName);
        } catch (InvocationTargetException e) {
            // Dialog interruptions surface here - intentionally ignored
        } catch (Exception e) {
            System.err.println("[SwingTestUtils] invokePrivateMethod error: " + e.getMessage());
        }
    }

    // -----------------------------------------------------------------------
    // Dialog auto-closer
    // -----------------------------------------------------------------------

    /**
     * Schedules a daemon timer that closes every visible JDialog after
     * {@code delayMs} milliseconds.  Call this BEFORE triggering any action
     * that will show a JOptionPane so the dialog doesn't block the test.
     *
     * Typical usage:
     *   scheduleDialogClose(300);
     *   SwingUtilities.invokeLater(() -> invokePrivateMethod(panel, "someMethod"));
     *   Thread.sleep(800);   // wait for dialog to appear and be closed
     */
    public static void scheduleDialogClose(long delayMs) {
        Timer timer = new Timer("DialogCloser", true /* daemon */);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    for (Window w : Window.getWindows()) {
                        if (w instanceof JDialog && w.isVisible()) {
                            w.setVisible(false);
                            w.dispose();
                        }
                    }
                });
            }
        }, delayMs);
    }
}
