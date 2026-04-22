package gui_panels;
import Main_gui.MainFrame;
import java.awt.*;
import javax.swing.*;

public class EmployeeMenuPanel extends JPanel {

    public EmployeeMenuPanel(MainFrame frame) {

        setLayout(new GridLayout(7, 1, 10, 10)); 

        JLabel title = new JLabel("MAIN MENU", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        JButton searchBtn = new JButton("Search Employee");
        JButton viewPayrollBtn = new JButton("View Payroll");
        JButton exitBtn = new JButton("Exit");

        searchBtn.addActionListener(e -> frame.showScreen("Enployeesearch"));
       
        viewPayrollBtn.addActionListener(e -> frame.showScreen("viewPayroll"));
        exitBtn.addActionListener(e -> System.exit(0));

        add(title);
        add(searchBtn);
        
        add(viewPayrollBtn);
        add(exitBtn);
    }
}

