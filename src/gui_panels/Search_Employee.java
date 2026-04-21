package gui_panels;

import Main_gui.MainFrame;
import java.awt.*;
import javax.swing.*;
import service.EmployeeService;

public class Search_Employee extends JPanel {
    private JTextField lastname = new JTextField(10);
    private JTextField empIdField = new JTextField(10);

    public Search_Employee(MainFrame frame) {
        setLayout(new GridLayout(0, 1, 5, 5)); 

        JLabel title = new JLabel("Search Employee Screen");
        JLabel prompt = new JLabel("Search by Employee ID:");
        JLabel prompt2 = new JLabel("Search by Last Name:");
        JButton searchBtn = new JButton("Search");
        JButton searchBtn2 = new JButton("Search"); 
        JButton backBtn = new JButton("Back to Menu");

        
        searchBtn.addActionListener(e -> {
            try {
                int empId = Integer.parseInt(empIdField.getText());
                EmployeeService service = new EmployeeService();
                service.searchEmployeeById(empId);
                JOptionPane.showMessageDialog(this,service.searchEmployeeById(empId) );

             
                

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number");
            }
        });
        searchBtn2.addActionListener(e -> {
            try {
                String lname = lastname.getText();
                EmployeeService service = new EmployeeService();
                service.searchByLastName(lname);
                JOptionPane.showMessageDialog(this,service.searchByLastName(lname));

             
                

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number");
            }
        });
      
        backBtn.addActionListener(e -> frame.showScreen("menu"));
        add(title);
        add(prompt);
        add(empIdField);
        add(searchBtn);
        add(prompt2);
        add(lastname);
        add(searchBtn2);
        add(backBtn);
    }
}
