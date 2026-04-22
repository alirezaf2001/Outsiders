package dao;

import db.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Division;

public class DivisionDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

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
}
