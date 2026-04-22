
package Main_gui;
import gui_panels.EmployeeMenuPanel;
import gui_panels.HRMenuPanel;
import gui_panels.Login;
import gui_panels.Search_Employee;
import gui_panels.Search_HR;
import gui_panels.Update_Employee;
import gui_panels.Update_Salary;
import gui_panels.View_Payroll;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

    private CardLayout layout = new CardLayout();
    private JPanel container = new JPanel(layout);

    public MainFrame() {
    
        container.add(new HRMenuPanel(this), "HRmenu");
        container.add(new EmployeeMenuPanel(this), "Employeemenu");
        container.add(new Login(this), "login");
        container.add(new Search_Employee(this), "Employeesearch");
        container.add(new Update_Employee(this), "updateEmployee");
        container.add(new Update_Salary(this), "updateSalary");
        container.add(new View_Payroll(this), "viewPayroll");
        container.add(new Search_HR(this), "HRsearch");




        add(container);

        setTitle("My Application");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        showScreen("login"); 
    }

    public void showScreen(String name) {
        layout.show(container, name);
    }

    public static void main(String[] args) {
        new MainFrame();
    }
}
