package dao;

import db.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Employee;

public class EmployeeDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    /**
    Find employee by ID 
    Input: empId (int)
    Output: Employee object if found, otherwise null
    Example usage:
    EmployeeDAO employeeDAO = new EmployeeDAO();
    Employee emp = employeeDAO.findById(1);
    */
    public Employee findById(int empId) {
        String sql = """
                SELECT empid, Fname, Lname, email, HireDate, addressid, salary, SSN, addressID
                FROM employees
                WHERE empid = ?
                """;
        try (Connection conn = connectionManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                Employee emp = new Employee();
                emp.setEmpId(rs.getInt("empid"));
                emp.setFname(rs.getString("Fname"));
                emp.setLname(rs.getString("Lname"));
                emp.setEmail(rs.getString("email"));
                emp.setHireDate(rs.getDate("HireDate").toString());
                emp.setSalary(rs.getDouble("salary"));
                emp.setSsn(rs.getString("SSN"));
                emp.setAddressId(rs.getInt("addressid"));
                return emp;
            }
        } catch (SQLException e) {
            System.out.println("Error finding employee by ID:" + e.getMessage());
        }
        return null;
    }
    
    // If to be implemented for searching by last name. output is list of the employees with the same last name.
    /**
    Search employees by last name
    Input: lname (String)
    Output: List of Employee objects if found, otherwise empty list
    Example usage:
    EmployeeDAO employeeDAO = new EmployeeDAO();
    List<Employee> employees = employeeDAO.searchByLastName("Smith");
    */
    // public List<Employee> searchByLastName(String lname) {

    //     String sql = """
    //             SELECT empid, Fname, Lname, email, HireDate, addressid, salary, SSN, addressID
    //             FROM employees
    //             WHERE Lname = ?
    //             """;
    //     List<Employee> emp = new ArrayList<>();
    //     try (Connection conn = connectionManager.getConnection();
    //         PreparedStatement stmt = conn.prepareStatement(sql)) {
    //         stmt.setString(1, lname);
    //         ResultSet rs = stmt.executeQuery();
    //         while (rs.next()) {
    //             Employee employee = new Employee();
    //             employee.setEmpId(rs.getInt("empid"));
    //             employee.setFname(rs.getString("Fname"));
    //             employee.setLname(rs.getString("Lname"));
    //             employee.setEmail(rs.getString("email"));
    //             employee.setHireDate(rs.getDate("HireDate").toString());
    //             emp.add(employee);
    //         }
    //         return emp;

    //     } catch (SQLException e) {
    //         System.out.println("Error finding employee by ID:" + e.getMessage());
    //     }
    //     return null;
    // }


    /**
    Search employees by email
    Input: email (String)
    Output: Employee object if found, otherwise null  
    Example usage:
    EmployeeDAO employeeDAO = new EmployeeDAO();
    Employee employee = employeeDAO.searchByEmail("example@example.com");
    */
    public Employee searchByEmail(String email) {

        String sql = """
                SELECT empid, Fname, Lname, email, HireDate, addressid, salary, SSN, addressID
                FROM employees
                WHERE email = ?
                """;
        try (Connection conn = connectionManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {

                Employee emp = new Employee();
                emp.setEmpId(rs.getInt("empid"));
                emp.setFname(rs.getString("Fname"));
                emp.setLname(rs.getString("Lname"));
                emp.setEmail(rs.getString("email"));
                emp.setHireDate(rs.getDate("HireDate").toString());
                emp.setSalary(rs.getDouble("salary"));
                emp.setSsn(rs.getString("SSN"));
                emp.setAddressId(rs.getInt("addressid"));
                return emp;
            }
        } catch (SQLException e) {
            System.out.println("Error finding employee by email:" + e.getMessage());
        }
        return null;
    }

    /**
    Update employee information
    Input: employee (Employee)
    Output: boolean indicating success or failure
    Example usage:
    EmployeeDAO employeeDAO = new EmployeeDAO();
    boolean updated = employeeDAO.updateEmployee(employee);
    */
    public boolean updateEmployee(Employee employee) {
        String sql = """
                UPDATE employees
                SET Fname = ?, Lname = ?, email = ?, HireDate = ?, salary = ?, SSN = ?, addressID = ?
                WHERE empid = ?
                """;
        try (Connection conn = connectionManager.getConnection()){
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return false;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, employee.getFname());
                stmt.setString(2, employee.getLname());
                stmt.setString(3, employee.getEmail());
                stmt.setDate(4, java.sql.Date.valueOf(employee.getHireDate()));
                stmt.setDouble(5, employee.getSalary());
                stmt.setString(6, employee.getSsn());
                stmt.setInt(7, employee.getAddressId());
                stmt.setInt(8, employee.getEmpId());
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
        }
        } catch (SQLException e) {
            System.out.println("Error updating employee:" + e.getMessage());
        }
        return false;
    }

    /**
    Update employee salary
    Input: empId (int), salary (double)
    Output: boolean indicating success or failure
    Example usage:
    EmployeeDAO employeeDAO = new EmployeeDAO();
    boolean updated = employeeDAO.updateSalary(1, 50000.0);
    */
    public boolean updateSalary(int empId, double salary) {
        String sql = """
                UPDATE employees
                SET salary = ?
                WHERE empid = ?
                """;
        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return false;
            }
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, salary);
                stmt.setInt(2, empId);
                int rowsAffected = stmt.executeUpdate();
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error updating employee salary:" + e.getMessage());
        }
        return false;
    }
}
