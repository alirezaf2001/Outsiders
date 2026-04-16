package dao;

import db.ConnectionManager;
import model.PayrollRecord;

public class PayrollDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public PayrollRecord findByEmployeeId(int employeeId) {
        // TODO: Use connectionManager.getConnection() and add a SELECT query.
        return null;
    }
}
