package gui_panels;

import Main_gui.MainFrame;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Employee;
import service.EmployeeService;

public class EmployeeSearchPanel extends JPanel {
    public enum ActionMode {
        VIEW,
        EDIT,
        DELETE,
        PAYROLL_HISTORY
    }

    private static final String ACCESS_DENIED_MESSAGE =
            "Access denied. Employees may only view their own records.";

    private final MainFrame frame;
    private final EmployeeService employeeService = new EmployeeService();
    private final javax.swing.JComboBox<String> searchTypeCombo = AppUI.createComboBox(
            new String[]{"Employee ID", "Name", "DOB", "SSN"});
    private final JTextField searchValueField = AppUI.createTextField(24);
    private final JLabel modeInfoLabel = AppUI.createInfoLabel("");
    private final JLabel statusLabel = AppUI.createInfoLabel("No search has been run yet.");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Emp ID", "First Name", "Last Name", "Email", "Hire Date", "Salary"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable resultsTable = AppUI.createTable(tableModel);
    private final JButton primaryActionBtn = AppUI.createPrimaryButton("View Details");

    private ActionMode mode = ActionMode.VIEW;
    private List<Employee> currentResults = new ArrayList<>();

    public EmployeeSearchPanel(MainFrame frame) {
        this.frame = frame;

        JButton searchBtn = AppUI.createPrimaryButton("Search");
        JButton clearBtn = AppUI.createSecondaryButton("Clear");
        JButton backBtn = AppUI.createSecondaryButton("Back");

        searchBtn.addActionListener(e -> performSearch());
        clearBtn.addActionListener(e -> resetSearch());
        backBtn.addActionListener(e -> goBack());
        primaryActionBtn.addActionListener(e -> handlePrimaryAction());

        resultsTable.setAutoCreateRowSorter(true);

        JPanel formPanel = AppUI.createFormPanel();
        AppUI.addFormRow(formPanel, 0, "Search Type", searchTypeCombo);
        AppUI.addFormRow(formPanel, 1, "Search Value", searchValueField);

        JScrollPane tableScrollPane = AppUI.wrapTable(resultsTable);
        tableScrollPane.setAlignmentX(LEFT_ALIGNMENT);

        JPanel body = AppUI.createBodyPanel();
        body.add(modeInfoLabel);
        body.add(Box.createVerticalStrut(14));
        body.add(formPanel);
        body.add(Box.createVerticalStrut(6));
        body.add(AppUI.createButtonRow(searchBtn, clearBtn, backBtn));
        body.add(Box.createVerticalStrut(14));
        body.add(statusLabel);
        body.add(Box.createVerticalStrut(8));
        body.add(tableScrollPane);
        body.add(Box.createVerticalStrut(14));
        body.add(AppUI.createButtonRow(primaryActionBtn));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Employee Search",
                "Use one search panel for both HR admins and general employees.",
                body));

        prepareForMode(ActionMode.VIEW);
    }

    public void prepareForMode(ActionMode mode) {
        this.mode = mode;
        primaryActionBtn.setText(getPrimaryActionText());
        modeInfoLabel.setText(getModeMessage());
        resetSearch();
    }

    public void resetSearch() {
        searchValueField.setText("");
        currentResults = new ArrayList<>();
        tableModel.setRowCount(0);
        statusLabel.setText("No search has been run yet.");
        resultsTable.clearSelection();
        primaryActionBtn.setText(getPrimaryActionText());
        modeInfoLabel.setText(getModeMessage());
    }

    private void performSearch() {
        if (!frame.getSession().isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Please log in first.");
            frame.showLogin();
            return;
        }

        String searchType = String.valueOf(searchTypeCombo.getSelectedItem());
        String searchValue = searchValueField.getText().trim();

        if (searchValue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter a value to search.");
            return;
        }

        List<Employee> matches = findEmployees(searchType, searchValue);
        List<Employee> permittedResults = new ArrayList<>();

        if (frame.getSession().isHrAdmin()) {
            permittedResults.addAll(matches);
        } else {
            for (Employee employee : matches) {
                if (employee.getEmpId() == frame.getSession().getEmpId()) {
                    permittedResults.add(employee);
                }
            }

            if (permittedResults.isEmpty() && !matches.isEmpty()) {
                JOptionPane.showMessageDialog(this, ACCESS_DENIED_MESSAGE, "Access Denied", JOptionPane.WARNING_MESSAGE);
            }
        }

        currentResults = permittedResults;
        refreshTable();

        if (currentResults.isEmpty()) {
            statusLabel.setText("No permitted results found for this search.");
        } else {
            statusLabel.setText("Found " + currentResults.size() + " result(s).");
        }
    }

    private List<Employee> findEmployees(String searchType, String searchValue) {
        List<Employee> matches = new ArrayList<>();

        try {
            switch (searchType) {
                case "Employee ID":
                    int empId = Integer.parseInt(searchValue);
                    Employee employee = employeeService.searchEmployeeById(empId);
                    if (employee != null) {
                        matches.add(employee);
                    }
                    break;
                case "Name":
                    matches.addAll(employeeService.searchByName(searchValue));
                    break;
                case "DOB":
                    java.sql.Date.valueOf(searchValue);
                    matches.addAll(employeeService.searchByDOB(searchValue));
                    break;
                case "SSN":
                    matches.addAll(employeeService.searchBySSN(searchValue));
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Employee ID must be a whole number.",
                    "Invalid Employee ID",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Use YYYY-MM-DD for DOB searches.",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
        }

        return matches;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);

        for (Employee employee : currentResults) {
            tableModel.addRow(new Object[]{
                    employee.getEmpId(),
                    employee.getFname(),
                    employee.getLname(),
                    employee.getEmail(),
                    employee.getHireDate(),
                    String.format("$%.2f", employee.getSalary())
            });
        }
    }

    private void handlePrimaryAction() {
        Employee selectedEmployee = getSelectedEmployee();
        if (selectedEmployee == null) {
            JOptionPane.showMessageDialog(this, "Select an employee first.");
            return;
        }

        if (!frame.getSession().isHrAdmin() && selectedEmployee.getEmpId() != frame.getSession().getEmpId()) {
            JOptionPane.showMessageDialog(this, ACCESS_DENIED_MESSAGE, "Access Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }

        switch (mode) {
            case VIEW:
                frame.showEmployeeDetails(selectedEmployee.getEmpId(), false);
                break;
            case EDIT:
                frame.showEmployeeDetails(selectedEmployee.getEmpId(), true);
                break;
            case DELETE:
                deleteSelectedEmployee(selectedEmployee);
                break;
            case PAYROLL_HISTORY:
                frame.showPayrollHistory(selectedEmployee.getEmpId());
                break;
            default:
                break;
        }
    }

    private void deleteSelectedEmployee(Employee employee) {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Delete employee " + employee.getEmpId() + " - " + employee.getFname() + " " + employee.getLname() + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        boolean deleted = employeeService.deleteEmployee(employee.getEmpId());
        if (!deleted) {
            JOptionPane.showMessageDialog(
                    this,
                    "Employee could not be deleted. Check related database records and constraints.",
                    "Delete Failed",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Employee deleted successfully.");

        if (frame.getSession().getEmpId() == employee.getEmpId()) {
            frame.logout();
            return;
        }

        performSearch();
    }

    private Employee getSelectedEmployee() {
        int selectedRow = resultsTable.getSelectedRow();
        if (selectedRow < 0) {
            return null;
        }

        int modelRow = resultsTable.convertRowIndexToModel(selectedRow);
        if (modelRow < 0 || modelRow >= currentResults.size()) {
            return null;
        }

        return currentResults.get(modelRow);
    }

    private void goBack() {
        if (!frame.getSession().isLoggedIn()) {
            frame.showLogin();
            return;
        }

        if (!frame.getSession().isHrAdmin()) {
            frame.showEmployeeMenu();
            return;
        }

        if (mode == ActionMode.PAYROLL_HISTORY) {
            frame.showHrPayrollMenu();
        } else {
            frame.showHrEmployeeManagementMenu();
        }
    }

    private String getPrimaryActionText() {
        if (!frame.getSession().isLoggedIn()) {
            return "View Details";
        }

        if (!frame.getSession().isHrAdmin()) {
            return "View Details";
        }

        switch (mode) {
            case VIEW:
                return "View Details";
            case EDIT:
                return "Edit Selected";
            case DELETE:
                return "Delete Selected";
            case PAYROLL_HISTORY:
                return "View Pay History";
            default:
                return "Open";
        }
    }

    private String getModeMessage() {
        if (!frame.getSession().isLoggedIn()) {
            return "Log in before searching employee records.";
        }

        if (frame.getSession().isHrAdmin()) {
            switch (mode) {
                case VIEW:
                    return "HR admins can search any employee by empID, name, DOB, or SSN.";
                case EDIT:
                    return "Search for the employee you want to edit, then open the edit form.";
                case DELETE:
                    return "Search for the employee you want to delete. Deletion requires confirmation.";
                case PAYROLL_HISTORY:
                    return "Search for an employee, then open that employee's pay statement history.";
                default:
                    return "";
            }
        }

        return "Employees may search only their own record. Any other employee data will be denied.";
    }
}
