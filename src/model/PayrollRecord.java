package model;

public class PayrollRecord {
    private int payrollId;
    private int empId;
    private String payDate;
    private double grossPay;
    private double deductions;
    private double netPay;

    public PayrollRecord() {
    }

    public PayrollRecord(int payrollId, int empId, String payDate, double grossPay,
            double deductions, double netPay) {
        this.payrollId = payrollId;
        this.empId = empId;
        this.payDate = payDate;
        this.grossPay = grossPay;
        this.deductions = deductions;
        this.netPay = netPay;
    }

    public int getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public double getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public double getNetPay() {
        return netPay;
    }

    public void setNetPay(double netPay) {
        this.netPay = netPay;
    }

    @Override
    public String toString() {
        return "PayrollRecord{payrollId=" + payrollId
            + ", empId=" + empId
            + ", payDate='" + payDate + '\''
            + ", grossPay=" + grossPay
            + ", deductions=" + deductions
            + ", netPay=" + netPay
            + '}';
    }
}
