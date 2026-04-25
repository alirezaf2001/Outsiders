package service;

import dao.PayrollDAO;
import java.util.Collections;
import java.util.List;
import model.PayrollRecord;

public class PayrollService {
    private final PayrollDAO payrollDAO = new PayrollDAO();
    /**
     * Get payroll record by employee ID
     * @param empId
     * @return PayrollRecord object if found, otherwise null
     * {@snippet lang="java" :
     * PayrollService payrollService = new PayrollService();
     * PayrollRecord record = payrollService.getPayrollByEmployeeId(1);
     * }
     */
    public PayrollRecord getPayrollByEmployeeId(int empId) {
        if (empId <= 0) {
            System.out.println("Employee ID must be greater than 0.");
            return null;
        }

        return payrollDAO.findByEmployeeId(empId);
    }

    public List<PayrollRecord> findPayrollHistoryByEmployeeId(int empId) {
        if (empId <= 0) {
            System.out.println("Employee ID must be greater than 0.");
            return Collections.emptyList();
        }

        return payrollDAO.findPayrollHistoryByEmployeeId(empId);
    }
}
