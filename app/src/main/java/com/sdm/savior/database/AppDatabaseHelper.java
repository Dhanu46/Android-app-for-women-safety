package com.sdm.savior.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sdm.savior.utility.Constants;
import com.sdm.savior.model.Alarm;
import com.sdm.savior.model.Contact;
import com.sdm.savior.model.SMS;
import com.sdm.savior.model.User;

import java.util.ArrayList;

public class AppDatabaseHelper extends SQLiteOpenHelper {

    public AppDatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USER_TABLE = "CREATE TABLE " + Constants.USER_TABLE_NAME + "(" +
                Constants.ID_KEY + " INTEGER PRIMARY KEY," +
                Constants.USER_NAME_KEY + " TEXT," +
                Constants.USER_PASSWORD_KEY + " TEXT," +
                Constants.USER_EMAIL_KEY + " TEXT," +
                Constants.USER_MOBILE_KEY + " TEXT" + ")";

        String CREATE_SMS_TABLE = "CREATE TABLE " + Constants.SMS_TABLE_NAME + "(" +
                Constants.ID_KEY + " INTEGER PRIMARY KEY," +
                Constants.SMS_TEXT_KEY + " TEXT," +
                Constants.USER_ID_KEY + " INTEGER" + ")";

        String CREATE_CONTACT_TABLE = "CREATE TABLE " + Constants.CONTACT_TABLE_NAME + "(" +
                Constants.ID_KEY + " INTEGER PRIMARY KEY," +
                Constants.CONTACT_NAME_KEY + " TEXT," +
                Constants.CONTACT_NUMBER_KEY + " TEXT," +
                Constants.USER_ID_KEY + " INTEGER" + ")";

