
package gui_panels;
import Main_gui.MainFrame;
import javax.swing.*;
import service.AuthService;
import service.EmployeeService;

public class Login extends JPanel {
    private final EmployeeService employeeService = new EmployeeService();
    public Login(MainFrame frame) {
         
        JTextField userField = new JTextField(10);
        JPasswordField passField = new JPasswordField(10);
        JButton loginBtn = new JButton("Login");
        JButton exitBtn = new JButton("Exit");
        

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            AuthService authService = new AuthService();
            if (authService.login(user, pass)) {
                if (authService.isHrUser(employeeService.searchByEmail(user).getEmpId())) {
                    frame.showScreen("HRmenu");
                } else {
                    frame.showScreen("Employeemenu");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid login");
            }

            // TODO: Temporary hardcoded login logic for testing !!! DETELE THIS LATER !!!
            if (user.equals("admin") && pass.equals("admin")) {
                frame.showScreen("HRmenu");   
            }
            else if (user.equals("admin2") && pass.equals("admin2")) {
                frame.showScreen("Employeemenu");   
            }
             else {
                JOptionPane.showMessageDialog(this, "Invalid login");
            }
        });
        exitBtn.addActionListener(e -> System.exit(0));
        add(new JLabel("Username:"));
        add(userField);
        add(new JLabel("Password:"));
        add(passField);
        add(loginBtn);
        add(exitBtn);
        
    }
}
