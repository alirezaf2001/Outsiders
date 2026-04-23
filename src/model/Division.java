package model;

public class Division {
    private int divID;
    private String name;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String country;
    private String postalCode;

    public Division() {
    }

    public Division(
        int divID,
        String name,
        String city,
        String addressLine1,
        String addressLine2,
        String state,
        String country,
        String postalCode) {
            
        this.divID = divID;
        this.name = name;
        this.city = city;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
    }

    public int getDivID() {
        return divID;
    }

    public void setDivID(int divID) {
        this.divID = divID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Override
    public String toString() {
        return "Division{divID=" + divID
            + ", name='" + name + '\''
            + ", city='" + city + '\''
            + ", addressLine1='" + addressLine1 + '\''
            + ", addressLine2='" + addressLine2 + '\''
            + ", state='" + state + '\''
            + ", country='" + country + '\''
            + ", postalCode='" + postalCode + '\''
            + '}';
    }
}
