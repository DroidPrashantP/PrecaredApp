package com.app.precared;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by prashant on 19/7/16.
 */
public class PrecaredApplication extends Application {
    public static final String TAG = PrecaredApplication.class.getSimpleName();
    private static final String PROPERTY_ID = "UA-29881647-2";
    private static Context mContext;
    private static Activity mActivity;
    private static PrecaredApplication mInstance;

    public PrecaredApplication() {
        super();
    }

    public static Activity getRunningActivity() {
        return mActivity;
    }

    public static Context getAppContext() {
        return PrecaredApplication.mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PrecaredApplication.mContext = getApplicationContext();
    }


    public static synchronized PrecaredApplication getInstance() {
        return mInstance;
    }
}

