package com.app.precared.gcm;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.precared.activities.HomeActivity;
import com.app.precared.interfaces.Constants;
import com.app.precared.utils.NetworkManager;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.Utils;
import com.app.precared.utils.VolleyController;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Prashant on 1/5/16.
 */
public class GCMRegistrationAsyncTask extends AsyncTask<String, Void, String> {

    private static final String TAG = GCMRegistrationAsyncTask.class.getSimpleName();
    private final Context mContext;
    private String mEmail = "";

    public GCMRegistrationAsyncTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(String... params) {
        String registrationId = null;
        try {
            // server key AIzaSyBcQmnyt4GPT_iiDoYAFcRLvzC4kF6ci7c
            registrationId = InstanceID.getInstance(mContext).getToken(Constants.GCM_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return registrationId;
    }

    @Override
    protected void onPostExecute(String deviceToken) {
        super.onPostExecute(deviceToken);
        Log.d(TAG, "" + deviceToken);
        if (!TextUtils.isEmpty(deviceToken)) {
            if (NetworkManager.isConnectedToInternet(mContext)) {
                new PrecaredSharePreferences(mContext).setRegistrationId(deviceToken);
                registerDeviceToServer(deviceToken, mContext);
            }
        }
    }

    /**
     * execute Seller request
     */
    public void registerDeviceToServer(final String gcm_reg_id, final Context mContext) {

        String URL = Constants.URL.API_DEVICE_REGISTRATION;

        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Reg GCM Token", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Reg GCM Token Error", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                    params.put(Constants.Preferences.DEVICE_NUMBER, Utils.getDeviceId(mContext));
                    params.put(Constants.Preferences.DEVICE_TYPE,"android");
                    params.put(Constants.Preferences.GCM_REGISTRATION_TOKEN,gcm_reg_id);

                return params;
            }
        };

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.DEVICE_REGISTRAION);
    }
}
