package com.tecdatum.Tracking.School.newhelpers;

public class Monthdayreportitems_Idle {
    String tv_vehcle, vehicleName, VehicleType, Location,StartDate,StartTime,EndDate,EndTime,DurationinHours,DurationinMins;

    public Monthdayreportitems_Idle(String tv_vehcle, String vehicleName, String vehicleType, String location, String startDate, String startTime, String endDate, String endTime, String durationinHours, String durationinMins) {
        this.tv_vehcle = tv_vehcle;
        this.vehicleName = vehicleName;
        VehicleType = vehicleType;
        Location = location;
        StartDate = startDate;
        StartTime = startTime;
        EndDate = endDate;
        EndTime = endTime;
        DurationinHours = durationinHours;
        DurationinMins = durationinMins;
    }

    public String getTv_vehcle() {
        return tv_vehcle;
    }

    public void setTv_vehcle(String tv_vehcle) {
        this.tv_vehcle = tv_vehcle;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getDurationinHours() {
        return DurationinHours;
    }

    public void setDurationinHours(String durationinHours) {
        DurationinHours = durationinHours;
    }

    public String getDurationinMins() {
        return DurationinMins;
    }

    public void setDurationinMins(String durationinMins) {
        DurationinMins = durationinMins;
    }
}
