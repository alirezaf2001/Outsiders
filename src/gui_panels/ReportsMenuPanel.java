package gui_panels;

import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ReportsMenuPanel extends JPanel {

    public ReportsMenuPanel(MainFrame frame) {
        JButton byJobTitleBtn = AppUI.createPrimaryButton("1. Total Pay by Job Title for Month");
        JButton byDivisionBtn = AppUI.createSecondaryButton("2. Total Pay by Division for Month");
        JButton newHiresBtn = AppUI.createSecondaryButton("3. New Hires by Date Range");
        JButton backBtn = AppUI.createSecondaryButton("0. Back");

        byJobTitleBtn.addActionListener(e -> frame.showReports(ReportsPanel.ReportType.PAY_BY_JOB_TITLE));
        byDivisionBtn.addActionListener(e -> frame.showReports(ReportsPanel.ReportType.PAY_BY_DIVISION));
        newHiresBtn.addActionListener(e -> frame.showReports(ReportsPanel.ReportType.NEW_HIRES));
        backBtn.addActionListener(e -> frame.showHrMenu());

        JPanel body = AppUI.createBodyPanel();
        body.add(AppUI.createMenuButtonStack(byJobTitleBtn, byDivisionBtn, newHiresBtn, backBtn));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Reports",
                "Choose a report to run.",
                body));
    }
}
