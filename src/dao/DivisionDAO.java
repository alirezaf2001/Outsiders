package dao;

import db.ConnectionManager;
import model.Division;

public class DivisionDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public Division findDivisionByEmployeeId(int empId) {
        // TODO: Use connectionManager.getConnection() and query division via employee_division.
        return null;
    }
}
