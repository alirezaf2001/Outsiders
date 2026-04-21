
package gui_panels;
import Main_gui.MainFrame;
import javax.swing.*;
public class Login extends JPanel {

    public Login(MainFrame frame) {

        JTextField userField = new JTextField(10);
        JPasswordField passField = new JPasswordField(10);
        JButton loginBtn = new JButton("Login");
        JButton MenuRet= new JButton("Return to menu");
        MenuRet.addActionListener((actionEvent) -> {
        frame.showScreen("menu");
    });

        loginBtn.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());

            if (user.equals("admin") && pass.equals("admin")) {
                frame.showScreen("menu");   
            }
             else {
                JOptionPane.showMessageDialog(this, "Invalid login");
            }
        });

        add(new JLabel("Username:"));
        add(userField);
        add(new JLabel("Password:"));
        add(passField);
        add(loginBtn);
        add(MenuRet);
    }
}
