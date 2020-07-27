package com.sdm.savior.model;

public class Alarm {

    private long alarmID;
    private long userID;
    private String name;

    public Alarm(long userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public Alarm(long alarmID, long userID, String name) {
        this.alarmID = alarmID;
        this.userID = userID;
        this.name = name;
    }

    public long getAlarmID() {
        return alarmID;
    }

    public void setAlarmID(long alarmID) {
        this.alarmID = alarmID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
