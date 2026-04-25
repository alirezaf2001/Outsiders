package service;

import dao.DivisionDAO;
import model.Division;
import model.Employee;

public class AuthService {
    // HR division ID is hardcoded for simplicity
    private static final int HR_DIVISION_ID = 3;

    private final DivisionDAO divisionDAO = new DivisionDAO();

    /**
     * Login method that checks if the provided email and password are correct.
     * The password is the last 4 digits of the employee's SSN.
     * @param email
     * @param password
     * @return boolean indicating success or failure of login
     * {@snippet lang="java" :
     * AuthService authService = new AuthService();
     * boolean loggedIn = authService.login("john.doe@example.com", "1234");}
     */
    public boolean login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("Email and password are required.");
            return false;
        }

        Employee employee = new EmployeeService().searchByEmail(email);
        if (employee == null) {
            System.out.println("No employee found with the provided email.");
            return false;
        }

        if (!(employee.getSsn().substring(employee.getSsn().length() - 4)).equals(password)) {
            System.out.println("Incorrect password.");
            return false;
        }

        return true;
    }

    /**
     * Check if the employee belongs to the HR division
     * @param empId
     * @return boolean indicating if the employee is in the HR division
     * {@snippet lang="java" :
     * AuthService authService = new AuthService();
     * boolean isHr = authService.isHrUser(1);
     * }
     */
    public boolean isHrUser(int empId) {
        Division division = divisionDAO.findDivisionByEmployeeId(empId);
        if (division == null) {
            System.out.println("No division found for the employee.");
            return false;
        }
        return division.getDivID() == HR_DIVISION_ID;
    }
}
