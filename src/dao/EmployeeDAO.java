package dao;

import db.ConnectionManager;
import model.Employee;

public class EmployeeDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public Employee findById(int employeeId) {
        // TODO: Use connectionManager.getConnection() and add a SELECT query.
        return null;
    }

    public boolean updateEmployee(Employee employee) {
        // TODO: Use connectionManager.getConnection() and add an UPDATE query.
        return false;
    }
}
