package com.sdm.savior.activitycontrollers;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sdm.savior.model.Alarm;
import com.sdm.savior.database.AppDatabaseHelper;
import com.sdm.savior.utility.AppInstance;
import com.sdm.savior.utility.Constants;
import com.sdm.savior.model.Contact;
import com.sdm.savior.model.ContactsList;
import com.sdm.savior.R;
import com.sdm.savior.model.SMS;
import com.sdm.savior.model.User;
import com.sdm.savior.utility.LocationTracker;
import com.sdm.savior.utility.ServiceCallbacks;
import com.sdm.savior.utility.ShakerService;
import com.sdm.savior.utility.Utility;

import java.io.File;
import java.util.ArrayList;

public class UserHomeActivity extends AppCompatActivity implements ServiceCallbacks{
    AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
    // LocationTracker class
    LocationTracker gps;
    private ShakerService shakerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        User user = ((AppInstance) getApplicationContext()).getCurrentUser();

        ContactsList.setContacts(databaseHelper.getAllContactsOfUserWithID(user.getUserID()));
        gps = new LocationTracker(this);
        // bind to Service
        Intent intent = new Intent(this, ShakerService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    /** Callbacks for service binding, passed to bindService() */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            ShakerService.LocalBinder binder = (ShakerService.LocalBinder) service;
            shakerService = binder.getService();
            shakerService.setCallbacks(UserHomeActivity.this); // register
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {

        }
    };

    /* Defined by ServiceCallbacks interface */
    @Override
    public void onDeviceShake() {
        shakerService.setCallbacks(null);
        call(null);
        shakerService.setCallbacks(UserHomeActivity.this);

    }


    public void sos(View view) {

        ArrayList<Contact> contacts = ContactsList.getContacts();
        if (contacts.size() > 0)
        {
            createSMS();
            callContact();
        }
        else
        {
            Toast.makeText(getApplicationContext(), Constants.NO_CONTACTS_ARE_CONFIGURED, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ConfigureContactsActivity.class);
            startActivity(i);
        }
    }

    public void call(View view) {

        ArrayList<Contact> contacts = ContactsList.getContacts();
        if (contacts.size() > 0)
        {
            callContact();
        }
        else {
            Toast.makeText(getApplicationContext(), Constants.NO_CONTACTS_ARE_CONFIGURED, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ConfigureContactsActivity.class);
            startActivity(i);
        }

    }

    public void sms(View view) {

        ArrayList<Contact> contacts = ContactsList.getContacts();
        if (contacts.size() > 0)
        {
            createSMS();
        }
        else
        {
            Toast.makeText(getApplicationContext(), Constants.NO_CONTACTS_ARE_CONFIGURED, Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ConfigureContactsActivity.class);
            startActivity(i);
        }

    }

    public void settings(View view) {
        Intent i = new Intent(this, UserPreferencesActivity.class);
        startActivity(i);
    }

    public void alarm(View view) {
        User user = ((AppInstance) getApplicationContext()).getCurrentUser();

        Alarm alarm = databaseHelper.getAlarmOfUserWithID(user.getUserID());

        MediaPlayer player = null;
        if (alarm == null) {
            player = MediaPlayer.create(this, R.raw.ambulance);
        } else {

            int resID = getResources().getIdentifier(alarm.getName(), "raw", getPackageName());

            player = MediaPlayer.create(this, resID);
        }
        player.start();

    }

    public void createSMS() {
        User user = ((AppInstance) getApplicationContext()).getCurrentUser();

        SMS sms = databaseHelper.getSMSOfUserWithID(user.getUserID());
        String smsText = null;

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            if (sms == null) {
                smsText = Constants.DEFAULT_SMS_TEXT + ". My location is: https://maps.google.com/?q=" + latitude + "," + longitude + "\nSent by Women Rescuer App";
            } else {
                smsText = sms.getText() + ". My location is: https://maps.google.com/?q=" + latitude + "," + longitude + "\nSent by Women Rescuer App";
            }
            ArrayList<Contact> contacts = ContactsList.getContacts();

            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED)
            {

                // Permission is not granted
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(UserHomeActivity.this,
                        Manifest.permission.SEND_SMS))
                {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                }
                else
                {

                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(UserHomeActivity.this,
                            new String[]{Manifest.permission.SEND_SMS},
                            Constants.MY_PERMISSIONS_REQUEST_SEND_SMS);

                    // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
            else
            {
                for (Contact contact : contacts)
                {
                    sendSms(contact.getNumber(), smsText);
                    // Permission has already been granted
                }
                SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(this);

                if (mypref.contains(Constants.CONTACT_NUMBER_KEY)) {
                    sendSms(mypref.getString(Constants.CONTACT_NUMBER_KEY, null), smsText);
                }

                Toast.makeText(getApplicationContext(), Constants.SMS_SENT_TO_CONTACTS_SUCCESSFULLY, Toast.LENGTH_LONG).show();
            }

        }
        else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_SEND_SMS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    User user = ((AppInstance) getApplicationContext()).getCurrentUser();

