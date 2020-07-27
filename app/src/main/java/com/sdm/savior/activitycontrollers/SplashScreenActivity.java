package com.sdm.savior.activitycontrollers;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import com.sdm.savior.utility.Constants;
import com.sdm.savior.R;


public class SplashScreenActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);

                Intent i = null;
                if (mypref.contains(Constants.FIRST_LAUNCH))
                {
                    i = new Intent(SplashScreenActivity.this, UserLoginActivity.class);
                }
                else
                {
                    i = new Intent(SplashScreenActivity.this, UserRegistrationActivity.class);
                }

                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
