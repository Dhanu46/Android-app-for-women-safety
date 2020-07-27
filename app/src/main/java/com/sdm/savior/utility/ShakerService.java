package com.sdm.savior.utility;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;

public class ShakerService extends Service implements ShakeDetector.Listener {

    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;


    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public ShakerService getService() {
            // Return this instance of MyService so clients can call public methods
            return ShakerService.this;
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    @Override

    public void onCreate() {
        super.onCreate();
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);
//register your sensor manager listener here
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void hearShake() {
        Context ctx = this; // or you can replace **'this'** with your **ActivityName.this**
        try {
//            Intent i = ctx.getPackageManager().getLaunchIntentForPackage("com.sdm.savior");
//            ctx.startActivity(i);
            if (serviceCallbacks != null) {
                serviceCallbacks.onDeviceShake();
            }

        } catch (Exception e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        }
    }
}