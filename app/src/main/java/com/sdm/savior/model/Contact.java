package com.sdm.savior.model;

public class Contact {

    private long contactID;
    private long userID;
    private String name;
    private String number;

    public Contact(long userID, String name, String number) {
        this.userID = userID;
        this.name = name;
        this.number = number;
    }

    public Contact(long contactID, long userID, String name, String number) {

        this.contactID = contactID;
        this.userID = userID;
        this.name = name;
        this.number = number;
    }

    public long getContactID() {
        return contactID;
    }

    public void setContactID(long contactID) {
        this.contactID = contactID;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
