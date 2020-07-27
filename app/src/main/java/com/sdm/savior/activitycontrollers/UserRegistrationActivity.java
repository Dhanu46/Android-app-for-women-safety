package com.sdm.savior.activitycontrollers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegistrationActivity extends AppCompatActivity
{
    private EditText nameET, passwordET, confirmPasswordET, mailET, mobileET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        nameET = (EditText) findViewById(R.id.userName);
        passwordET = (EditText) findViewById(R.id.password);
        confirmPasswordET = (EditText) findViewById(R.id.confirmPassword);
        mailET = (EditText) findViewById(R.id.mail);
        mobileET = (EditText) findViewById(R.id.mobile);
    }

    public void registerUser(View view)
    {
        String userName = nameET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String confirmPassword = confirmPasswordET.getText().toString().trim();
        String mail = mailET.getText().toString().trim();
        String mobile = mobileET.getText().toString().trim();

        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        ArrayList<String> userNames = databaseHelper.getAllUserNames();

        if (userName.length() == 0) {
            nameET.setError(Constants.MISSING_NAME);
            nameET.requestFocus();
        }
        else if (userNames.size()>0 && userNames.contains(userName))
        {
            nameET.setError(Constants.DUPLICATE_USER_NAME);
            nameET.requestFocus();
        }
        else if (password.length() == 0) {
            passwordET.setError(Constants.MISSING_PASSWORD);
            passwordET.requestFocus();
        }
        else if(password.length() == 0)
        {
            passwordET.setError(Constants.MISSING_PASSWORD);
            passwordET.requestFocus();
        }
        else if(password.length()<Constants.MINIMUM_PASSWORD_LENGTH &&!isValidPassword(password))
        {
            passwordET.setError(Constants.INVALID_PASSWORD);
            passwordET.requestFocus();
        }
        else if (confirmPassword.length() == 0) {
            confirmPasswordET.setError(Constants.MISSING_PASSWORD_CONFIRMATION);
            confirmPasswordET.requestFocus();
        }
        else if (!password.equals(confirmPassword)) {
            confirmPasswordET.setError(Constants.PASSWORD_MISMATCH);
            confirmPasswordET.requestFocus();
        }
        else if (TextUtils.isEmpty(mail))  {
            mailET.setError(Constants.MISSING_MAIL);
            mailET.requestFocus();
        }
        else if(false == android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            mailET.setError(Constants.INVALID_MAIL);
            mailET.requestFocus();
        }
        else if (mobile.length() == 0) {
            mobileET.setError(Constants.MISSING_MOBILE);
            mobileET.requestFocus();
        }
        else if (mobile.length() != 10) {
            mobileET.setError(Constants.INVALID_MOBILE);
            mobileET.requestFocus();
        }
        else
        {
            User user = new User(userName, password, mail, mobile);
            long user_id = databaseHelper.addUser(user);
            user.setUserID(user_id);
            ((AppInstance)getApplicationContext()).setCurrentUser(user);

            SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor prefsEditr = mypref.edit();
            prefsEditr.putString(Constants.FIRST_LAUNCH, "Savior");
            prefsEditr.commit();

            Intent i = new Intent(this, UserHomeActivity.class);
            startActivity(i);
            // close this activity
            finish();
        }
    }

    public static boolean isValidPassword(final String password)
    {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public boolean onCreateOptionsMenu(Menu menu)
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

                return true;

            case R.id.feedback:

                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address"  , new String ("8904562692"));
                smsIntent.putExtra("sms_body"  , "Hi, How are you");
                try {
                    startActivity(smsIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There are no sms clients installed.", Toast.LENGTH_LONG).show();
                }
//                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
//                emailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Women Rescuer App Feedback");
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"womenrescuer123@gmail.com"});
//
//                try {
//                    startActivity(Intent.createChooser(emailIntent, "Send feedback..."));
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_LONG).show();
//                }


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
