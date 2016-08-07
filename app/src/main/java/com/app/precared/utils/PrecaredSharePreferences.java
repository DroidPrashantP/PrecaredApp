package com.app.precared.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.precared.interfaces.Constants;

/**
 * Created by prashant on 19/7/16.
 */
public class PrecaredSharePreferences implements Constants.Preferences {

    private static SharedPreferences mSharedPreferences;
    private boolean soundAlert;

    public PrecaredSharePreferences(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
    }

    /**
     * clear all preference
     */
    public void clearPreferences() {
        if (mSharedPreferences != null) {
            mSharedPreferences.edit().clear().apply();
        }
    }

    public String getAccessToken() {
        return mSharedPreferences.getString(ACCESS_TOKEN, "");
    }

    public void setAccessToken(String accessToken) {
        mSharedPreferences.edit().putString(ACCESS_TOKEN, accessToken).apply();
    }

    public String getUserId() {
        return mSharedPreferences.getString(USER_ID, "");
    }

    public void setUserId(String userId) {
        mSharedPreferences.edit().putString(USER_ID, userId).apply();
    }

    public String getName() {
        return mSharedPreferences.getString(NAME, "");
    }

    public void setName(String name) {
        mSharedPreferences.edit().putString(NAME, name).apply();
    }

    public String getEmail() {
        return mSharedPreferences.getString(EMAIL, "");
    }

    public void setEmail(String email) {
        mSharedPreferences.edit().putString(EMAIL, email).apply();
    }

    public boolean isLoggedIn() {
        return mSharedPreferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        mSharedPreferences.edit().putBoolean(IS_LOGGED_IN, loggedIn).apply();
    }


    /**
     * * get registration device id
     * * @return
     */
    public String getRegistrationId() {
        return mSharedPreferences.getString(GCM_REGISTRATION_TOKEN, "");
    }

    /**
     * Set GCM registration id
     *
     * @param regId
     */
    public void setRegistrationId(String regId) {
        mSharedPreferences.edit().putString(GCM_REGISTRATION_TOKEN, regId).apply();

    }
}
