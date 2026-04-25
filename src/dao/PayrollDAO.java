package dao;

import db.ConnectionManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
                SELECT payID AS payrollId, empid AS empId, pay_date AS payDate,
                       earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care
                FROM payroll
                WHERE empid = ?
                ORDER BY pay_date DESC
                LIMIT 1
                """;
        try (var conn = connectionManager.getConnection()) {
            if (conn == null) {
                return null;
            }

            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, empId);
                var rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapPayrollRecord(rs);
                }
            }
        } catch (Exception e) {
            System.out.println("Error finding payroll record by employee ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Find payroll history by employee ID
     * @param empId
     * @return List of PayrollRecord objects if found, otherwise empty list
     * {@snippet lang="java" :
     * PayrollDAO payrollDAO = new PayrollDAO();
     * List<PayrollRecord> records = payrollDAO.findPayrollHistoryByEmployeeId(1);
     * }
     */
    public List<PayrollRecord> findPayrollHistoryByEmployeeId(int empId) {
        String sql = """
                SELECT payID AS payrollId, empid AS empId, pay_date AS payDate,
                       earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care
                FROM payroll
                WHERE empid = ?
                ORDER BY pay_date DESC, payID DESC
                """;

        List<PayrollRecord> records = new ArrayList<>();

        try (var conn = connectionManager.getConnection()) {
            if (conn == null) {
                return Collections.emptyList();
            }

            try (var stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, empId);
                var rs = stmt.executeQuery();

                while (rs.next()) {
                    records.add(mapPayrollRecord(rs));
                }
            }
        } catch (Exception e) {
            System.out.println("Error finding payroll history by employee ID: " + e.getMessage());
        }

        return records;
    }

    /**
     * Map a ResultSet row to a PayrollRecord object
     * @param rs
     * @return
     * @throws java.sql.SQLExceptionc
     * {@snippet lang="java" :
     * // This method is used internally by the DAO and is not typically called directly.
     * }
     */
    private PayrollRecord mapPayrollRecord(java.sql.ResultSet rs) throws java.sql.SQLException {
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
}
