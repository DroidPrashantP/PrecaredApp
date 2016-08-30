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

    public String getFirstName() {
        return mSharedPreferences.getString(FIRST_NAME, "");
    }

    public void setFirstName(String name) {
        mSharedPreferences.edit().putString(FIRST_NAME, name).apply();
    }
    public String getLastName() {
        return mSharedPreferences.getString(LAST_NAME, "");
    }

    public void setLastName(String name) {
        mSharedPreferences.edit().putString(LAST_NAME, name).apply();
    }

    public String getPhoneNumber() {
        return mSharedPreferences.getString(PHONE, "");
    }

    public void setPhoneNumber(String phoneName) {
        mSharedPreferences.edit().putString(PHONE, phoneName).apply();
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

    public String getReferralCode() {
        return mSharedPreferences.getString(REFERRAL_CODE, "");
    }

    public void setReferralCode(String code) {
        mSharedPreferences.edit().putString(REFERRAL_CODE, code).apply();
    }

    public String getReferralUrl() {
        return mSharedPreferences.getString(REF_URL, "");
    }

    public void setReferralUrl(String url) {
        mSharedPreferences.edit().putString(REF_URL, url).apply();
    }
    public String getReferralMsg() {
        return mSharedPreferences.getString(REFERRAL_MESSAGE, "");
    }

    public void setReferralMsg(String message) {
        mSharedPreferences.edit().putString(REFERRAL_MESSAGE, message).apply();
    }
    public String getAmountEarned() {
        return mSharedPreferences.getString(AMOUNT_EARNED, "");
    }

    public void setAmountEarned(String AmountEarned) {
        mSharedPreferences.edit().putString(AMOUNT_EARNED, AmountEarned).apply();
    }

    public String getAmountPending() {
        return mSharedPreferences.getString(AMOUNT_PENDING, "");
    }

    public void setAmountPending(String AmountPending) {
        mSharedPreferences.edit().putString(AMOUNT_PENDING, AmountPending).apply();
    }
    public String getTotalAmountEarned() {
        return mSharedPreferences.getString(TOTOL_AMOUNT_EARNED, "");
    }

    public void setTotalAmountEarned(String amount) {
        mSharedPreferences.edit().putString(TOTOL_AMOUNT_EARNED, amount).apply();
    }

    public String getAddress() {
        return mSharedPreferences.getString(ADDRESS, "");
    }

    public void setAddress(String address) {
        mSharedPreferences.edit().putString(ADDRESS, address).apply();
    }
}
