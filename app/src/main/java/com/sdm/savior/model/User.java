package com.sdm.savior.model;


public class User
{
    private long userID;
    private String name;
    private String password;
    private String mail;
    private String mobile;

    public User(String name, String password, String mail, String mobile) {
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.mobile = mobile;
    }

    public User(long userID, String name, String password, String mail, String mobile) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.mobile = mobile;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
