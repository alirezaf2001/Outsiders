package automated_tests;

import model.Division;
import model.Employee;
import service.EmployeeService;

public class DatabaseEmployeeRoundTripTest {
    private static final AutomatedTestSupport.TestReport REPORT =
            new AutomatedTestSupport.TestReport("DatabaseEmployeeRoundTripTest");

    public static void main(String[] args) {
        EmployeeService employeeService = new EmployeeService();
        Employee tempEmployee = null;
        boolean added = false;

        try {
            Integer addressId = AutomatedTestSupport.findAnyAddressId();
            Integer divisionId = AutomatedTestSupport.findAnyDivisionId();

            REPORT.check(
                    "addresses table contains at least one address row",
                    addressId != null,
                    "No address rows were found. Add Employee round-trip cannot run.");
            REPORT.check(
                    "division table contains at least one division row",
                    divisionId != null,
                    "No division rows were found. Add Employee round-trip cannot run.");

            if (addressId == null || divisionId == null) {
                REPORT.finish();
                return;
            }

            String uniqueSuffix = String.valueOf(System.currentTimeMillis());
            String lastFour = uniqueSuffix.substring(uniqueSuffix.length() - 4);
            String initialEmail = "autotest_roundtrip_" + uniqueSuffix + "@example.com";
            String updatedEmail = "autotest_roundtrip_updated_" + uniqueSuffix + "@example.com";

            tempEmployee = new Employee(
                    0,
                    "Auto",
                    "RoundTrip" + lastFour,
                    initialEmail,
                    "2026-04-25",
                    54321.37,
                    "999-88-" + lastFour,
                    addressId);

            added = employeeService.addEmployee(tempEmployee, divisionId);
            REPORT.check(
                    "Temporary employee insert succeeds",
                    added,
                    "EmployeeService.addEmployee() failed for the temporary round-trip employee.");

            if (!added) {
                return;
            }

            REPORT.check(
                    "Temporary employee received a generated empId",
                    tempEmployee.getEmpId() > 0,
                    "The temporary employee did not receive an empId after insertion.");

            Employee insertedById = employeeService.searchEmployeeById(tempEmployee.getEmpId());
            REPORT.check(
                    "Inserted employee can be loaded by empId",
                    insertedById != null && insertedById.getEmpId() == tempEmployee.getEmpId(),
                    "The temporary employee could not be loaded by empId after insertion.");

            Employee insertedByEmail = employeeService.searchByEmail(initialEmail);
            REPORT.check(
                    "Inserted employee can be loaded by email",
                    insertedByEmail != null && insertedByEmail.getEmpId() == tempEmployee.getEmpId(),
                    "The temporary employee could not be loaded by email after insertion.");

            tempEmployee.setLname("Updated" + lastFour);
            tempEmployee.setEmail(updatedEmail);
            tempEmployee.setSalary(55444.44);

            boolean updated = employeeService.updateEmployee(tempEmployee, divisionId);
            REPORT.check(
                    "Temporary employee update succeeds",
                    updated,
                    "EmployeeService.updateEmployee() failed for the temporary employee.");

            Employee updatedEmployee = employeeService.searchEmployeeById(tempEmployee.getEmpId());
            REPORT.check(
                    "Updated employee record contains the new email",
                    updatedEmployee != null && updatedEmail.equals(updatedEmployee.getEmail()),
                    "The updated email was not saved.");
            REPORT.check(
                    "Updated employee record contains the new salary",
                    updatedEmployee != null && Math.abs(updatedEmployee.getSalary() - 55444.44) < 0.001,
                    "The updated salary was not saved.");

            Division division = employeeService.getDivisionByEmployeeId(tempEmployee.getEmpId());
            REPORT.check(
                    "Temporary employee keeps the assigned division",
                    division != null && division.getDivID() == divisionId,
                    "The temporary employee division was not saved as expected.");
        } catch (Throwable error) {
            REPORT.unexpected("Unexpected exception during employee round-trip test", error);
        } finally {
            if (added && tempEmployee != null && tempEmployee.getEmpId() > 0) {
                try {
                    boolean deleted = employeeService.deleteEmployee(tempEmployee.getEmpId());
                    REPORT.check(
                            "Temporary employee cleanup delete succeeds",
                            deleted,
                            "Cleanup failed. The temporary employee was not deleted.");

                    Employee deletedEmployee = employeeService.searchEmployeeById(tempEmployee.getEmpId());
                    REPORT.check(
                            "Temporary employee is gone after cleanup",
                            deletedEmployee == null,
                            "The temporary employee still exists after cleanup.");
                } catch (Throwable error) {
                    REPORT.unexpected("Unexpected exception during employee round-trip cleanup", error);
                }
            }
        }

        REPORT.finish();
    }
}
