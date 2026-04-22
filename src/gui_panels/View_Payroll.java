package gui_panels;
import Main_gui.MainFrame;
import java.awt.*;
import javax.swing.*;
public class View_Payroll extends JPanel {
    private JTextArea resultArea = new JTextArea(5, 20);
    private JTextField empIdField = new JTextField(10);

    public View_Payroll(MainFrame frame) {
        setLayout(new GridLayout(0, 1, 5, 5)); 
        JLabel title = new JLabel("View Payroll", SwingConstants.CENTER);
        JLabel prompt = new JLabel("Search by Employee ID:");
        JButton searchBtn = new JButton("Search");
        JButton back = new JButton("Back to Menu");

        back.addActionListener(e -> frame.showScreen("menu"));
        /*searchBtn.addActionListener(e -> {
            try {
                int empId = Integer.parseInt(empIdField.getText());
                PayrollDAO DAO = new PayrollDAO();

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
});/* */ 

        add(title);
        add(prompt);
        add(searchBtn);
        add(back);
    }
}
