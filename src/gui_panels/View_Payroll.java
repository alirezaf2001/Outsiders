package gui_panels;
import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.*;

public class View_Payroll extends JPanel {
    private final JTextArea resultArea = AppUI.createOutputArea(
            8,
            28,
            "Payroll details will appear here after you connect the payroll query.");
    private final JTextField empIdField = AppUI.createTextField(22);

    public View_Payroll(MainFrame frame) {
        JButton searchBtn = AppUI.createPrimaryButton("View Payroll");
        JButton clearBtn = AppUI.createSecondaryButton("Clear");
        JButton backBtn = AppUI.createSecondaryButton("Back to Menu");

        clearBtn.addActionListener(e -> {
            empIdField.setText("");
            resultArea.setText("Payroll details will appear here after you connect the payroll query.");
        });
        backBtn.addActionListener(e -> frame.returnToMenu());

        JPanel formPanel = AppUI.createFormPanel();
        AppUI.addFormRow(formPanel, 0, "Employee ID", empIdField);

        JPanel body = AppUI.createBodyPanel();
        body.add(formPanel);
        body.add(Box.createVerticalStrut(6));
        body.add(AppUI.createButtonRow(searchBtn, clearBtn, backBtn));
        body.add(Box.createVerticalStrut(18));
        body.add(AppUI.createInfoLabel("Payroll Summary"));
        body.add(Box.createVerticalStrut(8));
        body.add(AppUI.wrapTextArea(resultArea));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "View Payroll",
                "View employee payroll information.",
                body));
    }
}
