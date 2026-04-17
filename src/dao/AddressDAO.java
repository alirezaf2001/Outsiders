package dao;

import db.ConnectionManager;
import model.Address;

public class AddressDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public Address findById(int addressId) {
        // TODO: Use connectionManager.getConnection() and query the addresses table by addressId.
        return null;
    }
}
