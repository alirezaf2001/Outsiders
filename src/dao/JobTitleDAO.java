package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.ConnectionManager;
import model.JobTitle;

public class JobTitleDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();
    /**
     * Find job titles by employee ID
     * @param empId
     * @return  JobTitle object if found, otherwise null
     * {@snippet lang="java" :
     * JobTitleDAO jobTitleDAO = new JobTitleDAO();
     * JobTitle jobTitle = jobTitleDAO.findJobTitlesByEmployeeId(1);}
     */
    public JobTitle findJobTitlesByEmployeeId(int empId) {
        String sql = """
                SELECT jt.jobTitleID, jt.title
                FROM employee_job_titles ejt
                JOIN job_titles jt ON jt.jobTitleID = ejt.jobTitleID
                WHERE ejt.empId = ?
                """;
        try (Connection conn = connectionManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, empId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JobTitle jobTitle = new JobTitle();
                jobTitle.setJobTitleId(rs.getInt("jobTitleID"));
                jobTitle.setJobTitle(rs.getString("title"));
                return jobTitle;
            }
        } catch (SQLException e) {
            System.out.println("Error finding job title by employee ID:" + e.getMessage());
        }
        return null;
    }
}
