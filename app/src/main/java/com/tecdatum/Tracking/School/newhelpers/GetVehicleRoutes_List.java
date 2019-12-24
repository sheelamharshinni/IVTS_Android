package com.tecdatum.Tracking.School.newhelpers;

public class GetVehicleRoutes_List {

    String RouteNumber, RouteType, RouteName, VehicleMasterID, Vehiclenumber, StartingPointName,
            LastStopPointID, LastStopPointName, PointsType, StopPoints,RouteID;

    public String getRouteNumber() {
        return RouteNumber;
    }

    public void setRouteNumber(String routeNumber) {
        RouteNumber = routeNumber;
    }

    public String getRouteType() {
        return RouteType;
    }

    public void setRouteType(String routeType) {
        RouteType = routeType;
    }

    public String getRouteName() {
        return RouteName;
    }

    public void setRouteName(String routeName) {
        RouteName = routeName;
    }

    public String getVehicleMasterID() {
        return VehicleMasterID;
    }

    public void setVehicleMasterID(String vehicleMasterID) {
        VehicleMasterID = vehicleMasterID;
    }

    public String getVehiclenumber() {
        return Vehiclenumber;
    }

    public void setVehiclenumber(String vehiclenumber) {
        Vehiclenumber = vehiclenumber;
    }

    public String getStartingPointName() {
        return StartingPointName;
    }

    public void setStartingPointName(String startingPointName) {
        StartingPointName = startingPointName;
    }

    public String getLastStopPointID() {
        return LastStopPointID;
    }

    public void setLastStopPointID(String lastStopPointID) {
        LastStopPointID = lastStopPointID;
    }

    public String getLastStopPointName() {
        return LastStopPointName;
    }

    public void setLastStopPointName(String lastStopPointName) {
        LastStopPointName = lastStopPointName;
    }

    public String getPointsType() {
        return PointsType;
    }

    public void setPointsType(String pointsType) {
        PointsType = pointsType;
    }

    public String getStopPoints() {
        return StopPoints;
    }

    public void setStopPoints(String stopPoints) {
        StopPoints = stopPoints;
    }

    public String getRouteID() {
        return RouteID;
    }

    public void setRouteID(String routeID) {
        RouteID = routeID;
    }

    public GetVehicleRoutes_List(String routeNumber, String routeType, String routeName, String vehicleMasterID, String vehiclenumber, String startingPointName, String lastStopPointID, String lastStopPointName, String pointsType, String stopPoints, String routeID) {
        RouteNumber = routeNumber;
        RouteType = routeType;
        RouteName = routeName;
        VehicleMasterID = vehicleMasterID;
        Vehiclenumber = vehiclenumber;
        StartingPointName = startingPointName;
        LastStopPointID = lastStopPointID;
        LastStopPointName = lastStopPointName;
        PointsType = pointsType;
        StopPoints = stopPoints;
        RouteID = routeID;
    }
}
