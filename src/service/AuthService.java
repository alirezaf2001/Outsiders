package service;

import dao.DivisionDAO;
import model.Division;

public class AuthService {
    private static final int HR_DIVISION_ID = 3;

    private final DivisionDAO divisionDAO = new DivisionDAO();

    public boolean login(int empId) {
        if (empId <= 0) {
            System.out.println("Employee ID is required.");
            return false;
        }

        Division division = divisionDAO.findDivisionByEmployeeId(empId);
        return division != null && division.getDivID() == HR_DIVISION_ID;
    }
}
