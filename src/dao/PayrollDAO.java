package dao;

import db.ConnectionManager;
import model.PayrollRecord;

public class PayrollDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public PayrollRecord findByEmployeeId(int empId) {
        // TODO: Use connectionManager.getConnection() and query the payroll table by empId.
        return null;
    }
}
