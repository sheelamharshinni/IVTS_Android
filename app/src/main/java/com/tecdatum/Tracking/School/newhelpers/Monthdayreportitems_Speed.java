package com.tecdatum.Tracking.School.newhelpers;

public class Monthdayreportitems_Speed {
    String Speed, Date, Time, Location;

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        Speed = speed;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public Monthdayreportitems_Speed(String speed, String date, String time, String location) {
        Speed = speed;
        Date = date;
        Time = time;
        Location = location;
    }
}
