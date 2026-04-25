package gui_panels;

import Main_gui.MainFrame;
import java.awt.BorderLayout;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Employee;
import model.PayrollRecord;
import service.EmployeeService;
import service.PayrollService;

public class PayrollHistoryPanel extends JPanel {
    private final MainFrame frame;
    private final PayrollService payrollService = new PayrollService();
    private final EmployeeService employeeService = new EmployeeService();
    private final JLabel employeeLabel = AppUI.createInfoLabel("No employee selected.");
    private final JLabel statusLabel = AppUI.createInfoLabel("No payroll records loaded.");
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Pay ID", "Pay Date", "Earnings", "Fed Tax", "Fed Med", "Fed SS", "State Tax", "401k", "Health Care"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable historyTable = AppUI.createTable(tableModel);

    private int currentEmployeeId;

    public PayrollHistoryPanel(MainFrame frame) {
        this.frame = frame;

        JButton refreshBtn = AppUI.createPrimaryButton("Refresh");
        JButton backBtn = AppUI.createSecondaryButton("Back");

        refreshBtn.addActionListener(e -> {
            if (currentEmployeeId > 0) {
                loadPayrollHistory(currentEmployeeId);
            }
        });
        backBtn.addActionListener(e -> goBack());

        JPanel body = AppUI.createBodyPanel();
        body.add(employeeLabel);
        body.add(Box.createVerticalStrut(6));
        body.add(statusLabel);
        body.add(Box.createVerticalStrut(8));
        body.add(AppUI.wrapTable(historyTable));
        body.add(Box.createVerticalStrut(14));
        body.add(AppUI.createButtonRow(refreshBtn, backBtn));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Payroll History",
                "Employees can see their own pay history. HR can open history for any selected employee.",
                body));
    }

    public void loadPayrollHistory(int empId) {
        if (!frame.getSession().isLoggedIn()) {
            frame.showLogin();
            return;
        }

        if (!frame.getSession().isHrAdmin() && empId != frame.getSession().getEmpId()) {
            JOptionPane.showMessageDialog(this,
                    "Access denied. Employees may only view their own pay statement history.",
                    "Access Denied",
                    JOptionPane.WARNING_MESSAGE);
            frame.showEmployeeMenu();
            return;
        }

        currentEmployeeId = empId;
        Employee employee = employeeService.searchEmployeeById(empId);
        List<PayrollRecord> payrollHistory = payrollService.findPayrollHistoryByEmployeeId(empId);

        tableModel.setRowCount(0);

        if (employee != null) {
            employeeLabel.setText("Pay history for " + employee.getEmpId() + " - "
                    + employee.getFname() + " " + employee.getLname());
        } else {
            employeeLabel.setText("Pay history for employee ID " + empId);
        }

        for (PayrollRecord record : payrollHistory) {
            tableModel.addRow(new Object[]{
                    record.getPayrollId(),
                    record.getPayDate(),
                    String.format("$%.2f", record.getEarnings()),
                    String.format("$%.2f", record.getFed_tax()),
                    String.format("$%.2f", record.getFed_med()),
                    String.format("$%.2f", record.getFed_SS()),
                    String.format("$%.2f", record.getState_tax()),
                    String.format("$%.2f", record.getRetire_401k()),
                    String.format("$%.2f", record.getHealth_care())
            });
        }

        if (payrollHistory.isEmpty()) {
            statusLabel.setText("No payroll history found for this employee.");
        } else {
            statusLabel.setText("Showing " + payrollHistory.size() + " payroll record(s), newest first.");
        }
    }

    public void clearResults() {
        currentEmployeeId = 0;
        tableModel.setRowCount(0);
        employeeLabel.setText("No employee selected.");
        statusLabel.setText("No payroll records loaded.");
    }

    private void goBack() {
        if (frame.getSession().isHrAdmin()) {
            frame.showHrPayrollMenu();
        } else {
            frame.showEmployeeMenu();
        }
    }
}
