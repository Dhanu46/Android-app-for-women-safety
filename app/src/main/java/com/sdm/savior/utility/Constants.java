package com.sdm.savior.utility;


public class Constants {

    public static final String FIRST_LAUNCH = "FirstLaunch";

    // Error Messages
    public static final String MISSING_NAME = "Please enter your name";
    public static final String MISSING_PASSWORD = "Please enter your password";
    public static final String MISSING_PASSWORD_CONFIRMATION = "Please confirm your password";
    public static final String MISSING_MAIL = "Please enter your email address";
    public static final String INVALID_MAIL = "Invalid email address";
    public static final String MISSING_MOBILE = "Please enter your mobile number";
    public static final String INVALID_MOBILE = "Mobile number should be 10 digit number";
    public static final String INVALID_USER = "Wrong Credentials";
    public static final String DUPLICATE_USER_NAME = "User Name already exists. Please enter a different name";
    public static final String PASSWORD_MISMATCH = "Password does not match";
    public static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    public static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 2;
    public static final String UNREGISTERED_USER = "You are not registered with the app. Please register to continue";

    public static final String FORGOT_PASSWORD = "The registered password has been sent to your registered mobile number";
    public static final String MISSING_OLD_PASSWORD = "Please enter your old password";
    public static final String OLD_PASSWORD_INCORRECT = "Your old password is incorrect";
    public static final String MISSING_NEW_PASSWORD = "Please enter your new password";
    public static final String PASSWORD_CHANGED_SUCCESSFULLY = "Password is changed successfully";

    public static final String DEFAULT_SMS_TEXT = "I am in an emergency. I need help";
    public static final String PDF_VIEWER_NOT_FOUND = "Unable to find application to perform this action. Please install application from Play Store to view pdf files";

    public static final String SMS_SENT_TO_CONTACTS_SUCCESSFULLY = "SMS has been sent to your registered contacts successfuly";
    public static final int MINIMUM_PASSWORD_LENGTH = 8;
    public static final String INVALID_PASSWORD = "Password must contain minimum 8 characters including a number and special character";

    public static final String AMBULANCE = "ambulance";
    public static final String SIREN = "siren";
    public static final String POLICE = "police";

    public static final String SMS_SAVED_SUCCESSFULLY = "SMS text is saved successfully";
    public static final String ALARM_SET_SUCCESSFULLY = "This sound is set as your alarm successfully";
    public static final String THREE_CONTACTS_ALLOWED = "You can add only 3 contacts";
    public static final String NO_PHONE_NUMBER_FOR_CONTACT = "This contact doesn't have a phone number";
    public static final String NO_CONTACTS_ARE_CONFIGURED = "You have not added any contacts. Please add contacts";
    public static final String CONTACT_ADDED_SUCCESSFULLY = "This contact is added successfully";
    // Database version
    public static final int DATABASE_VERSION = 1;

    // Database name
    public static final String DATABASE_NAME = "Savior";

    // Database tables
    public static final String USER_TABLE_NAME = "User";
    public static final String SMS_TABLE_NAME = "SMS";
    public static final String CONTACT_TABLE_NAME = "Contacts";
    public static final String ALARM_TABLE_NAME = "Alarm";

    // Common column names
    public static final String ID_KEY = "_id";

    // User Table
    public static final String USER_NAME_KEY = "name";
    public static final String USER_PASSWORD_KEY = "password";
    public static final String USER_EMAIL_KEY = "email";
    public static final String USER_MOBILE_KEY = "mobile";

    // SMS Table
    public static final String SMS_TEXT_KEY = "text";
    public static final String USER_ID_KEY = "user_id";

    // Contacts Table
    public static final String CONTACT_NAME_KEY = "name";
    public static final String CONTACT_NUMBER_KEY = "number";

    // Alarm Table
    public static final String ALARM_NAME_KEY = "name";

    // App Description
    public static final String APP_DESCRIPTION = "Women Rescuer app is aimed at helping women for their safety and security in an emergency. It is a personal safety application that requires the name and number of the person who is to be contacted in times of emergency.";


}
