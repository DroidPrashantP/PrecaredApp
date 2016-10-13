package com.app.precared.Apis;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.precared.interfaces.Constants;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.StringUtils;
import com.app.precared.utils.VolleyController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prashant on 22/7/16.
 */
public class ChatApi {

    private static final String TAG = ChatApi.class.getSimpleName();

    private Context mContext;
    private ChatListener mChatListener;
    private PrecaredSharePreferences mPreference;

    public ChatApi(Context context, ChatListener ChatListener) {
        this.mContext = context;
        mChatListener = ChatListener;
        mPreference = new PrecaredSharePreferences(mContext);
    }

    /**
     * execute Seller request
     */
    public void executeSellerRequest(String access_token, String sellerRequestID) {
        String URL ="";
        if (StringUtils.isNotEmpty(sellerRequestID)) {
            URL = Constants.URL.API_FETCH_CHAT_DATA + "?access_token=" + access_token + "&seller_request_id" + sellerRequestID;
        }else {
            URL = Constants.URL.API_FETCH_CHAT_DATA + "?access_token=" + access_token;
        }
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Seller: " + response);
                mChatListener.onChatResponse(response, Constants.MyChatKeys.API_CHAT_LIST);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mChatListener.onError(error, Constants.MyChatKeys.API_CHAT_LIST);
            }
        });
        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.SELLER_REQUEST);
    }

    /**
     * execute Seller request
     */
    public void sendMessageRequest(final String access_token, final String message) {
        String URL = Constants.URL.API_SEND_MESSAGE;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "On message Success: " + response);
                mChatListener.onChatResponse(response, Constants.MyChatKeys.API_SEND_MSG);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mChatListener.onError(error, Constants.MyChatKeys.API_SEND_MSG);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.LoginKeys.ACCESS_TOKEN, access_token);
                params.put(Constants.MyChatKeys.MESSAGES, message);

                return params;
            }
        };

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.SELLER_REQUEST);
    }


    public interface ChatListener {
        //called method after successfully Seller
        void onChatResponse(String response, String ApiType);

        //called method on Seller error
        void onError(VolleyError error, String ApiType);
    }



}
