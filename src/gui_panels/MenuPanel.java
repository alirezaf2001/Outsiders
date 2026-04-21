package gui_panels;
import Main_gui.MainFrame;
import java.awt.*;
import javax.swing.*;

public class MenuPanel extends JPanel {

    public MenuPanel(MainFrame frame) {

        setLayout(new GridLayout(7, 1, 10, 10)); 

        JLabel title = new JLabel("MAIN MENU", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JButton loginBtn = new JButton("Login");
        JButton searchBtn = new JButton("Search Employee");
        JButton updateEmpBtn = new JButton("Update Employee");
        JButton updateSalaryBtn = new JButton("Update Salary");
        JButton viewPayrollBtn = new JButton("View Payroll");
        JButton exitBtn = new JButton("Exit");

        loginBtn.addActionListener(e -> frame.showScreen("login"));
        searchBtn.addActionListener(e -> frame.showScreen("search"));
        updateEmpBtn.addActionListener(e -> frame.showScreen("updateEmployee"));
        updateSalaryBtn.addActionListener(e -> frame.showScreen("updateSalary"));
        viewPayrollBtn.addActionListener(e -> frame.showScreen("viewPayroll"));
        exitBtn.addActionListener(e -> System.exit(0));

        add(title);
        add(loginBtn);
        add(searchBtn);
        add(updateEmpBtn);
        add(updateSalaryBtn);
        add(viewPayrollBtn);
        add(exitBtn);
    }
}

