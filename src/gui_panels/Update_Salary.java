package gui_panels;
import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.*;


public class Update_Salary extends JPanel {

    public Update_Salary(MainFrame frame) {
        JTextField employeeIdField = AppUI.createTextField(20);
        JTextField salaryField = AppUI.createTextField(20);
        JTextField bonusField = AppUI.createTextField(20);
        JTextField effectiveDateField = AppUI.createTextField(20);
        JTextArea notesArea = AppUI.createOutputArea(
                4,
                22,
                "Optional notes or payroll comments.");
        notesArea.setEditable(true);

        JButton loadButton = AppUI.createPrimaryButton("Load");
        JButton saveButton = AppUI.createSecondaryButton("Save Changes");
        JButton clearButton = AppUI.createSecondaryButton("Clear");
        JButton backButton = AppUI.createSecondaryButton("Back to Menu");

        clearButton.addActionListener(e -> {
            employeeIdField.setText("");
            salaryField.setText("");
            bonusField.setText("");
            effectiveDateField.setText("");
            notesArea.setText("Optional notes or payroll comments.");
        });
        backButton.addActionListener(e -> frame.returnToMenu());

        JPanel formPanel = AppUI.createFormPanel();
        AppUI.addFormRow(formPanel, 0, "Employee ID", employeeIdField);
        AppUI.addFormRow(formPanel, 1, "Base Salary", salaryField);
        AppUI.addFormRow(formPanel, 2, "Bonus / Adjustment", bonusField);
        AppUI.addFormRow(formPanel, 3, "Effective Date", effectiveDateField);
        AppUI.addFormRow(formPanel, 4, "Notes", AppUI.wrapTextArea(notesArea));

        JPanel body = AppUI.createBodyPanel();
        body.add(formPanel);
        body.add(Box.createVerticalStrut(6));
        body.add(AppUI.createButtonRow(loadButton, saveButton, clearButton, backButton));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Update Salary",
                "Modify employee salary information.",
                body));
    }
}
