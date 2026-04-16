package service;

import dao.HrDAO;
import model.HrUser;

public class AuthService {
    private final HrDAO hrDAO = new HrDAO();

    public boolean login(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            System.out.println("Username and password are required.");
            return false;
        }

        HrUser hrUser = hrDAO.findByUsername(username);

        if (hrUser == null) {
            // TODO: Replace this with real authentication logic.
            return false;
        }

        return password.equals(hrUser.getPassword());
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
