package gui_panels;
import Main_gui.MainFrame;
import javax.swing.*;


public class Update_Employee extends JPanel {

    public Update_Employee(MainFrame frame) {

        JLabel title = new JLabel("Update Employee", SwingConstants.CENTER);
        JButton back = new JButton("Back to Menu");

        back.addActionListener(e -> frame.showScreen("menu"));

        add(title);
        add(back);
    }
}
