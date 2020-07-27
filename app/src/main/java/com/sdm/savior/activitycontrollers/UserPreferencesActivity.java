package com.sdm.savior.activitycontrollers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sdm.savior.utility.AppInstance;
import com.sdm.savior.utility.Constants;
import com.sdm.savior.model.ContactsList;
import com.sdm.savior.R;
import com.sdm.savior.utility.Utility;

import java.io.File;


public class UserPreferencesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpreferences);
    }

    public void configureContacts(View view)
    {
        Intent i = new Intent(this, ConfigureContactsActivity.class);
        startActivity(i);
    }

    public void configureSMS(View view)
    {
        Intent i = new Intent(this, ConfigureSMSActivity.class);
        startActivity(i);
    }

    public void configureAlarm(View view)
    {
        Intent i = new Intent(this, ConfigureAlarmActivity.class);
        startActivity(i);
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
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                return true;


            case R.id.manual:

                File fileBrochure = new File(Environment.getExternalStorageDirectory() + "/" + "sd.pdf");
                if (!fileBrochure.exists())
                {
                    Utility.copyPDFFileToExternalStorage(this);
                }

                /** PDF reader code */
                File file = new File(Environment.getExternalStorageDirectory() + "/" + "sd.pdf");

                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(Uri.fromFile(file),"application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try
                {
                    getApplicationContext().startActivity(pdfIntent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(this, Constants.PDF_VIEWER_NOT_FOUND, Toast.LENGTH_SHORT).show();
                }

        }
        return false;
    }

}
