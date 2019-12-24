package com.tecdatum.Tracking.School.newhelpers;

public class PickpointsHelper {
    String PointID, TypeOfPointsID,PointType,PointCode,PointName,Location,Latitude,Longitude;

    public String getPointID() {
        return PointID;
    }

    public void setPointID(String pointID) {
        PointID = pointID;
    }

    public String getTypeOfPointsID() {
        return TypeOfPointsID;
    }

    public void setTypeOfPointsID(String typeOfPointsID) {
        TypeOfPointsID = typeOfPointsID;
    }

    public String getPointType() {
        return PointType;
    }

    public void setPointType(String pointType) {
        PointType = pointType;
    }

    public String getPointCode() {
        return PointCode;
    }

    public void setPointCode(String pointCode) {
        PointCode = pointCode;
    }

    public String getPointName() {
        return PointName;
    }

    public void setPointName(String pointName) {
        PointName = pointName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

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

    public PickpointsHelper(String pointID, String typeOfPointsID, String pointType, String pointCode, String pointName, String location, String latitude, String longitude) {
        PointID = pointID;
        TypeOfPointsID = typeOfPointsID;
        PointType = pointType;
        PointCode = pointCode;
        PointName = pointName;
        Location = location;
        Latitude = latitude;
        Longitude = longitude;
    }
}
