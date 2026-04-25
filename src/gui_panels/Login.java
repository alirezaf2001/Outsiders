package gui_panels;

import Main_gui.MainFrame;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.Employee;
import service.AuthService;
import service.EmployeeService;

public class Login extends JPanel {
    private final EmployeeService employeeService = new EmployeeService();
    private final AuthService authService = new AuthService();
    private final JTextField userField = AppUI.createTextField(24);
    private final JPasswordField passField = AppUI.createPasswordField(24);

    public Login(MainFrame frame) {
        JButton loginBtn = AppUI.createPrimaryButton("Login");
        JButton exitBtn = AppUI.createSecondaryButton("Exit");

        JPanel formPanel = AppUI.createFormPanel();
        AppUI.addFormRow(formPanel, 0, "Email", userField);
        AppUI.addFormRow(formPanel, 1, "Password (Last 4 of SSN)", passField);

        JPanel body = AppUI.createBodyPanel();
        body.add(formPanel);
        body.add(Box.createVerticalStrut(8));
        body.add(AppUI.createButtonRow(loginBtn, exitBtn));
        body.add(Box.createVerticalStrut(10));

        JLabel helperLabel = AppUI.createInfoLabel(
                "Use the employee email and the last 4 digits of the SSN to log in.");
        helperLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(helperLabel);
        body.add(Box.createVerticalStrut(6));

        loginBtn.addActionListener(e -> handleLogin(frame));
        exitBtn.addActionListener(e -> System.exit(0));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Employee Management System",
                "Log in to continue.",
                body));
    }

    public void clearFields() {
        userField.setText("");
        passField.setText("");
    }

    private void handleLogin(MainFrame frame) {
        String email = userField.getText().trim();
        String password = new String(passField.getPassword());

        if (!authService.login(email, password)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invalid login. Use the employee email and the last 4 digits of the SSN.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passField.setText("");
            return;
        }

        Employee employee = employeeService.searchByEmail(email);
        if (employee == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "The employee record could not be loaded after login.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passField.setText("");
            return;
        }

        boolean hrAdmin = authService.isHrUser(employee.getEmpId());
        frame.startSession(employee, hrAdmin);

        if (hrAdmin) {
            frame.showHrMenu();
        } else {
            frame.showEmployeeMenu();
        }
    }
}
