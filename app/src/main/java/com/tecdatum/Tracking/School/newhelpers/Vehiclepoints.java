package com.tecdatum.Tracking.School.newhelpers;

public class Vehiclepoints {
    String Latitude, Longitude, PointName;

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getPointName() {
        return PointName;
    }

    public void setPointName(String pointName) {
        PointName = pointName;
    }

    public Vehiclepoints(String latitude, String longitude, String pointName) {
        Latitude = latitude;
        Longitude = longitude;
        PointName = pointName;
    }
}
