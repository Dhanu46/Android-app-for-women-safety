package com.sdm.savior.utility;

import android.app.Application;
import android.content.Context;

import com.sdm.savior.model.User;

public class AppInstance extends Application
{
    public static AppInstance instance = null;
    private User currentUser;

    private static Context mContext;

    public static Context getInstance()
    {
        if (null == instance) {
            instance = new AppInstance();
        }
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

    }

    public static Context getContext() {
        return mContext;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public void setCurrentUser(User currentUser)
    {
        this.currentUser = currentUser;
    }

}





