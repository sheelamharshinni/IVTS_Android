package com.tecdatum.Tracking.School.newhelpers;

public class QvVehiclesList {
    String vehicle_Id, VehicleRegNo, Vehicle, Vehicletype, Sincefrom,
            Livedatetime, DriverName, MobileNo, VehicleStatus, Location, Latitude,Longitude,AcStatus,Gpsstatus,Speed,Ignition,Model;
    private boolean expanded;

    public QvVehiclesList(String vehicle_Id, String vehicleRegNo, String vehicle, String vehicletype, String sincefrom, String livedatetime, String driverName, String mobileNo, String vehicleStatus, String location, String latitude, String longitude, String acStatus, String gpsstatus, String speed, String ignition, String model) {
        this.vehicle_Id = vehicle_Id;
        VehicleRegNo = vehicleRegNo;
        Vehicle = vehicle;
        Vehicletype = vehicletype;
        Sincefrom = sincefrom;
        Livedatetime = livedatetime;
        DriverName = driverName;
        MobileNo = mobileNo;
        VehicleStatus = vehicleStatus;
        Location = location;
        Latitude = latitude;
        Longitude = longitude;
        AcStatus = acStatus;
        Gpsstatus = gpsstatus;
        Speed = speed;
        Ignition = ignition;
        Model = model;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getVehicle_Id() {
        return vehicle_Id;
    }

    public void setVehicle_Id(String vehicle_Id) {
        this.vehicle_Id = vehicle_Id;
    }

    public String getVehicleRegNo() {
        return VehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo) {
        VehicleRegNo = vehicleRegNo;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }

    public String getVehicletype() {
        return Vehicletype;
    }

    public void setVehicletype(String vehicletype) {
        Vehicletype = vehicletype;
    }

    public String getSincefrom() {
        return Sincefrom;
    }

    public void setSincefrom(String sincefrom) {
        Sincefrom = sincefrom;
    }

    public String getLivedatetime() {
        return Livedatetime;
    }

    public void setLivedatetime(String livedatetime) {
        Livedatetime = livedatetime;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getVehicleStatus() {
        return VehicleStatus;
    }

    public void setVehicleStatus(String vehicleStatus) {
        VehicleStatus = vehicleStatus;
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

    public String getAcStatus() {
        return AcStatus;
    }

    public void setAcStatus(String acStatus) {
        AcStatus = acStatus;
    }

    public String getGpsstatus() {
        return Gpsstatus;
    }

    public void setGpsstatus(String gpsstatus) {
        Gpsstatus = gpsstatus;
    }

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        Speed = speed;
    }

    public String getIgnition() {
        return Ignition;
    }

    public void setIgnition(String ignition) {
        Ignition = ignition;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String model) {
        Model = model;
    }
}
