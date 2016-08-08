package com.app.precared;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.segment.analytics.Analytics;

import java.util.HashMap;

/**
 * Created by prashant on 19/7/16.
 */
public class PrecaredApplication extends Application {
    public static final String TAG = PrecaredApplication.class.getSimpleName();
    private static final String PROPERTY_ID = "UA-29881647-2";
    private static Context mContext;
    private static Activity mActivity;
    private static PrecaredApplication mInstance;
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

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
        Analytics analytics = new Analytics.Builder(mContext, "dnYvfpExNQTYDmUVkOlLRBgR9nj8YPiP").build();
        Analytics.setSingletonInstance(analytics);

        // Safely call Analytics.with(context) from anywhere within your app!
        Analytics.with(mContext).track("Application Started");
    }


    public static synchronized PrecaredApplication getInstance() {
        return mInstance;
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(R.xml.app_tracker)
                    : analytics.newTracker(PROPERTY_ID);// if trackerId == TrackerName.GLOBAL_TRACKER
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }


}

