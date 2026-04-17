package model;

public class City {
    private int cityID;
    private String cityName;

    public City() {
    }

    public City(int cityID, String cityName) {
        this.cityID = cityID;
        this.cityName = cityName;
    }

    public int getCityID() {
        return cityID;
    }

    public void setCityID(int cityID) {
        this.cityID = cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return "City{cityID=" + cityID
            + ", cityName='" + cityName + '\''
            + '}';
    }
}
