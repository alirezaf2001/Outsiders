package gui_panels;

import Main_gui.MainFrame;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Employee;
import service.EmployeeService;


/* 
This panel provides a form for HR admins to add new employees to the system.
It includes fields for all required employee information and validates input
before attempting to add the employee to the database.
The form is designed to be straightforward and aligned with the current database schema, 
with notes to guide HR users on how to fill in certain fields.
*/
public class AddEmployeePanel extends JPanel {
    private final EmployeeService employeeService = new EmployeeService();
    private final JTextField firstNameField = AppUI.createTextField(20);
    private final JTextField lastNameField = AppUI.createTextField(20);
    private final JTextField emailField = AppUI.createTextField(20);
    private final JTextField hireDateField = AppUI.createTextField(20);
    private final JTextField salaryField = AppUI.createTextField(20);
    private final JTextField ssnField = AppUI.createTextField(20);
    private final JTextField addressIdField = AppUI.createTextField(20);
    private final JTextField divisionIdField = AppUI.createTextField(20);

    public AddEmployeePanel(MainFrame frame) {
        JButton saveBtn = AppUI.createPrimaryButton("Add Employee");
        JButton clearBtn = AppUI.createSecondaryButton("Clear");
        JButton backBtn = AppUI.createSecondaryButton("Back");

        saveBtn.addActionListener(e -> addEmployee());
        clearBtn.addActionListener(e -> resetForm());
        backBtn.addActionListener(e -> frame.showHrEmployeeManagementMenu());

        JPanel formPanel = AppUI.createFormPanel();
        AppUI.addFormRow(formPanel, 0, "First Name", firstNameField);
        AppUI.addFormRow(formPanel, 1, "Last Name", lastNameField);
        AppUI.addFormRow(formPanel, 2, "Email", emailField);
        AppUI.addFormRow(formPanel, 3, "Hire Date (YYYY-MM-DD)", hireDateField);
        AppUI.addFormRow(formPanel, 4, "Salary", salaryField);
        AppUI.addFormRow(formPanel, 5, "SSN", ssnField);
        AppUI.addFormRow(formPanel, 6, "Address ID", addressIdField);
        AppUI.addFormRow(formPanel, 7, "Division ID", divisionIdField);

        JPanel body = AppUI.createBodyPanel();
        JLabel idNote = AppUI.createInfoLabel(
                "Employee ID is assigned from the next available empID because the current schema is not auto-increment.");
        JLabel addressNote = AppUI.createInfoLabel(
                "Address ID must already exist in the addresses table. Full address insertion is left as a TODO.");
        body.add(idNote);
        body.add(Box.createVerticalStrut(6));
        body.add(addressNote);
        body.add(Box.createVerticalStrut(14));
        body.add(formPanel);
        body.add(Box.createVerticalStrut(8));
        body.add(AppUI.createButtonRow(saveBtn, clearBtn, backBtn));

        setLayout(new BorderLayout());
        add(AppUI.createScreenShell(
                "Add New Employee",
                "Keep employee insertion simple and aligned with the current schema.",
                body));
    }

    public void resetForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        hireDateField.setText("");
        salaryField.setText("");
        ssnField.setText("");
        addressIdField.setText("");
        divisionIdField.setText("");
    }

    private void addEmployee() {
        try {
            double salary = Double.parseDouble(salaryField.getText().trim());
            int addressId = Integer.parseInt(addressIdField.getText().trim());
            int divisionId = Integer.parseInt(divisionIdField.getText().trim());
            java.sql.Date.valueOf(hireDateField.getText().trim());

            Employee employee = new Employee(
                    0,
                    firstNameField.getText().trim(),
                    lastNameField.getText().trim(),
                    emailField.getText().trim(),
                    hireDateField.getText().trim(),
                    salary,
                    ssnField.getText().trim(),
                    addressId);

            boolean added = employeeService.addEmployee(employee, divisionId);
            if (!added) {
                JOptionPane.showMessageDialog(
                        this,
                        "Employee could not be added. Check the database values and try again.",
                        "Add Employee",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(
                    this,
                    "Employee added successfully with empID " + employee.getEmpId() + ".");
            resetForm();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Salary, address ID, and division ID must use valid numbers.",
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
}
