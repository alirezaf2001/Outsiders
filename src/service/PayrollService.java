package service;

import dao.PayrollDAO;
import model.PayrollRecord;

public class PayrollService {
    private final PayrollDAO payrollDAO = new PayrollDAO();

    public PayrollRecord getPayrollByEmployeeId(int empId) {
        if (empId <= 0) {
            System.out.println("Employee ID must be greater than 0.");
            return null;
        }

        return payrollDAO.findByEmployeeId(empId);
    }
}
