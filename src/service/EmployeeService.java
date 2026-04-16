package service;

import dao.EmployeeDAO;
import model.Employee;

public class EmployeeService {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public Employee searchEmployeeById(int employeeId) {
        if (employeeId <= 0) {
            System.out.println("Employee ID must be greater than 0.");
            return null;
        }

        return employeeDAO.findById(employeeId);
    }

    public boolean updateEmployee(Employee employee) {
        if (employee == null || employee.getId() <= 0) {
            System.out.println("Employee data is not valid.");
            return false;
        }

        return employeeDAO.updateEmployee(employee);
    }
}
