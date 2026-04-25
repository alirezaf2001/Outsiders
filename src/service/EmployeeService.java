package service;

import dao.AddressDAO;
import dao.DivisionDAO;
import dao.EmployeeDAO;
import java.util.Collections;
import java.util.List;
import model.Address;
import model.Division;
import model.Employee;

public class EmployeeService {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final DivisionDAO divisionDAO = new DivisionDAO();

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
     * Search employees by name (first or last)
     * @param name
     * @return List of Employee objects matching the name, or empty list if none found
     * {@snippet lang="java" :
     * EmployeeService employeeService = new EmployeeService();
     * List<Employee> employees = employeeService.searchByName("John");}
     */
    public List<Employee> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name is required.");
            return Collections.emptyList();
        }

        return employeeDAO.searchByName(name.trim());
    }

    /**
     * Search employees by date of birth
     * @param dob in YYYY-MM-DD format
     * @return List of Employee objects matching the date of birth, or empty list if none found
     * {@snippet lang="java" :
     * EmployeeService employeeService = new EmployeeService();
     * List<Employee> employees = employeeService.searchByDOB("1990-01-01");}
     */
    public List<Employee> searchByDOB(String dob) {
        if (dob == null || dob.trim().isEmpty()) {
            System.out.println("Date of birth is required.");
            return Collections.emptyList();
        }

        return employeeDAO.searchByDOB(dob.trim());
    }

    /**
     * Search employees by SSN
     * @param ssn
     * @return List of Employee objects matching the SSN, or empty list if none found
     * {@snippet lang="java" :
     * EmployeeService employeeService = new EmployeeService();
     * List<Employee> employees = employeeService.searchBySSN("123-45-6789");}
     */
    public List<Employee> searchBySSN(String ssn) {
        if (ssn == null || ssn.trim().isEmpty()) {
            System.out.println("SSN is required.");
            return Collections.emptyList();
        }

        return employeeDAO.searchBySSN(ssn.trim());
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
     * Update employee information along with their division
     * @param employee
     * @param divisionId
     * @return boolean indicating success or failure
     * {@snippet lang="java" :
     *  EmployeeService employeeService = new EmployeeService();
     *  Employee employee = employeeService.searchEmployeeById(1);
     *  employee.setEmail("jane.doe@example.com");
     *  employeeService.updateEmployee(employee, 1);}
     */
    public boolean updateEmployee(Employee employee, int divisionId) {
        if (!updateEmployee(employee)) {
            return false;
        }

        return divisionDAO.upsertEmployeeDivision(employee.getEmpId(), divisionId);
    }

    /**
     * Add new employee
     * @param employee
     * @return boolean indicating success or failure
     * {@snippet lang="java" :
     * EmployeeService employeeService = new EmployeeService();
     * Employee newEmployee = new Employee(0, "Alice", "Smith", "alice.smith@example.com");
     * boolean added = employeeService.addEmployee(newEmployee);}
     */
    public boolean addEmployee(Employee employee) {
        if (employee == null) {
            System.out.println("Employee data is required.");
            return false;
        }

        return employeeDAO.addEmployee(employee);
    }

    /**
     * Add new employee along with their division
     * @param employee
     * @param divisionId
     * @return boolean indicating success or failure
     * {@snippet lang="java" :
     * EmployeeService employeeService = new EmployeeService();
     * Employee newEmployee = new Employee(0, "Alice", "Smith", "alice.smith@example.com");
     * boolean added = employeeService.addEmployee(newEmployee, 1);}
     */
    public boolean addEmployee(Employee employee, int divisionId) {
        if (!addEmployee(employee)) {
            return false;
        }

        boolean divisionSaved = divisionDAO.upsertEmployeeDivision(employee.getEmpId(), divisionId);
        if (!divisionSaved) {
            employeeDAO.deleteEmployee(employee.getEmpId());
            return false;
        }

        return true;
    }

        /**
        * Delete employee by ID
        * @param empId
        * @return boolean indicating success or failure
        * {@snippet lang="java" :
        * EmployeeService employeeService = new EmployeeService();
        * boolean deleted = employeeService.deleteEmployee(1);}
        */
    public boolean deleteEmployee(int empId) {
        if (empId <= 0) {
            System.out.println("Employee ID must be greater than 0.");
            return false;
        }

        return employeeDAO.deleteEmployee(empId);
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
     * Update salaries for employees within a specified salary range by a given percentage
     * @param minSalary minimum salary (inclusive)
     * @param maxSalary maximum salary (inclusive)
     * @param percentage percentage increase (e.g., 10 for 10% increase)
     * @return number of employees whose salaries were updated
     * {@snippet lang="java" :
     * EmployeeService employeeService = new EmployeeService();
     * int updatedCount = employeeService.updateSalariesByRange(40000.0, 60000.0, 10.0);}
     */
    public int updateSalariesByRange(double minSalary, double maxSalary, double percentage) {
        if (minSalary < 0 || maxSalary < 0 || maxSalary < minSalary) {
            System.out.println("Salary range is not valid.");
            return 0;
        }

        if (percentage <= 0) {
            System.out.println("Percentage increase must be positive.");
            return 0;
        }

        return employeeDAO.updateSalariesByRange(minSalary, maxSalary, percentage);
    }

    // Helper methods to get related information
    public Address getAddressByEmployeeId(int empId) {
        Employee employee = searchEmployeeById(empId);
        if (employee == null) {
            return null;
        }

        return addressDAO.findById(employee.getAddressId());
    }
    // Helper method to get division information for an employee
    public Division getDivisionByEmployeeId(int empId) {
        return divisionDAO.findDivisionByEmployeeId(empId);
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
