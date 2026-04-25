package service;

import dao.AddressDAO;
import dao.DivisionDAO;
import dao.EmployeeDAO;
import model.Address;
import model.Division;
import model.Employee;
import java.util.Collections;
import java.util.List;

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

    public List<Employee> searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Name is required.");
            return Collections.emptyList();
        }

        return employeeDAO.searchByName(name.trim());
    }

    public List<Employee> searchByDOB(String dob) {
        if (dob == null || dob.trim().isEmpty()) {
            System.out.println("Date of birth is required.");
            return Collections.emptyList();
        }

        return employeeDAO.searchByDOB(dob.trim());
    }

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

    public boolean updateEmployee(Employee employee, int divisionId) {
        if (!updateEmployee(employee)) {
            return false;
        }

        return divisionDAO.upsertEmployeeDivision(employee.getEmpId(), divisionId);
    }

    public boolean addEmployee(Employee employee) {
        if (employee == null) {
            System.out.println("Employee data is required.");
            return false;
        }

        return employeeDAO.addEmployee(employee);
    }

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

    public Address getAddressByEmployeeId(int empId) {
        Employee employee = searchEmployeeById(empId);
        if (employee == null) {
            return null;
        }

        return addressDAO.findById(employee.getAddressId());
    }

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
