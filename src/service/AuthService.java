package service;

import dao.DivisionDAO;
import model.Division;
import model.Employee;

public class AuthService {
    private static final int HR_DIVISION_ID = 3; //set HR Division ID

    private final DivisionDAO divisionDAO = new DivisionDAO();

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

    public boolean isHrUser(int empId) {
        Division division = divisionDAO.findDivisionByEmployeeId(empId);
        if (division == null) {
            System.out.println("No division found for the employee.");
            return false;
        }
        return division.getDivID() == HR_DIVISION_ID;
    }
}
