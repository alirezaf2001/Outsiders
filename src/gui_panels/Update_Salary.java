package gui_panels;
import Main_gui.MainFrame;
import javax.swing.*;


public class Update_Salary extends JPanel {

    public Update_Salary(MainFrame frame) {

        JLabel title = new JLabel("Update Salary", SwingConstants.CENTER);
        JButton back = new JButton("Back to Menu");

        back.addActionListener(e -> frame.showScreen("menu"));

        add(title);
        add(back);
    }
}
