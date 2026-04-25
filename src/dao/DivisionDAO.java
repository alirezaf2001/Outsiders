package dao;

import db.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Division;

public class DivisionDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();
    /**
     * Find division by employee ID
     * @param empId
     * @return Division object if found, otherwise null
     * {@snippet lang="java" :
     * DivisionDAO divisionDAO = new DivisionDAO();
     * Division division = divisionDAO.findDivisionByEmployeeId(1);}
     */
    public Division findDivisionByEmployeeId(int empId) {
        String sql = """
                SELECT d.divID, d.Name, d.city, d.addressLine1, d.addressLine2, d.state, d.country, d.postalCode
                FROM employee_division ed
                JOIN division d ON d.divID = ed.div_ID
                WHERE ed.empid = ?
                LIMIT 1
                """;
        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                return null;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, empId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Division division = new Division();
                    division.setDivID(rs.getInt("divID"));
                    division.setName(rs.getString("Name"));
                    division.setCity(rs.getString("city"));
                    division.setAddressLine1(rs.getString("addressLine1"));
                    division.setAddressLine2(rs.getString("addressLine2"));
                    division.setState(rs.getString("state"));
                    division.setCountry(rs.getString("country"));
                    division.setPostalCode(rs.getString("postalCode"));
                    return division;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding division by employee ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Upsert employee division association
     * @param empId
     * @param divisionId
     * @return boolean indicating success or failure
     * {@snippet lang="java" :
     * DivisionDAO divisionDAO = new DivisionDAO();
     * boolean success = divisionDAO.upsertEmployeeDivision(1, 2);}
     */
    public boolean upsertEmployeeDivision(int empId, int divisionId) {
        String sql = """
                INSERT INTO employee_division (empid, div_ID)
                VALUES (?, ?)
                ON DUPLICATE KEY UPDATE div_ID = VALUES(div_ID)
                """;

        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                return false;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, empId);
                stmt.setInt(2, divisionId);
                return stmt.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error saving employee division: " + e.getMessage());
        }

        return false;
    }

    /**
     * Find division ID by employee ID
     * @param empId
     * @return division ID if found, otherwise null
     * {@snippet lang="java" :
     * DivisionDAO divisionDAO = new DivisionDAO();
     * Integer divisionId = divisionDAO.findDivisionIdByEmployeeId(1);}
     */
    public Integer findDivisionIdByEmployeeId(int empId) {
        Division division = findDivisionByEmployeeId(empId);
        return division != null ? division.getDivID() : null;
    }
}
