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
import model.MonthlyPaySummary;

public class ReportDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    /**
     * Get total pay by job title for a given month and year
     * @param month
     * @param year
     * @return List of MonthlyPaySummary objects containing job title and total pay
     * {@snippet lang="java" :
     * ReportDAO reportDAO = new ReportDAO();
     * List<MonthlyPaySummary> payByJobTitle = reportDAO.getTotalPayByJobTitle(5, 2024);
     * }
     */
    public List<MonthlyPaySummary> getTotalPayByJobTitle(int month, int year) {
        String sql = """
                SELECT jt.job_title AS label, SUM(p.earnings) AS totalPay
                FROM payroll p
                JOIN employee_job_titles ejt ON ejt.empid = p.empid
                JOIN job_titles jt ON jt.job_title_id = ejt.job_title_id
                WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ?
                GROUP BY jt.job_title_id, jt.job_title
                ORDER BY jt.job_title
                """;

        return queryMonthlyTotals(sql, month, year);
    }

        /**
        * Get total pay by division for a given month and year
        * @param month
        * @param year
        * @return List of MonthlyPaySummary objects containing division name and total pay
        * {@snippet lang="java" :
        * ReportDAO reportDAO = new ReportDAO();
        * List<MonthlyPaySummary> payByDivision = reportDAO.getTotalPayByDivision(5, 2024);
        * }
        */
    public List<MonthlyPaySummary> getTotalPayByDivision(int month, int year) {
        String sql = """
                SELECT d.Name AS label, SUM(p.earnings) AS totalPay
                FROM payroll p
                JOIN employee_division ed ON ed.empid = p.empid
                JOIN division d ON d.divID = ed.div_ID
                WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ?
                GROUP BY d.divID, d.Name
                ORDER BY d.Name
                """;

        return queryMonthlyTotals(sql, month, year);
    }

    /**
     * Get new hires within a specified date range
     * @param startDate in YYYY-MM-DD format
     * @param endDate in YYYY-MM-DD format
     * @return List of Employee objects representing new hires
     * {@snippet lang="java" :
     * ReportDAO reportDAO = new ReportDAO();
     * List<Employee> newHires = reportDAO.getNewHiresByDateRange("2024-01-01", "2024-03-31");
     * }
     */
    public List<Employee> getNewHiresByDateRange(String startDate, String endDate) {
        String sql = """
                SELECT empid, Fname, Lname, email, HireDate, Salary, SSN, addressID
                FROM employees
                WHERE HireDate BETWEEN ? AND ?
                ORDER BY HireDate DESC, empid
                """;

        List<Employee> employees = new ArrayList<>();

        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                return Collections.emptyList();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDate(1, Date.valueOf(startDate));
                stmt.setDate(2, Date.valueOf(endDate));
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setEmpId(rs.getInt("empid"));
                    employee.setFname(rs.getString("Fname"));
                    employee.setLname(rs.getString("Lname"));
                    employee.setEmail(rs.getString("email"));
                    employee.setHireDate(rs.getDate("HireDate").toString());
                    employee.setSalary(rs.getDouble("Salary"));
                    employee.setSsn(rs.getString("SSN"));
                    employee.setAddressId(rs.getInt("addressID"));
                    employees.add(employee);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Date range must use YYYY-MM-DD format.");
        } catch (SQLException e) {
            System.out.println("Error finding new hires by date range: " + e.getMessage());
        }

        return employees;
    }

    /**
     * Helper method to run monthly totals queries for both job title and division reports
     * @param sql
     * @param month
     * @param year
     * @return List of MonthlyPaySummary objects containing label and total pay
     */
    private List<MonthlyPaySummary> queryMonthlyTotals(String sql, int month, int year) {
        List<MonthlyPaySummary> rows = new ArrayList<>();

        try (Connection conn = connectionManager.getConnection()) {
            if (conn == null) {
                return Collections.emptyList();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, month);
                stmt.setInt(2, year);
                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    rows.add(new MonthlyPaySummary(
                            rs.getString("label"),
                            rs.getDouble("totalPay")));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error running monthly totals report: " + e.getMessage());
        }

        return rows;
    }
}
