package gui_panels;

import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import service.EmployeeService;

public class SalaryUpdatePanel extends JPanel {
    private final EmployeeService employeeService = new EmployeeService();
    private final JTextField minSalaryField = AppUI.createTextField(20);
    private final JTextField maxSalaryField = AppUI.createTextField(20);
    private final JTextField percentageField = AppUI.createTextField(20);
    private final JTextArea resultArea = AppUI.createOutputArea(
            6,
            30,
            "Run a salary update to see how many employee records were affected.");

    public SalaryUpdatePanel(MainFrame frame) {
        JButton applyBtn = AppUI.createPrimaryButton("Apply Update");
        JButton clearBtn = AppUI.createSecondaryButton("Clear");
        JButton backBtn = AppUI.createSecondaryButton("Back");

        applyBtn.addActionListener(e -> applyUpdate());
        clearBtn.addActionListener(e -> resetForm());
        backBtn.addActionListener(e -> frame.showHrPayrollMenu());

        JPanel formPanel = AppUI.createFormPanel();
        AppUI.addFormRow(formPanel, 0, "Minimum Salary", minSalaryField);
        AppUI.addFormRow(formPanel, 1, "Maximum Salary", maxSalaryField);
        AppUI.addFormRow(formPanel, 2, "Percentage Increase", percentageField);

        JPanel body = AppUI.createBodyPanel();
        body.add(formPanel);
        body.add(Box.createVerticalStrut(8));
        body.add(AppUI.createButtonRow(applyBtn, clearBtn, backBtn));
        body.add(Box.createVerticalStrut(14));
        body.add(AppUI.createInfoLabel("Update Result"));
        body.add(Box.createVerticalStrut(8));
        body.add(AppUI.wrapTextArea(resultArea));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Update Salaries by Percentage and Range",
                "Only employees whose salary falls inside the range will be updated.",
                body));
    }

    public void resetForm() {
        minSalaryField.setText("");
        maxSalaryField.setText("");
        percentageField.setText("");
        resultArea.setText("Run a salary update to see how many employee records were affected.");
    }

    private void applyUpdate() {
        try {
            double minSalary = Double.parseDouble(minSalaryField.getText().trim());
            double maxSalary = Double.parseDouble(maxSalaryField.getText().trim());
            double percentage = Double.parseDouble(percentageField.getText().trim());

            if (percentage <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Percentage increase must be positive.",
                        "Invalid Percentage",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Apply a " + percentage + "% increase to salaries between "
                            + minSalary + " and " + maxSalary + "?",
                    "Confirm Salary Update",
                    JOptionPane.YES_NO_OPTION);

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            int updatedCount = employeeService.updateSalariesByRange(minSalary, maxSalary, percentage);
            resultArea.setText("Updated " + updatedCount + " employee record(s).");
            JOptionPane.showMessageDialog(this, "Salary update completed for " + updatedCount + " employee(s).");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Minimum salary, maximum salary, and percentage must use valid numbers.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
