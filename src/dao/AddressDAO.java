package dao;

import db.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Address;

public class AddressDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    /**
     * Find address by ID
     * @param addressId
     * @return Address object if found, otherwise null
     * {@snippet lang="java" :
     * AddressDAO addressDAO = new AddressDAO();
     * Address address = addressDAO.findById(1);
     * }
     */
    public Address findById(int addressId) {
        String sql = """
                SELECT addressID, street, cityID, stateID, zip, DOB, phone,
                       emergency_contact AS emergencyContact,
                       emergency_phone AS emergencyPhone
                FROM addresses
                WHERE addressID = ?
                LIMIT 1
                """;
        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                return null;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, addressId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    Address address = new Address();
                    address.setAddressID(rs.getInt("addressID"));
                    address.setStreet(rs.getString("street"));
                    address.setCityID(rs.getInt("cityID"));
                    address.setStateID(rs.getInt("stateID"));
                    address.setZip(rs.getString("zip"));
                    address.setDOB(rs.getString("DOB"));
                    address.setPhone(rs.getString("phone"));
                    address.setEmergencyContact(rs.getString("emergencyContact"));
                    address.setEmergencyPhone(rs.getString("emergencyPhone"));
                    return address;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding address by ID: " + e.getMessage());
        }
        return null;
    }
}
