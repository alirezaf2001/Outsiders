package dao;

import db.ConnectionManager;
import model.HrUser;

public class HrDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public HrUser findByUsername(String username) {
        // TODO: Use connectionManager.getConnection() and add a SELECT query.
        return null;
    }
}
