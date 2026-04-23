package dao;

import db.ConnectionManager;
import model.PayrollRecord;

public class PayrollDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

        /**
        * Find payroll record by employee ID
        * @param empId
        * @return PayrollRecord object if found, otherwise null
        * {@snippet lang="java" :
        * PayrollDAO payrollDAO = new PayrollDAO();
        * PayrollRecord record = payrollDAO.findByEmployeeId(1);
        * }
        */
    public PayrollRecord findByEmployeeId(int empId) {
        String sql = """
                SELECT payrollId, empId, payDate, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care
                FROM payroll
                WHERE empId = ?
                """;
        try (var conn = connectionManager.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                PayrollRecord record = new PayrollRecord();
                record.setPayrollId(rs.getInt("payrollId"));
                record.setEmpId(rs.getInt("empId"));
                record.setPayDate(rs.getString("payDate"));
                record.setEarnings(rs.getDouble("earnings"));
                record.setFed_tax(rs.getDouble("fed_tax"));
                record.setFed_med(rs.getDouble("fed_med"));
                record.setFed_SS(rs.getDouble("fed_SS"));
                record.setState_tax(rs.getDouble("state_tax"));
                record.setRetire_401k(rs.getDouble("retire_401k"));
                record.setHealth_care(rs.getDouble("health_care"));
                return record;
            }
        } catch (Exception e) {
            System.out.println("Error finding payroll record by employee ID: " + e.getMessage());
        }
        return null;
    }
}
