package service;

import dao.ReportDAO;
import java.util.Collections;
import java.util.List;
import model.Employee;
import model.MonthlyPaySummary;

public class ReportService {
    private final ReportDAO reportDAO = new ReportDAO();

    public List<MonthlyPaySummary> getTotalPayByJobTitle(int month, int year) {
        if (!isValidMonthYear(month, year)) {
            return Collections.emptyList();
        }

        return reportDAO.getTotalPayByJobTitle(month, year);
    }

    public List<MonthlyPaySummary> getTotalPayByDivision(int month, int year) {
        if (!isValidMonthYear(month, year)) {
            return Collections.emptyList();
        }

        return reportDAO.getTotalPayByDivision(month, year);
    }

    public List<Employee> getNewHiresByDateRange(String startDate, String endDate) {
        if (startDate == null || startDate.isBlank() || endDate == null || endDate.isBlank()) {
            return Collections.emptyList();
        }

        try {
            java.sql.Date start = java.sql.Date.valueOf(startDate.trim());
            java.sql.Date end = java.sql.Date.valueOf(endDate.trim());
            if (start.after(end)) {
                return Collections.emptyList();
            }
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }

        return reportDAO.getNewHiresByDateRange(startDate.trim(), endDate.trim());
    }

    private boolean isValidMonthYear(int month, int year) {
        return month >= 1 && month <= 12 && year >= 1900;
    }
}
