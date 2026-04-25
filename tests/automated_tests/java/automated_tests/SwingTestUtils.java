package automated_tests;

import java.awt.Component;
import java.awt.Window;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

final class SwingTestUtils {
    private SwingTestUtils() {
    }

    static Object getField(Object object, String fieldName) throws Exception {
        Class<?> type = object.getClass();
        while (type != null) {
            try {
                Field field = type.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(object);
            } catch (NoSuchFieldException e) {
                type = type.getSuperclass();
            }
        }

        throw new NoSuchFieldException("Field '" + fieldName + "' not found in " + object.getClass().getName());
    }

    static void setTextField(Object panel, String fieldName, String text) throws Exception {
        Object field = getField(panel, fieldName);
        SwingUtilities.invokeAndWait(() -> {
            if (field instanceof JTextField) {
                ((JTextField) field).setText(text);
            }
        });
    }

    static String getFieldText(Object panel, String fieldName) throws Exception {
        Object field = getField(panel, fieldName);
        final String[] result = {""};

        SwingUtilities.invokeAndWait(() -> {
            if (field instanceof JTextField) {
                result[0] = ((JTextField) field).getText();
            } else if (field instanceof JTextArea) {
                result[0] = ((JTextArea) field).getText();
            } else if (field instanceof JLabel) {
                result[0] = ((JLabel) field).getText();
            } else if (field instanceof JButton) {
                result[0] = ((JButton) field).getText();
            }
        });

        return result[0];
    }

    static void setComboSelection(Object panel, String fieldName, Object value) throws Exception {
        Object field = getField(panel, fieldName);
        SwingUtilities.invokeAndWait(() -> {
            if (field instanceof JComboBox<?>) {
                ((JComboBox<?>) field).setSelectedItem(value);
            }
        });
    }

    static boolean isComponentVisible(Object panel, String fieldName) throws Exception {
        Object field = getField(panel, fieldName);
        if (!(field instanceof Component)) {
            return false;
        }

        final boolean[] visible = {false};
        SwingUtilities.invokeAndWait(() -> visible[0] = ((Component) field).isVisible());
        return visible[0];
    }

    static int getTableRowCount(Object panel, String modelFieldName) throws Exception {
        Object model = getField(panel, modelFieldName);
        if (!(model instanceof DefaultTableModel)) {
            return -1;
        }

        final int[] count = {0};
        SwingUtilities.invokeAndWait(() -> count[0] = ((DefaultTableModel) model).getRowCount());
        return count[0];
    }

    static void invokePrivateMethod(Object object, String methodName) {
        try {
            Class<?> type = object.getClass();
            while (type != null) {
                try {
                    Method method = type.getDeclaredMethod(methodName);
                    method.setAccessible(true);
                    method.invoke(object);
                    return;
                } catch (NoSuchMethodException e) {
                    type = type.getSuperclass();
                }
            }

            System.err.println("[SwingTestUtils] Method not found: " + methodName);
        } catch (InvocationTargetException e) {
            // Dialogs often interrupt the reflective call path. That is expected here.
        } catch (Exception e) {
            System.err.println("[SwingTestUtils] invokePrivateMethod error: " + e.getMessage());
        }
    }

    static void scheduleDialogClose(long delayMs) {
        Timer timer = new Timer("DialogCloser", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    for (Window window : Window.getWindows()) {
                        if (window instanceof JDialog && window.isVisible()) {
                            window.setVisible(false);
                            window.dispose();
                        }
                    }
                });
            }
        }, delayMs);
    }
}
