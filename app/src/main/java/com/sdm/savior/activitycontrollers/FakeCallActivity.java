package com.sdm.savior.activitycontrollers;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;

import com.sdm.savior.R;

public class FakeCallActivity extends Activity {

    private MediaPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fakecall);

        player = MediaPlayer.create(this,
                Settings.System.DEFAULT_RINGTONE_URI);
        //setting loop play to true
        //this will make the ringtone continuously playing
        player.setLooping(true);

        //staring the player
        player.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
    }
}