package gui_panels;
import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class HRMenuPanel extends JPanel {

    public HRMenuPanel(MainFrame frame) {
        JButton employeeManagementBtn = AppUI.createPrimaryButton("1. Employee Management");
        JButton payrollManagementBtn = AppUI.createSecondaryButton("2. Payroll / Salary Management");
        JButton reportsBtn = AppUI.createSecondaryButton("3. Reports");
        JButton logoutBtn = AppUI.createSecondaryButton("0. Logout");

        employeeManagementBtn.addActionListener(e -> frame.showHrEmployeeManagementMenu());
        payrollManagementBtn.addActionListener(e -> frame.showHrPayrollMenu());
        reportsBtn.addActionListener(e -> frame.showReportsMenu());
        logoutBtn.addActionListener(e -> frame.logout());

        JPanel body = AppUI.createBodyPanel();
        body.add(AppUI.createMenuButtonStack(
                employeeManagementBtn,
                payrollManagementBtn,
                reportsBtn,
                logoutBtn));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "HR Admin Menu",
                "Choose a section to manage employees, payroll, and reports.",
                body));
    }
}
