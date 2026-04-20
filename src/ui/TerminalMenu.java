package ui;

import java.util.Scanner;

import db.ConnectionManager;
import model.Employee;
import model.PayrollRecord;
import service.AuthService;
import service.EmployeeService;
import service.PayrollService;
import util.InputHandler;

public class TerminalMenu {
    private final Scanner scanner;
    private final InputHandler inputHandler;
    private final ConnectionManager connectionManager;
    private final AuthService authService;
    private final EmployeeService employeeService;
    private final PayrollService payrollService;

    public TerminalMenu() {
        this.scanner = new Scanner(System.in);
        this.inputHandler = new InputHandler(scanner);
        this.connectionManager = new ConnectionManager();
        this.authService = new AuthService();
        this.employeeService = new EmployeeService();
        this.payrollService = new PayrollService();
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = inputHandler.readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleSearchEmployee();
                    break;
                case 3:
                    handleUpdateEmployee();
                    break;
                case 4:
                    handleUpdateSalary();
                    break;
                case 5:
                    handleViewPayroll();
                    break;
                case 6:
                    handleTestDatabaseConnection();
                    break;
                case 7:
                    running = false;
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }

            System.out.println();
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println("=== HR Management Menu ===");
        System.out.println("1. Login");
        System.out.println("2. Search Employee");
        System.out.println("3. Update Employee");
        System.out.println("4. Update Salary");
        System.out.println("5. View Payroll");
        System.out.println("6. Test Database Connection");
        System.out.println("7. Exit");
    }

    private void handleLogin() {
        String username = inputHandler.readString("Username: ");
        String password = inputHandler.readString("Password: ");

        boolean success = authService.login(username, password);
        if (success) {
            System.out.println("Login successful.");
        } else {
            System.out.println("Login is not fully implemented yet.");
        }
    }

    private void handleSearchEmployee() {
        int empId = inputHandler.readInt("Employee ID: ");
        Employee employee = employeeService.searchEmployeeById(empId);

        if (employee == null) {
            System.out.println("No employee found. Search logic still needs implementation.");
        } else {
            System.out.println(employee);
        }
    }

    private void handleUpdateEmployee() {
        int empId = inputHandler.readInt("Employee ID: ");
        String fname = inputHandler.readString("First name: ");
        String lname = inputHandler.readString("Last name: ");
        String email = inputHandler.readString("Email: ");
        String hireDate = inputHandler.readString("Hire date (YYYY-MM-DD): ");
        double salary = inputHandler.readDouble("Salary: ");
        String ssn = inputHandler.readString("SSN: ");
        int addressId = inputHandler.readInt("Address ID: ");

        Employee employee = new Employee(empId, fname, lname, email, hireDate, salary, ssn, addressId);
        boolean updated = employeeService.updateEmployee(employee);

        if (updated) {
            System.out.println("Employee updated.");
        } else {
            System.out.println("Update is not fully implemented yet.");
        }
    }

    private void handleUpdateSalary() {
        int empId = inputHandler.readInt("Employee ID: ");
        double salary = inputHandler.readDouble("New salary: ");

        boolean updated = employeeService.updateSalary(empId, salary);

        if (updated) {
            System.out.println("Salary updated.");
        } else {
            System.out.println("Salary update is not fully implemented yet.");
        }
    }

    private void handleViewPayroll() {
        int empId = inputHandler.readInt("Employee ID: ");
        PayrollRecord payrollRecord = payrollService.getPayrollByEmployeeId(empId);

        if (payrollRecord == null) {
            System.out.println("No payroll record found. Payroll logic still needs implementation.");
        } else {
            System.out.println(payrollRecord);
        }
    }

    private void handleTestDatabaseConnection() {
        boolean connected = connectionManager.testConnection();

        if (connected) {
            System.out.println("Database connection successful.");
        } else {
            System.out.println("Database connection test failed.");
        }
    }
}
