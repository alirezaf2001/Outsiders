package gui_panels;

import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class HrEmployeeManagementMenuPanel extends JPanel {

    public HrEmployeeManagementMenuPanel(MainFrame frame) {
        JButton searchBtn = AppUI.createPrimaryButton("1. Search Employee");
        JButton addBtn = AppUI.createSecondaryButton("2. Add New Employee");
        JButton updateBtn = AppUI.createSecondaryButton("3. Update Employee");
        JButton deleteBtn = AppUI.createSecondaryButton("4. Delete Employee");
        JButton backBtn = AppUI.createSecondaryButton("0. Back");

        searchBtn.addActionListener(e -> frame.showEmployeeSearch(EmployeeSearchPanel.ActionMode.VIEW));
        addBtn.addActionListener(e -> frame.showAddEmployee());
        updateBtn.addActionListener(e -> frame.showEmployeeSearch(EmployeeSearchPanel.ActionMode.EDIT));
        deleteBtn.addActionListener(e -> frame.showEmployeeSearch(EmployeeSearchPanel.ActionMode.DELETE));
        backBtn.addActionListener(e -> frame.showHrMenu());

        JPanel body = AppUI.createBodyPanel();
        body.add(AppUI.createMenuButtonStack(searchBtn, addBtn, updateBtn, deleteBtn, backBtn));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Employee Management",
                "Search, add, update, or delete employee records.",
                body));
    }
}
