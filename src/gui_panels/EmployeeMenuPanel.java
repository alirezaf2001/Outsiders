package gui_panels;
import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class EmployeeMenuPanel extends JPanel {

    public EmployeeMenuPanel(MainFrame frame) {
        JButton profileBtn = AppUI.createPrimaryButton("1. Search / View My Profile");
        JButton payrollBtn = AppUI.createSecondaryButton("2. View My Pay Statement History");
        JButton logoutBtn = AppUI.createSecondaryButton("0. Logout");

        profileBtn.addActionListener(e -> frame.showEmployeeSearch(EmployeeSearchPanel.ActionMode.VIEW));
        payrollBtn.addActionListener(e -> frame.showPayrollHistory(frame.getSession().getEmpId()));
        logoutBtn.addActionListener(e -> frame.logout());

        JPanel body = AppUI.createBodyPanel();
        body.add(AppUI.createMenuButtonStack(profileBtn, payrollBtn, logoutBtn));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Employee Menu",
                "Employees can view only their own profile and pay history.",
                body));
    }
}
