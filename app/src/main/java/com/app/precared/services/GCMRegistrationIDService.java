package com.app.precared.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.precared.interfaces.Constants;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.VolleyController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashant on 26/7/16.
 */
public class GCMRegistrationIDService extends IntentService {

    private static final String TAG = "GCMREG";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GCMRegistrationIDService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null) {
            Log.d(TAG, "Service Started!");
            registerDeviceToServer((String) extras.get("deviceToken"), GCMRegistrationIDService.this);
            Log.d(TAG, "Service Stopping!");
        }
        this.stopSelf();
    }

    /**
     * execute Seller request
     */
    public void registerDeviceToServer(final String gcm_reg_id, Context mContext) {

        String URL = Constants.URL.API_DEVICE_GCM_REGISTRAION+"?access_token="+new PrecaredSharePreferences(mContext).getAccessToken();
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
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.Preferences.GCM_REGISTRATION_TOKEN, gcm_reg_id);

                return params;
            }
        };

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.SELLER_REQUEST);
    }
}

