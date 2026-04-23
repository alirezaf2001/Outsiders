package service;

import dao.AddressDAO;
import dao.EmployeeDAO;
import model.Address;
import model.Employee;

public class EmployeeService {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final AddressDAO addressDAO = new AddressDAO();

    /**
     * Search employee by ID
     * @param empId
     * @return Employee object if found, otherwise null
     * {@snippet lang="java" :
     * EmployeeService employeeService = new EmployeeService();
     * Employee employee = employeeService.searchEmployeeById(1);
     * }
     */
    public Employee searchEmployeeById(int empId) {
        if (empId <= 0) {
            System.out.println("Employee ID must be greater than 0.");
            return null;
        }

        return employeeDAO.findById(empId);
    }

    // If to be implemented for searching by last name. output is list of the employees with the same last name.
    // public List<Employee> searchByLastName(String lname) {
    //     if (lname == null || lname.trim().isEmpty()) {
    //         System.out.println("Last name is required.");
    //         return List.of();
    //     }

    //     return employeeDAO.searchByLastName(lname);
    // }

    /**
     * Search employee by email
     * @param email
     * @return Employee object if found, otherwise null
     * {@snippet lang="java" :
     * EmployeeService employeeService = new EmployeeService();
     * Employee employee = employeeService.searchByEmail("john.doe@example.com");}
     */
    public Employee searchByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email is required.");
            return null;
        }

        return employeeDAO.searchByEmail(email);
    }

    /**
     * Update employee information
     * @param employee
     * @return boolean indicating success or failure
     * {@snippet lang="java" :
     *  EmployeeService employeeService = new EmployeeService();
     *  Employee employee = employeeService.searchEmployeeById(1);
     *  employee.setEmail("jane.doe@example.com");
     *  employeeService.updateEmployee(employee);}
     */
    public boolean updateEmployee(Employee employee) {
        if (employee == null || employee.getEmpId() <= 0) {
            System.out.println("Employee data is not valid.");
            return false;
        }

        return employeeDAO.updateEmployee(employee);
    }
    /**
     * Update employee salary
     * @param empId
     * @param salary
     * @return boolean indicating success or failure
     * {@snippet lang="java" :
     * EmployeeService employeeService = new EmployeeService();
     * boolean updated = employeeService.updateSalary(1, 50000.0);}
     */
    public boolean updateSalary(int empId, double salary) {
        if (empId <= 0) {
            System.out.println("Employee ID must be greater than 0.");
            return false;
        }

        if (salary < 0) {
            System.out.println("Salary cannot be negative.");
            return false;
        }

        return employeeDAO.updateSalary(empId, salary);
    }

    /**
     * Get employee information along with their address
     * @param empId
     * @return String containing employee and address information, or null if employee not found
     * {@snippet lang="java" :
     * EmployeeService employeeService = new EmployeeService();
     * String info = employeeService.getEmployeeWithAddress(1);
     * System.out.println(info);}
     */
    public String getEmployeeWithAddress(int empId) {
        if (empId <= 0) {
            System.out.println("Employee ID must be greater than 0.");
            return null;
        }

        Employee employee = employeeDAO.findById(empId);
        if (employee == null) {
            return null;
        }

        Address address = addressDAO.findById(employee.getAddressId());
        if (address == null) {
            return employee.toString();
        }

        return employee + System.lineSeparator() + address;
    }
}
