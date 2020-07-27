package com.sdm.savior.activitycontrollers;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.robotium.solo.Solo;
import com.sdm.savior.R;

import static org.junit.Assert.*;

public class UserLoginActivityTest extends ActivityInstrumentationTestCase2<UserLoginActivity> {

    private Solo solo;


    public UserLoginActivityTest() {
        super(UserLoginActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
//        solo.finishOpenedActivities();
            try {
                solo.finalize();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            getActivity().finish();
            super.tearDown();

    }

    public void testAboutMenu() throws Exception {
        solo.assertCurrentActivity("wrong activity", UserLoginActivity.class);
        // open the menu
        solo.sendKey(Solo.MENU);
        solo.clickOnText("About");
    }

    public void testLogin() throws Exception
    {
        solo.assertCurrentActivity("wrong activity", UserLoginActivity.class);
        solo.enterText((EditText) solo.getView(R.id.userName),"test");
        solo.enterText((EditText) solo.getView(R.id.password),"test");
        solo.clickOnButton("Login");
    }

//    public void testChangePasswordMenu() throws Exception {
//        solo.assertCurrentActivity("wrong activity", UserHomeActivity.class);
//        // open the menu
//        solo.sendKey(Solo.MENU);
//        solo.clickOnText("Change Password");
//        solo.assertCurrentActivity("wrong activity", ChangePasswordActivity.class);
//    }

}