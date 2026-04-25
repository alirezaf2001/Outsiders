package dao;

import db.ConnectionManager;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.Employee;

public class EmployeeDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    /**
     * Find employee by ID
     * @param empId
     * @return Employee object if found, otherwise null
     * {@snippet lang="java" :
     * EmployeeDAO employeeDAO = new EmployeeDAO();
     * Employee employee = employeeDAO.findById(1);\
     * }
     */
    public Employee findById(int empId) {
        String sql = """
                SELECT empid, Fname, Lname, email, HireDate, Salary, SSN, addressID
                FROM employees
                WHERE empid = ?
                """;
        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                return null;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, empId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapEmployee(rs);
                }
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
     * Search employee by email
     * @param email
     * @return Employee object if found, otherwise null
     * {@snippet lang="java" :
     * EmployeeDAO employeeDAO = new EmployeeDAO();
     * Employee employee = employeeDAO.searchByEmail("example@example.com");
     * }
     */
    public Employee searchByEmail(String email) {

        String sql = """
                SELECT empid, Fname, Lname, email, HireDate, Salary, SSN, addressID
                FROM employees
                WHERE email = ?
                """;
        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                return null;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return mapEmployee(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding employee by email:" + e.getMessage());
        }
        return null;
    }
    /**
     * Search employees by name
     * @param name
     * @return List of Employee objects if found, otherwise empty list
     * {@snippet lang="java" :
     * EmployeeDAO employeeDAO = new EmployeeDAO();
     * List<Employee> employees = employeeDAO.searchByName("John");
     * }
     */
    public List<Employee> searchByName(String name) {
        String sql = """
                SELECT empid, Fname, Lname, email, HireDate, Salary, SSN, addressID
                FROM employees
                WHERE Fname LIKE ? OR Lname LIKE ? OR CONCAT(Fname, ' ', Lname) LIKE ?
                ORDER BY Lname, Fname, empid
                """;

        List<Employee> employees = new ArrayList<>();
        String likeValue = "%" + name + "%";

        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                return Collections.emptyList();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, likeValue);
                stmt.setString(2, likeValue);
                stmt.setString(3, likeValue);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding employees by name:" + e.getMessage());
        }

        return employees;
    }
    
    /**
     * Search employees by date of birth
     * @param dob
     * @return List of Employee objects if found, otherwise empty list
     * {@snippet lang="java" :
     * EmployeeDAO employeeDAO = new EmployeeDAO();
     * List<Employee> employees = employeeDAO.searchByDOB("1990-01-01");
     * }
     */
    public List<Employee> searchByDOB(String dob) {
        String sql = """
                SELECT e.empid, e.Fname, e.Lname, e.email, e.HireDate, e.Salary, e.SSN, e.addressID
                FROM employees e
                JOIN addresses a ON a.addressID = e.addressID
                WHERE a.DOB = ?
                ORDER BY e.empid
                """;

        List<Employee> employees = new ArrayList<>();

        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                return Collections.emptyList();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(dob));
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("DOB must be in YYYY-MM-DD format.");
        } catch (SQLException e) {
            System.out.println("Error finding employees by DOB:" + e.getMessage());
        }

        return employees;
    }

    /**
     * Search employees by SSN
     * @param ssn
     * @return List of Employee objects if found, otherwise empty list
     * {@snippet lang="java" :
     * EmployeeDAO employeeDAO = new EmployeeDAO();
     * List<Employee> employees = employeeDAO.searchBySSN("123-45-6789");
     * }
     */
    public List<Employee> searchBySSN(String ssn) {
        String sql = """
                SELECT empid, Fname, Lname, email, HireDate, Salary, SSN, addressID
                FROM employees
                WHERE SSN = ?
                ORDER BY empid
                """;

        List<Employee> employees = new ArrayList<>();

        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                return Collections.emptyList();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, ssn);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    employees.add(mapEmployee(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error finding employees by SSN:" + e.getMessage());
        }

        return employees;
    }

    /**
     * Update employee information
     * @param employee
     * @return  boolean indicating success or failure
     * {@snippet lang="java" :
     * EmployeeDAO employeeDAO = new EmployeeDAO();
     * Employee employee = employeeDAO.findById(1);
     * employee.setEmail("newemail@example.com");
     * boolean updated = employeeDAO.updateEmployee(employee);
     * }
     */
    public boolean updateEmployee(Employee employee) {
        String sql = """
                UPDATE employees
                SET Fname = ?, Lname = ?, email = ?, HireDate = ?, Salary = ?, SSN = ?, addressID = ?
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
        } catch (IllegalArgumentException e) {
            System.out.println("Hire date must be in YYYY-MM-DD format.");
        } catch (SQLException e) {
            System.out.println("Error updating employee:" + e.getMessage());
        }
        return false;
    }

    /**
     * Update employee salary
     * @param empId
     * @param salary
     * @return boolean indicating success or failure
     * {@snippet lang="java" :
     * EmployeeDAO employeeDAO = new EmployeeDAO();
     * boolean updated = employeeDAO.updateSalary(1, 50000.0);}
     */
    public boolean updateSalary(int empId, double salary) {
        String sql = """
                UPDATE employees
                SET Salary = ?
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

    public boolean addEmployee(Employee employee) {
        String sql = """
                INSERT INTO employees (empid, Fname, Lname, email, HireDate, Salary, SSN, addressID)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return false;
            }

            int empId = employee.getEmpId() > 0 ? employee.getEmpId() : getNextEmployeeId(conn);
            employee.setEmpId(empId);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, employee.getEmpId());
                stmt.setString(2, employee.getFname());
                stmt.setString(3, employee.getLname());
                stmt.setString(4, employee.getEmail());
                stmt.setDate(5, Date.valueOf(employee.getHireDate()));
                stmt.setDouble(6, employee.getSalary());
                stmt.setString(7, employee.getSsn());
                stmt.setInt(8, employee.getAddressId());
                return stmt.executeUpdate() > 0;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Hire date must be in YYYY-MM-DD format.");
        } catch (SQLException e) {
            System.out.println("Error adding employee:" + e.getMessage());
        }

        return false;
    }

    /**
     * Delete an employee from the database
     * @param empId
     * @return boolean indicating success or failure
     * {@snippet lang="java" :
     * EmployeeDAO employeeDAO = new EmployeeDAO();
     * boolean deleted = employeeDAO.deleteEmployee(1);}
     */
    public boolean deleteEmployee(int empId) {
        String deleteJobTitlesSql = "DELETE FROM employee_job_titles WHERE empid = ?";
        String deleteDivisionSql = "DELETE FROM employee_division WHERE empid = ?";
        String deletePayrollSql = "DELETE FROM payroll WHERE empid = ?";
        String deleteEmployeeSql = "DELETE FROM employees WHERE empid = ?";

        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return false;
            }

            conn.setAutoCommit(false);

            try (
                PreparedStatement deleteJobTitles = conn.prepareStatement(deleteJobTitlesSql);
                PreparedStatement deleteDivision = conn.prepareStatement(deleteDivisionSql);
                PreparedStatement deletePayroll = conn.prepareStatement(deletePayrollSql);
                PreparedStatement deleteEmployee = conn.prepareStatement(deleteEmployeeSql)
            ) {
                deleteJobTitles.setInt(1, empId);
                deleteJobTitles.executeUpdate();

                deleteDivision.setInt(1, empId);
                deleteDivision.executeUpdate();

                deletePayroll.setInt(1, empId);
                deletePayroll.executeUpdate();

                deleteEmployee.setInt(1, empId);
                int deletedRows = deleteEmployee.executeUpdate();

                conn.commit();
                return deletedRows > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.out.println("Error deleting employee:" + e.getMessage());
        }

        return false;
    }

    /**
     * Update salaries for employees within a specified salary range by a given percentage
     * @param minSalary minimum salary of the range
     * @param maxSalary maximum salary of the range
     * @param percentage percentage to increase salaries by (e.g., 10 for 10%)
     * @return number of employees whose salaries were updated
     * {@snippet lang="java" :
     * EmployeeDAO employeeDAO = new EmployeeDAO();
     * int updatedCount = employeeDAO.updateSalariesByRange(40000.0, 60000.0, 10.0);
     * }
     */
    public int updateSalariesByRange(double minSalary, double maxSalary, double percentage) {
        String sql = """
                UPDATE employees
                SET Salary = Salary + (Salary * ? / 100)
                WHERE Salary BETWEEN ? AND ?
                """;

        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                System.out.println("Failed to establish a database connection.");
                return 0;
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, percentage);
                stmt.setDouble(2, minSalary);
                stmt.setDouble(3, maxSalary);
                return stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Error updating employee salaries:" + e.getMessage());
        }

        return 0;
    }

    /**
     * Map a ResultSet row to an Employee object
     * @param rs
     * @return Employee object
     * @throws SQLException
     * {@snippet lang="java" :
     * // This method is used internally by the DAO and is not typically called directly.
     * }
     */
    private Employee mapEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmpId(rs.getInt("empid"));
        employee.setFname(rs.getString("Fname"));
        employee.setLname(rs.getString("Lname"));
        employee.setEmail(rs.getString("email"));

        Date hireDate = rs.getDate("HireDate");
        employee.setHireDate(hireDate != null ? hireDate.toString() : "");
        employee.setSalary(rs.getDouble("Salary"));
        employee.setSsn(rs.getString("SSN"));
        employee.setAddressId(rs.getInt("addressID"));
        return employee;
    }

    /**
     * Get the next available employee ID
     * @param conn
     * @return next employee ID as an integer
     * @throws SQLException
     * {@snippet lang="java" :
     * // This method is used internally by the DAO when adding a new employee without a specified
     * ID. It queries the database to find the current maximum employee ID and returns the next one.
     * }
     */
    private int getNextEmployeeId(Connection conn) throws SQLException {
        String sql = "SELECT COALESCE(MAX(empid), 0) + 1 AS nextId FROM employees";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("nextId");
            }
        }

        throw new SQLException("Unable to generate next employee ID.");
    }
}
