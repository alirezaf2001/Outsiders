package model;

public class Address {
    private int addressID;
    private String street;
    private int cityID;
    private int stateID;
    private String zip;
    private String DOB;
    private String phone;
    private String emergencyContact;
    private String emergencyPhone;

    public Address() {
    }

    public Address(int addressID, String street, int cityID, int stateID, String zip,
            String DOB, String phone, String emergencyContact, String emergencyPhone) {
        this.addressID = addressID;
        this.street = street;
        this.cityID = cityID;
        this.stateID = stateID;
        this.zip = zip;
        this.DOB = DOB;
        this.phone = phone;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }

    @Override
    public String toString() {
        return "Address{addressID=" + addressID
            + ", street='" + street + '\''
            + ", cityID=" + cityID
            + ", stateID=" + stateID
            + ", zip='" + zip + '\''
            + ", DOB='" + DOB + '\''
            + ", phone='" + phone + '\''
            + ", emergencyContact='" + emergencyContact + '\''
            + ", emergencyPhone='" + emergencyPhone + '\''
            + '}';
    }
}
