package gui_panels;
import Main_gui.MainFrame;
import javax.swing.*;

public class View_Payroll extends JPanel {

    public View_Payroll(MainFrame frame) {

        JLabel title = new JLabel("View Payroll", SwingConstants.CENTER);
        JButton back = new JButton("Back to Menu");

        back.addActionListener(e -> frame.showScreen("menu"));

        add(title);
        add(back);
    }
}
