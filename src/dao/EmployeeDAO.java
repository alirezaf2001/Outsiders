package dao;

import java.util.ArrayList;
import java.util.List;

import db.ConnectionManager;
import model.Employee;

public class EmployeeDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public Employee findById(int empId) {
        // TODO: Use connectionManager.getConnection() and query the employees table by empId.
        return null;
    }

    public List<Employee> searchByLastName(String lname) {
        // TODO: Query the employees table by lname and return matching records.
        return new ArrayList<>();
    }

    public boolean updateEmployee(Employee employee) {
        // TODO: Update the employee row using the fields in the Employee model.
        return false;
    }

    public boolean updateSalary(int empId, double salary) {
        // TODO: Update only the salary column for the given empId.
        return false;
    }
}