        String CREATE_ALARM_TABLE = "CREATE TABLE " + Constants.ALARM_TABLE_NAME + "(" +
                Constants.ID_KEY + " INTEGER PRIMARY KEY," +
                Constants.ALARM_NAME_KEY + " TEXT," +
                Constants.USER_ID_KEY + " INTEGER" + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_SMS_TABLE);
        db.execSQL(CREATE_CONTACT_TABLE);
        db.execSQL(CREATE_ALARM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Getting user given username and password. This is useful in login screen
    public User getUser(String userName, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_TABLE_NAME;
        String[] columns = new String[]{Constants.ID_KEY,
                Constants.USER_NAME_KEY, Constants.USER_EMAIL_KEY, Constants.USER_MOBILE_KEY};
        String where = Constants.USER_NAME_KEY + " =?" + " AND " + Constants.USER_PASSWORD_KEY + " =?";

        Cursor cursor = db.query(table, columns, where,
                new String[]{userName, password}, null, null, null, null);
        User user = null;
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            long userID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));
            String userMail = cursor.getString(cursor.getColumnIndex(Constants.USER_EMAIL_KEY));
            String userMobile = cursor.getString(cursor.getColumnIndex(Constants.USER_MOBILE_KEY));

            user = new User(userID, userName, password, userMail, userMobile);
        }

        db.close();
        return user;
    }

    public User getUserWithName(String userName)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_TABLE_NAME;
        String[] columns = new String[]{Constants.ID_KEY,
                Constants.USER_NAME_KEY, Constants.USER_PASSWORD_KEY, Constants.USER_EMAIL_KEY, Constants.USER_MOBILE_KEY};
        String where = Constants.USER_NAME_KEY + " =?" ;

        Cursor cursor = db.query(table, columns, where,
                new String[]{userName}, null, null, null, null);
        User user = null;
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            long userID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));
            String userMail = cursor.getString(cursor.getColumnIndex(Constants.USER_EMAIL_KEY));
            String password = cursor.getString(cursor.getColumnIndex(Constants.USER_PASSWORD_KEY));

            String userMobile = cursor.getString(cursor.getColumnIndex(Constants.USER_MOBILE_KEY));

            user = new User(userID, userName, password, userMail, userMobile);
        }

        db.close();
        return user;
    }

    public long addUser(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Inserting Row to User table
        values.put(Constants.USER_NAME_KEY, user.getName());
        values.put(Constants.USER_PASSWORD_KEY, user.getPassword());
        values.put(Constants.USER_EMAIL_KEY, user.getMail());
        values.put(Constants.USER_MOBILE_KEY, user.getMobile());

        long user_id = db.insert(Constants.USER_TABLE_NAME, null, values);

        db.close();
        return user_id;
    }

    public long addSMS(SMS sms)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Inserting Row to User table
        values.put(Constants.SMS_TEXT_KEY, sms.getText());
        values.put(Constants.USER_ID_KEY, sms.getUserID());

        long smsID = db.insert(Constants.SMS_TABLE_NAME, null, values);

        db.close();
        return smsID;
    }

    public long addAlarm(Alarm alarm)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Inserting Row to User table
        values.put(Constants.ALARM_NAME_KEY, alarm.getName());
        values.put(Constants.USER_ID_KEY, alarm.getUserID());

        long alarmID = db.insert(Constants.ALARM_TABLE_NAME, null, values);

        db.close();
        return alarmID;
    }

    public long addContact(Contact contact)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Inserting Row to User table
        values.put(Constants.CONTACT_NAME_KEY, contact.getName());
        values.put(Constants.CONTACT_NUMBER_KEY, contact.getNumber());
        values.put(Constants.USER_ID_KEY, contact.getUserID());

        long contactID = db.insert(Constants.CONTACT_TABLE_NAME, null, values);

        db.close();
        return contactID;
    }

    // This method returns the list of all the usernames in the database. It is used to check if a newly entered user name
    // already exists in the database.
    public ArrayList<String> getAllUserNames()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_TABLE_NAME;
        String[] columns = new String[]{Constants.USER_NAME_KEY};

        Cursor cursor = db.query(table, columns, null,
                null, null, null, null, null);

        ArrayList<String> userNames = new ArrayList<String>();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(Constants.USER_NAME_KEY));
                userNames.add(name);
            } while (cursor.moveToNext());
        }

        db.close();
        return userNames;
    }

    public ArrayList<Contact> getAllContactsOfUserWithID(long userID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.CONTACT_TABLE_NAME;
        String[] columns = new String[]{Constants.CONTACT_NAME_KEY, Constants.CONTACT_NUMBER_KEY, Constants.ID_KEY};
        String where_clause = Constants.USER_ID_KEY + " =?";

        Cursor cursor = db.query(table, columns, where_clause,
                new String[]{String.valueOf(userID)}, null, null, null, null);

        ArrayList<Contact> contacts = new ArrayList();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(Constants.CONTACT_NAME_KEY));
                String number = cursor.getString(cursor.getColumnIndex(Constants.CONTACT_NUMBER_KEY));
                long contactID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

                Contact contact = new Contact(contactID, userID, name, number);
                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        return contacts;
    }

    public SMS getSMSOfUserWithID(long userID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.SMS_TABLE_NAME;
        String[] columns = new String[]{Constants.SMS_TEXT_KEY, Constants.ID_KEY};
        String where_clause = Constants.USER_ID_KEY + " =?";

        Cursor cursor = db.query(table, columns, where_clause,
                new String[]{String.valueOf(userID)}, null, null, null, null);
        SMS sms = null;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
                String text = cursor.getString(cursor.getColumnIndex(Constants.SMS_TEXT_KEY));
                long smsID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

                sms = new SMS(smsID, userID, text);
        }

        db.close();
        return sms;
    }


    public Alarm getAlarmOfUserWithID(long userID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.ALARM_TABLE_NAME;
        String[] columns = new String[]{Constants.ALARM_NAME_KEY, Constants.ID_KEY};
        String where_clause = Constants.USER_ID_KEY + " =?";

        Cursor cursor = db.query(table, columns, where_clause,
                new String[]{String.valueOf(userID)}, null, null, null, null);
        Alarm alarm = null;
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            String name = cursor.getString(cursor.getColumnIndex(Constants.ALARM_NAME_KEY));
            long alarmID = cursor.getLong(cursor.getColumnIndex(Constants.ID_KEY));

            alarm = new Alarm(alarmID, userID, name);
        }

        db.close();
        return alarm;
    }

    public String getUserNameWithID(long userID)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String table = Constants.USER_TABLE_NAME;
        String[] columns = new String[]{Constants.USER_NAME_KEY};
        String where_clause = Constants.ID_KEY + " =?";

        Cursor cursor = db.query(table, columns, where_clause,
                new String[]{String.valueOf(userID)}, null, null, null, null);

        String userName = null;
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            userName = cursor.getString(cursor.getColumnIndex(Constants.USER_NAME_KEY));
        }

        db.close();
        return userName;
    }

    public void deleteContactWithID(long contactID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.CONTACT_TABLE_NAME, Constants.ID_KEY + "=?",
                new String[]{String.valueOf(contactID)});
        db.close();
    }

    public int updatePasswordForUser(String newPassword, long userID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.USER_PASSWORD_KEY , newPassword);

        // Updating row
        return db.update(Constants.USER_TABLE_NAME, values, Constants.ID_KEY + "=?",
                new String[]{String.valueOf(userID)});
    }

}