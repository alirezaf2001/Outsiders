package automated_tests;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import model.Employee;
import model.MonthlyPaySummary;
import model.PayrollRecord;
import service.EmployeeService;
import service.PayrollService;
import service.ReportService;

public class DatabaseReadSmokeTest {
    private static final AutomatedTestSupport.TestReport REPORT =
            new AutomatedTestSupport.TestReport("DatabaseReadSmokeTest");

    public static void main(String[] args) {
        try {
            runReadSmokeChecks();
        } catch (Throwable error) {
            REPORT.unexpected("Unexpected exception during database read smoke test", error);
        }

        REPORT.finish();
    }

    private static void runReadSmokeChecks() throws Exception {
        try (Connection connection = AutomatedTestSupport.requireConnection()) {
            REPORT.check(
                    "Database connection opens successfully",
                    connection != null && !connection.isClosed(),
                    "ConnectionManager could not open the employeeData database.");
        }

        EmployeeService employeeService = new EmployeeService();
        PayrollService payrollService = new PayrollService();
        ReportService reportService = new ReportService();

        Integer employeeId = AutomatedTestSupport.findAnyEmployeeId();
        REPORT.check(
                "employees table contains at least one employee",
                employeeId != null,
                "No employee rows were found.");
        if (employeeId == null) {
            return;
        }

        Employee sampleEmployee = employeeService.searchEmployeeById(employeeId);
        REPORT.check(
                "searchEmployeeById() loads the sample employee",
                sampleEmployee != null && sampleEmployee.getEmpId() == employeeId,
                "EmployeeService.searchEmployeeById() did not return the expected employee.");
        if (sampleEmployee == null) {
            return;
        }

        Employee byEmail = employeeService.searchByEmail(sampleEmployee.getEmail());
        REPORT.check(
                "searchByEmail() finds the same employee",
                byEmail != null && byEmail.getEmpId() == sampleEmployee.getEmpId(),
                "EmployeeService.searchByEmail() did not find the sample employee.");

        List<Employee> byName = employeeService.searchByName(sampleEmployee.getLname());
        REPORT.check(
                "searchByName() finds the sample employee",
                containsEmployee(byName, sampleEmployee.getEmpId()),
                "EmployeeService.searchByName() did not return the sample employee.");

        List<Employee> bySsn = employeeService.searchBySSN(sampleEmployee.getSsn());
        REPORT.check(
                "searchBySSN() finds the sample employee",
                containsEmployee(bySsn, sampleEmployee.getEmpId()),
                "EmployeeService.searchBySSN() did not return the sample employee.");

        Integer payrollEmployeeId = AutomatedTestSupport.findAnyPayrollEmployeeId();
        if (payrollEmployeeId == null) {
            REPORT.info("Payroll table has no rows. Payroll and monthly report smoke checks were skipped.");
        } else {
            List<PayrollRecord> payrollHistory = payrollService.findPayrollHistoryByEmployeeId(payrollEmployeeId);
            REPORT.check(
                    "findPayrollHistoryByEmployeeId() returns payroll rows for a sample employee",
                    payrollHistory != null && !payrollHistory.isEmpty(),
                    "PayrollService.findPayrollHistoryByEmployeeId() returned no rows for the sample payroll employee.");

            if (payrollHistory != null && !payrollHistory.isEmpty()) {
                REPORT.check(
                        "Payroll history is sorted newest first",
                        isNewestFirst(payrollHistory),
                        "Payroll history rows were not sorted by most recent pay date first.");
            }

            AutomatedTestSupport.MonthYear monthYear = AutomatedTestSupport.findAnyPayrollMonthYear();
            if (monthYear != null) {
                List<MonthlyPaySummary> byJobTitle = reportService.getTotalPayByJobTitle(
                        monthYear.getMonth(), monthYear.getYear());
                REPORT.check(
                        "Total pay by job title report executes for a sample payroll month",
                        byJobTitle != null,
                        "ReportService.getTotalPayByJobTitle() returned null.");

                List<MonthlyPaySummary> byDivision = reportService.getTotalPayByDivision(
                        monthYear.getMonth(), monthYear.getYear());
                REPORT.check(
                        "Total pay by division report executes for a sample payroll month",
                        byDivision != null,
                        "ReportService.getTotalPayByDivision() returned null.");
            }
        }

        AutomatedTestSupport.DateRange hireDateRange = AutomatedTestSupport.findEmployeeHireDateRange();
        if (hireDateRange == null) {
            REPORT.info("No employee hire-date range could be determined. New hires report smoke check was skipped.");
        } else {
            List<Employee> newHires = reportService.getNewHiresByDateRange(
                    hireDateRange.getStartDate(),
                    hireDateRange.getEndDate());
            REPORT.check(
                    "New hires report returns rows for the full employee hire-date range",
                    newHires != null && !newHires.isEmpty(),
                    "ReportService.getNewHiresByDateRange() did not return rows for the full employee hire-date range.");
        }
    }

    private static boolean containsEmployee(List<Employee> employees, int empId) {
        if (employees == null) {
            return false;
        }

        for (Employee employee : employees) {
            if (employee.getEmpId() == empId) {
                return true;
            }
        }

        return false;
    }

    private static boolean isNewestFirst(List<PayrollRecord> records) {
        for (int index = 1; index < records.size(); index++) {
            PayrollRecord previous = records.get(index - 1);
            PayrollRecord current = records.get(index);

            Date previousDate = Date.valueOf(previous.getPayDate());
            Date currentDate = Date.valueOf(current.getPayDate());

            if (previousDate.before(currentDate)) {
                return false;
            }

            if (previousDate.equals(currentDate) && previous.getPayrollId() < current.getPayrollId()) {
                return false;
            }
        }

        return true;
    }
}
