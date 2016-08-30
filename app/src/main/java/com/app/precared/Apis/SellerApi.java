package com.app.precared.Apis;

import android.app.Activity;
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
    private AddressesListener mAddressesListener;
    private AddAddressesListener mAddAddressesListener;
    private PrecaredSharePreferences mPreference;

    public SellerApi(Context context, Activity activity) {
        this.mContext = context;
        mSellerListener = (SellerListener) activity;
        mAddressesListener = (AddressesListener) activity;
        mAddAddressesListener = (AddAddressesListener) activity;
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

    public void getAddresses(String accessToken) {
        String URL = Constants.URL.API_GET_ADDRESSES+"?access_token="+accessToken;
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Seller: " + response);
                mAddressesListener.onAddresses(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAddressesListener.onError(error);
            }
        });

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.SELLER_COUNTS_REQUEST);
    }

    public void addAddressesRequest(final String line1, final String line2, final String city, final String state, final String pincode, final String phone) {
        String URL = Constants.URL.API_GET_ADDRESSES;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Seller: " + response);
                mAddAddressesListener.onAddAddresses(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAddAddressesListener.onError(error);
            }
        }){ @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("line1", line1);
            params.put("line2", line2);
            params.put("city", city);
            params.put("state", state);
            params.put("pincode", pincode);
            params.put("mobile_no", phone);
            params.put("default",""+ false);
            params.put(Constants.LoginKeys.ACCESS_TOKEN, mPreference.getAccessToken());
            return params;
        }
        };

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.ADD_ADDRESS);
    }


    public interface SellerListener {
        //called method after successfully Seller
        void onSeller(String response, String apiType);

        //called method on Seller error
        void onError(VolleyError error, String apiType);
    }

    public interface AddressesListener {
        void onAddresses(String response);

        void onError(VolleyError error);
    }

    public interface AddAddressesListener {
        void onAddAddresses(String response);

        void onError(VolleyError error);
    }
}
