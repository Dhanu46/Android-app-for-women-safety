package com.sdm.savior.activitycontrollers;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sdm.savior.database.AppDatabaseHelper;
import com.sdm.savior.utility.AppInstance;
import com.sdm.savior.utility.Constants;
import com.sdm.savior.R;
import com.sdm.savior.model.User;
import com.sdm.savior.utility.Utility;

import java.io.File;
import java.util.ArrayList;


public class UserLoginActivity extends AppCompatActivity
{
    private EditText nameET, passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        nameET = (EditText) findViewById(R.id.userName);
        passwordET = (EditText) findViewById(R.id.password);
    }

    public void login(View view)
    {
        String userName = nameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (userName.length() == 0)
        {
            nameET.setError(Constants.MISSING_NAME);
            nameET.requestFocus();
        }
        else if (password.length() == 0)
        {
            passwordET.setError(Constants.MISSING_PASSWORD);
            passwordET.requestFocus();
        }
        else {
            // Validate credentials
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            User user = databaseHelper.getUser(userName, password);
            if (user == null) {
                Toast.makeText(getApplicationContext(), Constants.INVALID_USER, Toast.LENGTH_SHORT).show();
            } else {
                ((AppInstance) getApplicationContext()).setCurrentUser(user);
                Intent i = new Intent(this, UserHomeActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    public void forgotPassword(View view)
    {
        String userName = nameET.getText().toString().trim();

        if (userName.length() == 0)
        {
            nameET.setError(Constants.MISSING_NAME);
            nameET.requestFocus();
        }
        else {
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            User user = databaseHelper.getUserWithName(userName);
            if (user == null) {
                Toast.makeText(getApplicationContext(), Constants.UNREGISTERED_USER, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), Constants.FORGOT_PASSWORD,Toast.LENGTH_LONG).show();
                final String passwordString = "Hello " + user.getName() + ". \nYour registered password is: " + user.getPassword() + "\nTeam Savior";

                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED)
                {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.SEND_SMS))
                    {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    }
                    else
                    {

                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.SEND_SMS},
                                Constants.MY_PERMISSIONS_REQUEST_SEND_SMS);

                        // MY_PERMISSIONS_REQUEST_SEND_SMS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
                else
                {
                        sendSms(user.getMobile(), passwordString);
                        // Permission has already been granted
                }


            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.MY_PERMISSIONS_REQUEST_SEND_SMS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String userName = nameET.getText().toString().trim();
                    AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
                    User user = databaseHelper.getUserWithName(userName);

                    final String passwordString = "Hello " + user.getName() + ". \nYour registered password is: " + user.getPassword() + "\nTeam Savior";

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    sendSms(user.getMobile(), passwordString);

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
        }
    }

    public void registerUser(View view)
    {
        Intent i = new Intent(this, UserRegistrationActivity.class);
        startActivity(i);
        // close this activity
        finish();
    }

    public boolean onCreateOptionsMenu(android.view.Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
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
                emailIntent.putExtra(Intent.EXTRA_TEXT, new String[]{"mail body"});


                try {
                    startActivity(Intent.createChooser(emailIntent, "Send feedback..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
                }
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

    public void onBackPressed()
    {
        moveTaskToBack(true);
    }

}
