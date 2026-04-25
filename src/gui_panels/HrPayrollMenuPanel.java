package gui_panels;

import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class HrPayrollMenuPanel extends JPanel {

    public HrPayrollMenuPanel(MainFrame frame) {
        JButton salaryUpdateBtn = AppUI.createPrimaryButton("1. Update Salaries by Percentage and Range");
        JButton payHistoryBtn = AppUI.createSecondaryButton("2. View Employee Pay History");
        JButton backBtn = AppUI.createSecondaryButton("0. Back");

        salaryUpdateBtn.addActionListener(e -> frame.showSalaryUpdate());
        payHistoryBtn.addActionListener(e -> frame.showEmployeeSearch(EmployeeSearchPanel.ActionMode.PAYROLL_HISTORY));
        backBtn.addActionListener(e -> frame.showHrMenu());

        JPanel body = AppUI.createBodyPanel();
        body.add(AppUI.createMenuButtonStack(salaryUpdateBtn, payHistoryBtn, backBtn));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Payroll / Salary Management",
                "Use this section for salary updates and employee pay history.",
                body));
    }
}
