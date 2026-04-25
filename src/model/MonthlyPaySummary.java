package model;

public class MonthlyPaySummary {
    private String label;
    private double totalPay;

    public MonthlyPaySummary() {
    }

    public MonthlyPaySummary(String label, double totalPay) {
        this.label = label;
        this.totalPay = totalPay;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(double totalPay) {
        this.totalPay = totalPay;
    }
}