                    SMS sms = databaseHelper.getSMSOfUserWithID(user.getUserID());
                    String smsText = null;
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    if (sms == null) {
                        smsText = Constants.DEFAULT_SMS_TEXT + ". My location is: https://maps.google.com/?q=" + latitude + "," + longitude + "\nSent by Women Rescuer App";
                    } else {
                        smsText = sms.getText() + ". My location is: https://maps.google.com/?q=" + latitude + "," + longitude + "\nSent by Women Rescuer App";
                    }
                    ArrayList<Contact> contacts = ContactsList.getContacts();

                    for (Contact contact : contacts) {
                        sendSms(contact.getNumber(), smsText);
                    }

                    SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(this);

                    if (mypref.contains(Constants.CONTACT_NUMBER_KEY)) {
                        sendSms(mypref.getString(Constants.CONTACT_NUMBER_KEY, null), smsText);
                    }

                    Toast.makeText(getApplicationContext(), Constants.SMS_SENT_TO_CONTACTS_SUCCESSFULLY, Toast.LENGTH_LONG).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case Constants.MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ArrayList<Contact> contacts = ContactsList.getContacts();

                    if (contacts.size() > 0) {
                        String number = contacts.get(0).getNumber();
                        String call = "tel:" + number;

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse(call));
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(callIntent);
                    }
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                    // other 'case' lines to check for other
                    // permissions this app might request.
                }
            }
        }
    }

    private void callContact()
    {
        ArrayList<Contact> contacts = ContactsList.getContacts();

        String number = contacts.get(0).getNumber();
        String call = "tel:" + number;

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UserHomeActivity.this,
                    Manifest.permission.CALL_PHONE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(UserHomeActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        Constants.MY_PERMISSIONS_REQUEST_CALL_PHONE);

                // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse(call));
            startActivity(callIntent);
            // Permission has already been granted
        }

    }

    private void sendSms(String phonenumber, String message)
    {
        SmsManager manager = SmsManager.getDefault();

        int length = message.length();

        if(length > 160)
        {
            ArrayList<String> messagelist = manager.divideMessage(message);

            manager.sendMultipartTextMessage(phonenumber, null, messagelist, null, null);
        }
        else
        {
            manager.sendTextMessage(phonenumber, null, message, null, null);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sub_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.fakeCall:
                Intent fakeCallIntent = new Intent(this, FakeCallActivity.class);
                startActivity(fakeCallIntent);

                return true;

            case R.id.about:
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(this);
                dialogBuilder.setIcon(R.drawable.savior);
                dialogBuilder.setTitle(R.string.app_name);
                dialogBuilder.setMessage(Constants.APP_DESCRIPTION);
                dialogBuilder.create();
                dialogBuilder.show();
                return true;

            case R.id.help:
                android.app.AlertDialog.Builder helpDialogBuilder = new android.app.AlertDialog.Builder(this);
                helpDialogBuilder.setIcon(R.drawable.savior);
                helpDialogBuilder.setTitle(R.string.app_name);
                helpDialogBuilder.setMessage("Please mail to \nwomenrescuer123@gmail.com\nfor any help regarding the app.");
                helpDialogBuilder.create();
                helpDialogBuilder.show();
                return true;


            case R.id.feedback:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Women Rescuer App Feedback");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"womenrescuer123@gmail.com"});

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send feedback..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.logout:
                ((AppInstance)getApplicationContext()).setCurrentUser(null);
                ContactsList.clearContacts();
                Intent i = new Intent(this, UserLoginActivity.class);
                startActivity(i);
                finish();
                return true;

            case R.id.changePassword:
                Intent passwordIntent = new Intent(this, ChangePasswordActivity.class);
                startActivity(passwordIntent);
                return true;

            case R.id.manual:

                File fileBrochure = new File(Environment.getExternalStorageDirectory() + "/" + "sd.pdf");
                if (!fileBrochure.exists())
                {
                    Utility.copyPDFFileToExternalStorage(this);
                }

                /** PDF reader code */
                File file = new File(Environment.getExternalStorageDirectory() + "/" + "sd.pdf");

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file),"application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try
                {
                    getApplicationContext().startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(this, Constants.PDF_VIEWER_NOT_FOUND, Toast.LENGTH_SHORT).show();
                }

        }
        return false;
    }

    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

}


