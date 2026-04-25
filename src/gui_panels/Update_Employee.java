package gui_panels;
import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.*;


public class Update_Employee extends JPanel {

    public Update_Employee(MainFrame frame) {
        JTextField employeeIdField = AppUI.createTextField(20);
        JTextField firstNameField = AppUI.createTextField(20);
        JTextField lastNameField = AppUI.createTextField(20);
        JTextField emailField = AppUI.createTextField(20);
        JTextField phoneField = AppUI.createTextField(20);
        JTextField divisionField = AppUI.createTextField(20);

        JButton loadButton = AppUI.createPrimaryButton("Load");
        JButton saveButton = AppUI.createSecondaryButton("Save Changes");
        JButton clearButton = AppUI.createSecondaryButton("Clear");
        JButton backButton = AppUI.createSecondaryButton("Back to Menu");

        clearButton.addActionListener(e -> {
            employeeIdField.setText("");
            firstNameField.setText("");
            lastNameField.setText("");
            emailField.setText("");
            phoneField.setText("");
            divisionField.setText("");
        });
        backButton.addActionListener(e -> frame.returnToMenu());

        JPanel formPanel = AppUI.createFormPanel();
        AppUI.addFormRow(formPanel, 0, "Employee ID", employeeIdField);
        AppUI.addFormRow(formPanel, 1, "First Name", firstNameField);
        AppUI.addFormRow(formPanel, 2, "Last Name", lastNameField);
        AppUI.addFormRow(formPanel, 3, "Email", emailField);
        AppUI.addFormRow(formPanel, 4, "Phone", phoneField);
        AppUI.addFormRow(formPanel, 5, "Division", divisionField);

        JPanel body = AppUI.createBodyPanel();
        body.add(formPanel);
        body.add(Box.createVerticalStrut(6));
        body.add(AppUI.createButtonRow(loadButton, saveButton, clearButton, backButton));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Update Employee",
                "Edit employee information.",
                body));
    }
}
