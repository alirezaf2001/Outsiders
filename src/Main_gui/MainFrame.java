package Main_gui;

import gui_panels.AddEmployeePanel;
import gui_panels.AppUI;
import gui_panels.EmployeeDetailsPanel;
import gui_panels.EmployeeMenuPanel;
import gui_panels.EmployeeSearchPanel;
import gui_panels.HRMenuPanel;
import gui_panels.HrEmployeeManagementMenuPanel;
import gui_panels.HrPayrollMenuPanel;
import gui_panels.Login;
import gui_panels.PayrollHistoryPanel;
import gui_panels.ReportsMenuPanel;
import gui_panels.ReportsPanel;
import gui_panels.SalaryUpdatePanel;
import java.awt.CardLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import model.Employee;
import session.UserSession;
import service.AuthService;

public class MainFrame extends JFrame {
    private final CardLayout layout = new CardLayout();
    private final JPanel container = new JPanel(layout);
    private final UserSession session = new UserSession();

    private final Login loginPanel;
    private final HRMenuPanel hrMenuPanel;
    private final EmployeeMenuPanel employeeMenuPanel;
    private final HrEmployeeManagementMenuPanel hrEmployeeManagementMenuPanel;
    private final HrPayrollMenuPanel hrPayrollMenuPanel;
    private final ReportsMenuPanel reportsMenuPanel;
    private final EmployeeSearchPanel employeeSearchPanel;
    private final EmployeeDetailsPanel employeeDetailsPanel;
    private final AddEmployeePanel addEmployeePanel;
    private final SalaryUpdatePanel salaryUpdatePanel;
    private final PayrollHistoryPanel payrollHistoryPanel;
    private final ReportsPanel reportsPanel;

    public MainFrame() {
        loginPanel = new Login(this);
        hrMenuPanel = new HRMenuPanel(this);
        employeeMenuPanel = new EmployeeMenuPanel(this);
        hrEmployeeManagementMenuPanel = new HrEmployeeManagementMenuPanel(this);
        hrPayrollMenuPanel = new HrPayrollMenuPanel(this);
        reportsMenuPanel = new ReportsMenuPanel(this);
        employeeSearchPanel = new EmployeeSearchPanel(this);
        employeeDetailsPanel = new EmployeeDetailsPanel(this);
        addEmployeePanel = new AddEmployeePanel(this);
        salaryUpdatePanel = new SalaryUpdatePanel(this);
        payrollHistoryPanel = new PayrollHistoryPanel(this);
        reportsPanel = new ReportsPanel(this);

        container.add(loginPanel, "login");
        container.add(hrMenuPanel, "hrMenu");
        container.add(employeeMenuPanel, "employeeMenu");
        container.add(hrEmployeeManagementMenuPanel, "hrEmployeeManagementMenu");
        container.add(hrPayrollMenuPanel, "hrPayrollMenu");
        container.add(reportsMenuPanel, "reportsMenu");
        container.add(employeeSearchPanel, "employeeSearch");
        container.add(employeeDetailsPanel, "employeeDetails");
        container.add(addEmployeePanel, "addEmployee");
        container.add(salaryUpdatePanel, "salaryUpdate");
        container.add(payrollHistoryPanel, "payrollHistory");
        container.add(reportsPanel, "reportsPanel");

        add(container);

        setTitle("Outsiders Employee Management System");
        setSize(980, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AppUI.styleFrame(this);
        showLogin();
        setVisible(true);
    }

    public UserSession getSession() {
        return session;
    }

    public void startSession(Employee employee, boolean hrAdmin) {
        session.setCurrentEmployee(employee, hrAdmin);
    }

    public void refreshSessionEmployee(Employee employee) {
        if (employee != null && session.isLoggedIn() && employee.getEmpId() == session.getEmpId()) {
            boolean hrAdmin = new AuthService().isHrUser(employee.getEmpId());
            session.setCurrentEmployee(employee, hrAdmin);
        }
    }

    public void showLogin() {
        loginPanel.clearFields();
        showScreen("login");
    }

    public void showHrMenu() {
        showScreen("hrMenu");
    }

    public void showEmployeeMenu() {
        showScreen("employeeMenu");
    }

    public void showHrEmployeeManagementMenu() {
        showScreen("hrEmployeeManagementMenu");
    }

    public void showHrPayrollMenu() {
        showScreen("hrPayrollMenu");
    }

    public void showReportsMenu() {
        showScreen("reportsMenu");
    }

    public void showEmployeeSearch(EmployeeSearchPanel.ActionMode mode) {
        employeeSearchPanel.prepareForMode(mode);
        showScreen("employeeSearch");
    }

    public void showEmployeeDetails(int empId, boolean editable) {
        if (employeeDetailsPanel.loadEmployee(empId, editable)) {
            showScreen("employeeDetails");
        }
    }

    public void showAddEmployee() {
        addEmployeePanel.resetForm();
        showScreen("addEmployee");
    }

    public void showSalaryUpdate() {
        salaryUpdatePanel.resetForm();
        showScreen("salaryUpdate");
    }

    public void showPayrollHistory(int empId) {
        payrollHistoryPanel.loadPayrollHistory(empId);
        showScreen("payrollHistory");
    }

    public void showReports(ReportsPanel.ReportType reportType) {
        reportsPanel.prepareForReport(reportType);
        showScreen("reportsPanel");
    }

    public void logout() {
        session.clear();
        employeeSearchPanel.resetSearch();
        employeeDetailsPanel.clearView();
        addEmployeePanel.resetForm();
        salaryUpdatePanel.resetForm();
        payrollHistoryPanel.clearResults();
        reportsPanel.clearResults();
        showLogin();
    }

    public void showScreen(String name) {
        layout.show(container, name);
    }

    public void showMenu(String name) {
        if ("HRmenu".equals(name)) {
            showHrMenu();
        } else if ("Employeemenu".equals(name)) {
            showEmployeeMenu();
        } else {
            showScreen(name);
        }
    }

    public void returnToMenu() {
        if (!session.isLoggedIn()) {
            showLogin();
        } else if (session.isHrAdmin()) {
            showHrMenu();
        } else {
            showEmployeeMenu();
        }
    }
}
