package model;

public class PayrollRecord {
    private int id;
    private int employeeId;
    private double baseSalary;
    private double bonus;
    private double deductions;
    private String payDate;

    public PayrollRecord() {
    }

    public PayrollRecord(int id, int employeeId, double baseSalary, double bonus, double deductions, String payDate) {
        this.id = id;
        this.employeeId = employeeId;
        this.baseSalary = baseSalary;
        this.bonus = bonus;
        this.deductions = deductions;
        this.payDate = payDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }

    public double getDeductions() {
        return deductions;
    }

    public void setDeductions(double deductions) {
        this.deductions = deductions;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    @Override
    public String toString() {
        return "PayrollRecord{id=" + id
            + ", employeeId=" + employeeId
            + ", baseSalary=" + baseSalary
            + ", bonus=" + bonus
            + ", deductions=" + deductions
            + ", payDate='" + payDate + '\''
            + '}';
    }
}
