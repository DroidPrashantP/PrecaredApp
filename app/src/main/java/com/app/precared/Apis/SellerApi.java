package com.app.precared.Apis;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Seller;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.VolleyController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashant on 22/7/16.
 */
public class SellerApi {

    private static final String TAG = SellerApi.class.getSimpleName();

    private Context mContext;
    private SellerListener mSellerListener;
    private PrecaredSharePreferences mPreference;

    public SellerApi(Context context, SellerListener SellerListener) {
        this.mContext = context;
        mSellerListener = SellerListener;
        mPreference = new PrecaredSharePreferences(mContext);
    }

    /**
     * execute Seller request
     */
    public void executeSellerRequest(String accessToken, String sellerType) {

        String URL = Constants.URL.API_Seller+sellerType+".json?access_token="+accessToken;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Seller: " + response);
                mSellerListener.onSeller(response, Constants.SellerKeys.API_SELLER_LISTING);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSellerListener.onError(error, Constants.SellerKeys.API_SELLER_LISTING);
            }
        });

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.SELLER_REQUEST);
    }

    public void executeSellerCountsRequest(String accessToken) {
        String URL = Constants.URL.API_SELLER_COUNTS+"?access_token="+accessToken;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Seller: " + response);
                mSellerListener.onSeller(response, Constants.SellerKeys.API_SELLER_COUNTS);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSellerListener.onError(error, Constants.SellerKeys.API_SELLER_COUNTS);
            }
        });

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.SELLER_COUNTS_REQUEST);
    }


    public interface SellerListener {
        //called method after successfully Seller
        void onSeller(String response, String apiType);

        //called method on Seller error
        void onError(VolleyError error, String apiType);
    }

}
