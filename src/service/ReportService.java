package service;

import dao.ReportDAO;
import java.util.Collections;
import java.util.List;
import model.Employee;
import model.MonthlyPaySummary;

public class ReportService {
    private final ReportDAO reportDAO = new ReportDAO();

    /**
     * Get total pay by job title for a given month and year
     * @param month 1-12
     * @param year 4-digit year
     * @return List of MonthlyPaySummary objects containing job title and total pay
     * {@snippet lang="java" :
     * ReportService reportService = new ReportService();
     * List<MonthlyPaySummary> payByJobTitle = reportService.getTotalPayByJobTitle(1, 2024);
     * }
     */
    public List<MonthlyPaySummary> getTotalPayByJobTitle(int month, int year) {
        if (!isValidMonthYear(month, year)) {
            return Collections.emptyList();
        }

        return reportDAO.getTotalPayByJobTitle(month, year);
    }

    /**
     * Get total pay by division for a given month and year
     * @param month 1-12
     * @param year 4-digit year
     * @return List of MonthlyPaySummary objects containing division name and total pay
     * {@snippet lang="java" :
     * ReportService reportService = new ReportService();
     * List<MonthlyPaySummary> payByDivision = reportService.getTotalPayByDivision(1, 2024);
     * }
     */
    public List<MonthlyPaySummary> getTotalPayByDivision(int month, int year) {
        if (!isValidMonthYear(month, year)) {
            return Collections.emptyList();
        }

        return reportDAO.getTotalPayByDivision(month, year);
    }

    /**
     * Get new hires by date range
     * @param startDate start date in YYYY-MM-DD format
     * @param endDate end date in YYYY-MM-DD format
     * @return List of Employee objects representing new hires within the date range
     * {@snippet lang="java" :
     * ReportService reportService = new ReportService();
     * List<Employee> newHires = reportService.getNewHiresByDateRange("2024-01-01", "2024-12-31");
     * }
     */

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

    // Helper method to validate month and year inputs
    private boolean isValidMonthYear(int month, int year) {
        return month >= 1 && month <= 12 && year >= 1900;
    }
}
