package dao;

import java.util.ArrayList;
import java.util.List;

import db.ConnectionManager;
import model.JobTitle;

public class JobTitleDAO {
    private final ConnectionManager connectionManager = new ConnectionManager();

    public List<JobTitle> findJobTitlesByEmployeeId(int empId) {
        // TODO: Use connectionManager.getConnection() and query job_titles via employee_job_titles.
        return new ArrayList<>();
    }
}
