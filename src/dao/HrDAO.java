package dao;

import db.ConnectionManager;
import model.HrUser;

public class HrDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public HrUser login(String username, String password) {
        // TODO: Use connectionManager.getConnection() and validate HR user credentials.
        return null;
    }
}
