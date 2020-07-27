package com.sdm.savior.model;

public class SMS
{
    private long smsID;
    private long userID;
    private String text;

    public long getSmsID() {
        return smsID;
    }

    public void setSmsID(long smsID) {
        this.smsID = smsID;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public SMS(long smsID, long userID, String text) {

        this.smsID = smsID;
        this.userID = userID;
        this.text = text;
    }

    public SMS(long userID, String text) {

        this.userID = userID;
        this.text = text;
    }
}
