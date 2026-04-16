package ui;

import java.util.Scanner;
import model.Employee;
import model.PayrollRecord;
import service.AuthService;
import service.EmployeeService;
import service.PayrollService;
import util.InputHandler;

/*
Temp terminal based UI
App must be ready to be implemented with simple GUI
Plan: using swing GUI
*/ 

public class TerminalMenu {
    private final Scanner scanner;
    private final InputHandler inputHandler;
    private final AuthService authService;
    private final EmployeeService employeeService;
    private final PayrollService payrollService;

    public TerminalMenu() {
        this.scanner = new Scanner(System.in);
        this.inputHandler = new InputHandler(scanner);
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
                case 1 -> handleLogin();
                case 2 -> handleSearchEmployee();
                case 3 -> handleUpdateEmployee();
                case 4 -> handleViewPayroll();
                case 5 -> {
                    running = false;
                    System.out.println("Exiting program.");
                }
                default -> System.out.println("Invalid option. Please try again.");
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
        System.out.println("4. View Payroll");
        System.out.println("5. Exit");
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
        int employeeId = inputHandler.readInt("Employee ID: ");
        Employee employee = employeeService.searchEmployeeById(employeeId);

        if (employee == null) {
            System.out.println("No employee found. Search logic still needs implementation.");
        } else {
            System.out.println(employee);
        }
    }

    private void handleUpdateEmployee() {
        int employeeId = inputHandler.readInt("Employee ID: ");
        String firstName = inputHandler.readString("First name: ");
        String lastName = inputHandler.readString("Last name: ");
        String department = inputHandler.readString("Department: ");
        double salary = inputHandler.readDouble("Salary: ");

        Employee employee = new Employee(employeeId, firstName, lastName, department, salary);
        boolean updated = employeeService.updateEmployee(employee);

        if (updated) {
            System.out.println("Employee updated.");
        } else {
            System.out.println("Update is not fully implemented yet.");
        }
    }

    private void handleViewPayroll() {
        int employeeId = inputHandler.readInt("Employee ID: ");
        PayrollRecord payrollRecord = payrollService.getPayrollByEmployeeId(employeeId);

        if (payrollRecord == null) {
            System.out.println("No payroll record found. Payroll logic still needs implementation.");
        } else {
            System.out.println(payrollRecord);
        }
    }
}
