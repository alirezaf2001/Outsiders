package gui_panels;

import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import model.Address;
import model.Division;
import model.Employee;
import service.EmployeeService;

public class EmployeeDetailsPanel extends JPanel {
    private final MainFrame frame;
    private final EmployeeService employeeService = new EmployeeService();
    private final JLabel modeLabel = AppUI.createInfoLabel("");
    private final JTextField empIdField = AppUI.createReadOnlyField(20);
    private final JTextField firstNameField = AppUI.createTextField(20);
    private final JTextField lastNameField = AppUI.createTextField(20);
    private final JTextField emailField = AppUI.createTextField(20);
    private final JTextField hireDateField = AppUI.createTextField(20);
    private final JTextField salaryField = AppUI.createTextField(20);
    private final JTextField ssnField = AppUI.createTextField(20);
    private final JTextField addressIdField = AppUI.createTextField(20);
    private final JTextField divisionIdField = AppUI.createTextField(20);
    private final JTextField divisionNameField = AppUI.createReadOnlyField(20);
    private final JTextField dobField = AppUI.createReadOnlyField(20);
    private final JTextField phoneField = AppUI.createReadOnlyField(20);
    private final JButton saveBtn = AppUI.createPrimaryButton("Save Changes");

    private boolean editable;

    public EmployeeDetailsPanel(MainFrame frame) {
        this.frame = frame;

        JButton backBtn = AppUI.createSecondaryButton("Back");
        saveBtn.addActionListener(e -> saveEmployee());
        backBtn.addActionListener(e -> goBack());

        JPanel formPanel = AppUI.createFormPanel();
        AppUI.addFormRow(formPanel, 0, "Employee ID", empIdField);
        AppUI.addFormRow(formPanel, 1, "First Name", firstNameField);
        AppUI.addFormRow(formPanel, 2, "Last Name", lastNameField);
        AppUI.addFormRow(formPanel, 3, "Email", emailField);
        AppUI.addFormRow(formPanel, 4, "Hire Date (YYYY-MM-DD)", hireDateField);
        AppUI.addFormRow(formPanel, 5, "Salary", salaryField);
        AppUI.addFormRow(formPanel, 6, "SSN", ssnField);
        AppUI.addFormRow(formPanel, 7, "Address ID", addressIdField);
        AppUI.addFormRow(formPanel, 8, "Division ID", divisionIdField);
        AppUI.addFormRow(formPanel, 9, "Division Name", divisionNameField);
        AppUI.addFormRow(formPanel, 10, "DOB", dobField);
        AppUI.addFormRow(formPanel, 11, "Phone", phoneField);

        JPanel body = AppUI.createBodyPanel();
        body.add(modeLabel);
        body.add(Box.createVerticalStrut(14));
        body.add(formPanel);
        body.add(Box.createVerticalStrut(8));
        body.add(AppUI.createButtonRow(saveBtn, backBtn));

        JScrollPane bodyScrollPane = new JScrollPane(body);
        bodyScrollPane.setBorder(null);
        bodyScrollPane.setAlignmentX(LEFT_ALIGNMENT);
        bodyScrollPane.setOpaque(false);
        bodyScrollPane.getViewport().setOpaque(false);
        bodyScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Employee Details",
                "View employee information and update it when editing is available.",
                bodyScrollPane));

        clearView();
    }

    public boolean loadEmployee(int empId, boolean editable) {
        Employee employee = employeeService.searchEmployeeById(empId);
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Employee not found.", "Employee Details", JOptionPane.ERROR_MESSAGE);
            clearView();
            return false;
        }

        Address address = employeeService.getAddressByEmployeeId(empId);
        Division division = employeeService.getDivisionByEmployeeId(empId);

        this.editable = editable && frame.getSession().isHrAdmin();

        empIdField.setText(String.valueOf(employee.getEmpId()));
        firstNameField.setText(employee.getFname());
        lastNameField.setText(employee.getLname());
        emailField.setText(employee.getEmail());
        hireDateField.setText(employee.getHireDate());
        salaryField.setText(String.valueOf(employee.getSalary()));
        ssnField.setText(employee.getSsn());
        addressIdField.setText(String.valueOf(employee.getAddressId()));
        divisionIdField.setText(division != null ? String.valueOf(division.getDivID()) : "");
        divisionNameField.setText(division != null ? division.getName() : "");
        dobField.setText(address != null ? address.getDOB() : "");
        phoneField.setText(address != null ? address.getPhone() : "");

        setEditableFields(this.editable);
        saveBtn.setVisible(this.editable);
        modeLabel.setText(this.editable
                ? "Edit the employee information and save your changes."
                : "This record is view-only.");
        return true;
    }

    public void clearView() {
        empIdField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        hireDateField.setText("");
        salaryField.setText("");
        ssnField.setText("");
        addressIdField.setText("");
        divisionIdField.setText("");
        divisionNameField.setText("");
        dobField.setText("");
        phoneField.setText("");
        setEditableFields(false);
        saveBtn.setVisible(false);
        modeLabel.setText("Load an employee record to view details.");
    }

    private void saveEmployee() {
        try {
            int empId = Integer.parseInt(empIdField.getText().trim());
            int addressId = Integer.parseInt(addressIdField.getText().trim());
            int divisionId = Integer.parseInt(divisionIdField.getText().trim());
            double salary = Double.parseDouble(salaryField.getText().trim());
            java.sql.Date.valueOf(hireDateField.getText().trim());

            Employee employee = new Employee(
                    empId,
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    emailField.getText().trim(),
                    hireDateField.getText().trim(),
                    salary,
                    ssnField.getText().trim(),
                    addressId);

            boolean updated = employeeService.updateEmployee(employee, divisionId);
            if (!updated) {
                JOptionPane.showMessageDialog(
                        this,
                        "Employee update failed. Check the database values and try again.",
                        "Update Failed",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            frame.refreshSessionEmployee(employee);
            JOptionPane.showMessageDialog(this, "Employee updated successfully.");
            loadEmployee(empId, true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Employee ID, address ID, division ID, and salary must use valid numbers.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Hire date must use YYYY-MM-DD format.",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setEditableFields(boolean editable) {
        firstNameField.setEditable(editable);
        lastNameField.setEditable(editable);
        emailField.setEditable(editable);
        hireDateField.setEditable(editable);
        salaryField.setEditable(editable);
        ssnField.setEditable(editable);
        addressIdField.setEditable(editable);
        divisionIdField.setEditable(editable);
        divisionNameField.setEditable(false);
        dobField.setEditable(false);
        phoneField.setEditable(false);
    }

    private void goBack() {
        if (frame.getSession().isHrAdmin()) {
            frame.showHrEmployeeManagementMenu();
        } else {
            frame.showEmployeeMenu();
        }
    }
}
