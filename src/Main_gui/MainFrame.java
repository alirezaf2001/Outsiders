
package Main_gui;
import gui_panels.Login;
import gui_panels.MenuPanel;
import gui_panels.Search_Employee;
import gui_panels.Update_Employee;
import gui_panels.Update_Salary;
import gui_panels.View_Payroll;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    private CardLayout layout = new CardLayout();
    private JPanel container = new JPanel(layout);

    public MainFrame() {
        // Add screens
        container.add(new MenuPanel(this), "menu");
        container.add(new Login(this), "login");
        container.add(new Search_Employee(this), "search");
        container.add(new Update_Employee(this), "updateEmployee");
        container.add(new Update_Salary(this), "updateSalary");
        container.add(new View_Payroll(this), "viewPayroll");



        add(container);

        setTitle("My Application");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        showScreen("MenuPanel"); 
    }

    public void showScreen(String name) {
        layout.show(container, name);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
