package service;

import dao.HrDAO;

public class AuthService {
    private final HrDAO hrDAO = new HrDAO();

    public boolean login(String username, String password) {
        if (isBlank(username) || isBlank(password)) {
            System.out.println("Username and password are required.");
            return false;
        }

        return hrDAO.login(username, password) != null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
