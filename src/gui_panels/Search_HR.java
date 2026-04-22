package gui_panels;
import Main_gui.MainFrame;
import dao.EmployeeDAO;
import java.awt.*;
import java.sql.SQLException;
import javax.swing.*;
import model.Employee;

public class Search_HR extends JPanel {
    
    private JTextArea resultArea = new JTextArea(5, 20);
    private JTextField empIdField = new JTextField(10);

    public Search_HR(MainFrame frame) {
        setLayout(new GridLayout(0, 1, 5, 5)); 

        JLabel title = new JLabel("Search Employee Screen");
        JLabel prompt = new JLabel("Search by Employee ID:");
        
        JButton searchBtn = new JButton("Search");
     
        JButton backBtn = new JButton("Back to Menu");

        
        /*searchBtn.addActionListener(e -> {
            try {
                int empId = Integer.parseInt(empIdField.getText());
                EmployeeDAO DAO = new EmployeeDAO();

                Employee emp = DAO.findById(empId);

                if (emp != null) {
                    resultArea.setText(emp.toString());
                } else {
                        JOptionPane.showMessageDialog(this, "Employee not found");
                }

            } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error finding employee: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID");
        }
});*/
        
      
        backBtn.addActionListener(e -> frame.showScreen("HRmenu"));
        add(title);
        
        add(prompt);
        add(empIdField);
        add(searchBtn);
     
       add(new JScrollPane(resultArea));
        add(backBtn);
    }
}
