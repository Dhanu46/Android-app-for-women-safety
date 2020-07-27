package com.sdm.savior.activitycontrollers;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdm.savior.database.AppDatabaseHelper;
import com.sdm.savior.utility.AppInstance;
import com.sdm.savior.utility.Constants;
import com.sdm.savior.model.Contact;
import com.sdm.savior.model.ContactsList;
import com.sdm.savior.R;
import com.sdm.savior.model.User;
import com.sdm.savior.utility.Utility;

import java.io.File;


public class ConfigureContactsActivity extends AppCompatActivity {

    LinearLayout contactOneLL, contactTwoLL, contactThreeLL;
    TextView contactOneTV, contactTwoTV, contactThreeTV;
    private EditText nameET, mobileET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configurecontacts);
        initializeUIComponents();
        loadContacts();
    }

    public void initializeUIComponents()
    {
        nameET = (EditText) findViewById(R.id.name);
        mobileET = (EditText) findViewById(R.id.mobile);

        contactOneLL = (LinearLayout) findViewById(R.id.contactOneLayout) ;
        contactTwoLL = (LinearLayout) findViewById(R.id.contactTwoLayout) ;
        contactThreeLL = (LinearLayout) findViewById(R.id.contactThreeLayout) ;
        contactOneTV = (TextView) findViewById(R.id.contactOne);
        contactTwoTV = (TextView) findViewById(R.id.contactTwo);
        contactThreeTV  = (TextView) findViewById(R.id.contactThree);
    }

    public void addContact(View view) {
        if (ContactsList.count() == 3)
        {
            Toast.makeText(getApplicationContext(), Constants.THREE_CONTACTS_ALLOWED, Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 123);

        }

    }

    public void addContactManually(View view)
    {
        String name = nameET.getText().toString().trim();
        String mobile = mobileET.getText().toString().trim();

        if (name.length() == 0) {
            nameET.setError(Constants.MISSING_NAME);
            nameET.requestFocus();
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
            SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor prefsEditr = mypref.edit();
            prefsEditr.putString(Constants.CONTACT_NAME_KEY, name);
            prefsEditr.putString(Constants.CONTACT_NUMBER_KEY, mobile);
            prefsEditr.commit();
            Toast.makeText(getApplicationContext(), Constants.CONTACT_ADDED_SUCCESSFULLY, Toast.LENGTH_LONG).show();

        }
    }

    @Override public void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);
        switch(reqCode)
        {
            case (123):
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri contactData = data.getData();
                    Cursor c = this.getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToFirst())
                    {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                        String hasPhone =
                                c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));

                        if (hasPhone.equalsIgnoreCase("1"))
                        {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                            phones.moveToFirst();
                            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            User user = ((AppInstance) getApplicationContext()).getCurrentUser();

                            Contact contact = new Contact(user.getUserID(), name, number);
                            AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
                            long contactID = databaseHelper.addContact(contact);
                            contact.setContactID(contactID);
                            ContactsList.addContact(contact);
                            updateUI();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), Constants.NO_PHONE_NUMBER_FOR_CONTACT, Toast.LENGTH_LONG).show();

                        }
                    }
                }
        }
    }

    public void loadContacts()
    {
        Contact contact = null;

        if (ContactsList.count() == 1 )
        {
            contact = ContactsList.getContactAtIndex(0);
            contactOneTV.setText("Name: " + contact.getName() + "\nNumber: " + contact.getNumber());
            contactOneLL.setVisibility(View.VISIBLE);
        }
        if (ContactsList.count() == 2 )
        {
            contact = ContactsList.getContactAtIndex(0);
            contactOneTV.setText("Name: " + contact.getName() + "\nNumber: " + contact.getNumber());
            contactOneLL.setVisibility(View.VISIBLE);

            contact = ContactsList.getContactAtIndex(1);
            contactTwoTV.setText("Name: " + contact.getName() + "\nNumber: " + contact.getNumber());
            contactTwoLL.setVisibility(View.VISIBLE);
        }
        if (ContactsList.count() == 3 )
        {
            contact = ContactsList.getContactAtIndex(0);
            contactOneTV.setText("Name: " + contact.getName() + "\nNumber: " + contact.getNumber());
            contactOneLL.setVisibility(View.VISIBLE);

            contact = ContactsList.getContactAtIndex(1);
            contactTwoTV.setText("Name: " + contact.getName() + "\nNumber: " + contact.getNumber());
            contactTwoLL.setVisibility(View.VISIBLE);

            contact = ContactsList.getContactAtIndex(2);
            contactThreeTV.setText("Name: " + contact.getName() + "\nNumber: " + contact.getNumber());
            contactThreeLL.setVisibility(View.VISIBLE);
        }
        SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(this);

        if (mypref.contains(Constants.CONTACT_NAME_KEY)) {
            nameET.setText(mypref.getString(Constants.CONTACT_NAME_KEY, null));
        }
        if (mypref.contains(Constants.CONTACT_NUMBER_KEY)) {
            mobileET.setText(mypref.getString(Constants.CONTACT_NUMBER_KEY, null));
        }

    }

    public void updateUI()
    {
        Contact contact = null;
        if (ContactsList.count() == 1 )
        {
            contact = ContactsList.getContactAtIndex(0);
            contactOneTV.setText("Name: " + contact.getName() + "\nNumber: " + contact.getNumber());
            contactOneLL.setVisibility(View.VISIBLE);
        }
        else if (ContactsList.count() == 2 )
        {
            contact = ContactsList.getContactAtIndex(1);
            contactTwoTV.setText("Name: " + contact.getName() + "\nNumber: " + contact.getNumber());
            contactTwoLL.setVisibility(View.VISIBLE);
        }
        else if (ContactsList.count() == 3 )
        {
            contact = ContactsList.getContactAtIndex(2);
            contactThreeTV.setText("Name: " + contact.getName() + "\nNumber: " + contact.getNumber());
            contactThreeLL.setVisibility(View.VISIBLE);
        }

    }

    public void deleteContact(View view)
    {
        int id = view.getId();
        long contactID = 0;
        switch (id)
        {
            case R.id.deleteOne:
                contactID = ContactsList.getContactAtIndex(0).getContactID();
                ContactsList.deleteContactAtIndex(0);
                contactOneTV.setText("");
                contactOneLL.setVisibility(View.GONE);
                break;

            case R.id.deleteTwo:
                if (ContactsList.count() == 1 )
                {
                    contactID = ContactsList.getContactAtIndex(0).getContactID();
                    ContactsList.deleteContactAtIndex(0);
                }
                else
                {
                    contactID = ContactsList.getContactAtIndex(1).getContactID();
                    ContactsList.deleteContactAtIndex(1);
                }
                contactTwoTV.setText("");
                contactTwoLL.setVisibility(View.GONE);
                break;
            case R.id.deleteThree:
                if (ContactsList.count() == 1 )
                {
                    contactID = ContactsList.getContactAtIndex(0).getContactID();
                    ContactsList.deleteContactAtIndex(0);
                }
                else if (ContactsList.count() == 2 )
                {
                    contactID = ContactsList.getContactAtIndex(1).getContactID();
                    ContactsList.deleteContactAtIndex(1);
                }
                else
                {
                    contactID = ContactsList.getContactAtIndex(2).getContactID();
                    ContactsList.deleteContactAtIndex(2);
                }
                contactThreeTV.setText("");
                contactThreeLL.setVisibility(View.GONE);
                break;

        }
        AppDatabaseHelper databaseHelper = new AppDatabaseHelper(this);
        databaseHelper.deleteContactWithID(contactID);

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

            case R.id.changePassword:
                Intent passwordIntent = new Intent(this, ChangePasswordActivity.class);
                startActivity(passwordIntent);
                return true;

            case R.id.logout:
                ((AppInstance)getApplicationContext()).setCurrentUser(null);
                ContactsList.clearContacts();
                Intent i = new Intent(this, UserLoginActivity.class);
                startActivity(i);
                finish();
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
}