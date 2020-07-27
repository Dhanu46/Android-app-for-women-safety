package com.sdm.savior.activitycontrollers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sdm.savior.R;
import com.sdm.savior.database.AppDatabaseHelper;
import com.sdm.savior.model.User;
import com.sdm.savior.utility.AppInstance;
import com.sdm.savior.utility.Constants;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangePasswordActivity extends AppCompatActivity
{
    private EditText oldPasswordET, newPasswordET,  confirmNewPasswordET;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        initializeUIComponents();
    }

    public void initializeUIComponents()
    {
        oldPasswordET = (EditText) findViewById(R.id.oldPassword);
        newPasswordET = (EditText) findViewById(R.id.newPassword);
        confirmNewPasswordET = (EditText) findViewById(R.id.confirmNewPassword);
    }

    public void changePassword(View view)
    {
        String oldPassword = oldPasswordET.getText().toString().trim();
        String newPassword = newPasswordET.getText().toString().trim();
        String confirmNewPassword = confirmNewPasswordET.getText().toString().trim();

        User user = ((AppInstance)getApplicationContext()).getCurrentUser();

        if (oldPassword.length() == 0) {
             oldPasswordET.setError(Constants.MISSING_OLD_PASSWORD);
             oldPasswordET.requestFocus();
        }
        else if (false == user.getPassword().equals(oldPassword))
        {
            oldPasswordET.setError(Constants.OLD_PASSWORD_INCORRECT);
            oldPasswordET.requestFocus();
        }
        else if(newPassword.length() == 0)
        {
            newPasswordET.setError(Constants.MISSING_NEW_PASSWORD);
            newPasswordET.requestFocus();
        }
        else if(newPassword.length()<Constants.MINIMUM_PASSWORD_LENGTH &&!isValidPassword(newPassword))
        {
            newPasswordET.setError(Constants.INVALID_PASSWORD);
            newPasswordET.requestFocus();
        }
        else if (confirmNewPassword.length() == 0) {
            confirmNewPasswordET.setError(Constants.MISSING_PASSWORD_CONFIRMATION);
            confirmNewPasswordET.requestFocus();
        }
        else if (!newPassword.equals(confirmNewPassword)) {
            confirmNewPasswordET.setError(Constants.PASSWORD_MISMATCH);
            confirmNewPasswordET.requestFocus();
        }
        else
        {
            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
            databaseHelper.updatePasswordForUser(newPassword, user.getUserID());
            Toast.makeText(this, Constants.PASSWORD_CHANGED_SUCCESSFULLY, Toast.LENGTH_SHORT).show();

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
}
