package session;

import model.Employee;

public class UserSession {
    private Employee currentEmployee;
    private int empId;
    private boolean hrAdmin;

    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public int getEmpId() {
        return empId;
    }

    public boolean isHrAdmin() {
        return hrAdmin;
    }

    public boolean isLoggedIn() {
        return currentEmployee != null;
    }

    public void setCurrentEmployee(Employee currentEmployee, boolean hrAdmin) {
        this.currentEmployee = currentEmployee;
        this.empId = currentEmployee != null ? currentEmployee.getEmpId() : 0;
        this.hrAdmin = hrAdmin;
    }

    public void clear() {
        currentEmployee = null;
        empId = 0;
        hrAdmin = false;
    }
}
