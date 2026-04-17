package model;

public class Employee {
    private int empId;
    private String fname;
    private String lname;
    private String email;
    private String hireDate;
    private double salary;
    private String ssn;
    private int addressId;

    public Employee() {
    }

    public Employee(int empId, String fname, String lname, String email, String hireDate,
            double salary, String ssn, int addressId) {
        this.empId = empId;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.hireDate = hireDate;
        this.salary = salary;
        this.ssn = ssn;
        this.addressId = addressId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        this.hireDate = hireDate;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    @Override
    public String toString() {
        return "Employee{empId=" + empId
            + ", fname='" + fname + '\''
            + ", lname='" + lname + '\''
            + ", email='" + email + '\''
            + ", hireDate='" + hireDate + '\''
            + ", salary=" + salary
            + ", ssn='" + ssn + '\''
            + ", addressId=" + addressId
            + '}';
    }
}
