package gui_panels;

import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import model.Employee;
import model.MonthlyPaySummary;
import service.ReportService;

public class ReportsPanel extends JPanel {
    public enum ReportType {
        PAY_BY_JOB_TITLE,
        PAY_BY_DIVISION,
        NEW_HIRES
    }

    private final ReportService reportService = new ReportService();
    private final JLabel reportLabel = AppUI.createInfoLabel("");
    private final JTextField monthField = AppUI.createTextField(20);
    private final JTextField yearField = AppUI.createTextField(20);
    private final JTextField startDateField = AppUI.createTextField(20);
    private final JTextField endDateField = AppUI.createTextField(20);
    private final JPanel monthRow = createInputRow("Month (1-12)", monthField);
    private final JPanel yearRow = createInputRow("Year", yearField);
    private final JPanel startDateRow = createInputRow("Start Date (YYYY-MM-DD)", startDateField);
    private final JPanel endDateRow = createInputRow("End Date (YYYY-MM-DD)", endDateField);
    private final DefaultTableModel tableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable resultsTable = AppUI.createTable(tableModel);

    private ReportType reportType = ReportType.PAY_BY_JOB_TITLE;

    public ReportsPanel(MainFrame frame) {
        JButton runBtn = AppUI.createPrimaryButton("Run Report");
        JButton clearBtn = AppUI.createSecondaryButton("Clear");
        JButton backBtn = AppUI.createSecondaryButton("Back");

        runBtn.addActionListener(e -> runReport());
        clearBtn.addActionListener(e -> clearResults());
        backBtn.addActionListener(e -> frame.showReportsMenu());

        JPanel formPanel = AppUI.createBodyPanel();
        formPanel.add(reportLabel);
        formPanel.add(Box.createVerticalStrut(12));
        formPanel.add(monthRow);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(yearRow);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(startDateRow);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(endDateRow);

        JPanel body = AppUI.createBodyPanel();
        body.add(formPanel);
        body.add(Box.createVerticalStrut(10));
        body.add(AppUI.createButtonRow(runBtn, clearBtn, backBtn));
        body.add(Box.createVerticalStrut(14));
        body.add(AppUI.wrapTable(resultsTable));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Reports",
                "Run payroll and hiring reports.",
                body));

        prepareForReport(ReportType.PAY_BY_JOB_TITLE);
    }

    public void prepareForReport(ReportType reportType) {
        this.reportType = reportType;
        clearResults();

        switch (reportType) {
            case PAY_BY_JOB_TITLE:
                reportLabel.setText("Total pay for a selected month and year, grouped by job title.");
                monthRow.setVisible(true);
                yearRow.setVisible(true);
                startDateRow.setVisible(false);
                endDateRow.setVisible(false);
                tableModel.setColumnIdentifiers(new Object[]{"Job Title", "Total Pay"});
                break;
            case PAY_BY_DIVISION:
                reportLabel.setText("Total pay for a selected month and year, grouped by division.");
                monthRow.setVisible(true);
                yearRow.setVisible(true);
                startDateRow.setVisible(false);
                endDateRow.setVisible(false);
                tableModel.setColumnIdentifiers(new Object[]{"Division", "Total Pay"});
                break;
            case NEW_HIRES:
                reportLabel.setText("Employees hired within a selected date range.");
                monthRow.setVisible(false);
                yearRow.setVisible(false);
                startDateRow.setVisible(true);
                endDateRow.setVisible(true);
                tableModel.setColumnIdentifiers(new Object[]{"Emp ID", "First Name", "Last Name", "Email", "Hire Date", "Salary"});
                break;
            default:
                break;
        }

        revalidate();
        repaint();
    }

    public void clearResults() {
        monthField.setText("");
        yearField.setText("");
        startDateField.setText("");
        endDateField.setText("");
        tableModel.setRowCount(0);
    }

    private void runReport() {
        tableModel.setRowCount(0);

        try {
            switch (reportType) {
                case PAY_BY_JOB_TITLE:
                    runMonthlySummaryReport(true);
                    break;
                case PAY_BY_DIVISION:
                    runMonthlySummaryReport(false);
                    break;
                case NEW_HIRES:
                    runNewHiresReport();
                    break;
                default:
                    break;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Month and year must use valid numbers.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void runMonthlySummaryReport(boolean byJobTitle) {
        int month = Integer.parseInt(monthField.getText().trim());
        int year = Integer.parseInt(yearField.getText().trim());

        java.util.List<MonthlyPaySummary> rows = byJobTitle
                ? reportService.getTotalPayByJobTitle(month, year)
                : reportService.getTotalPayByDivision(month, year);

        for (MonthlyPaySummary row : rows) {
            tableModel.addRow(new Object[]{
                    row.getLabel(),
                    String.format("$%.2f", row.getTotalPay())
            });
        }

        if (rows.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No report rows were found for that month and year.");
        }
    }

    private void runNewHiresReport() {
        try {
            java.sql.Date.valueOf(startDateField.getText().trim());
            java.sql.Date.valueOf(endDateField.getText().trim());
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Start date and end date must use YYYY-MM-DD format.",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        java.util.List<Employee> employees = reportService.getNewHiresByDateRange(
                startDateField.getText().trim(),
                endDateField.getText().trim());

        for (Employee employee : employees) {
            tableModel.addRow(new Object[]{
                    employee.getEmpId(),
                    employee.getFname(),
                    employee.getLname(),
                    employee.getEmail(),
                    employee.getHireDate(),
                    String.format("$%.2f", employee.getSalary())
            });
        }

        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hires were found for that date range.");
        }
    }

    private JPanel createInputRow(String labelText, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(0, 6));
        row.setOpaque(false);
        row.add(new JLabel(labelText), BorderLayout.NORTH);
        row.add(field, BorderLayout.CENTER);
        return row;
    }
}
