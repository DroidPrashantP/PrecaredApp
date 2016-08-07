package com.app.precared.Apis;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Login;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.VolleyController;

import java.util.HashMap;
import java.util.Map;

/**
 * Login related APIs
 * Created by Prashant on 19/06/16.
 */
public class LoginApi implements Constants.LoginKeys {

    private static final String TAG = LoginApi.class.getSimpleName();

    private Context mContext;
    private LoginListener mLoginListener;
    private SignUpListener mSignUpListener;
    private PrecaredSharePreferences mPreference;

    public LoginApi(Context context, LoginListener loginListener) {
        this.mContext = context;
        mLoginListener = loginListener;
        mPreference = new PrecaredSharePreferences(mContext);
    }

    public LoginApi(Context context, SignUpListener signUpListener) {
        this.mContext = context;
        mSignUpListener = signUpListener;
        mPreference = new PrecaredSharePreferences(mContext);
    }

    /**
     * Email login
     *
     * @param login Login model
     */
    public void executeEmailLogin(final Login login) {

        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL.API_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login: " + response);
                mLoginListener.onLogin(response, Constants.LoginKeys.MODE_EMAIL);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mLoginListener.onError(error);
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(EMAIL, login.email);
                params.put(PASSWORD, login.password);

                return params;
            }
        };

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.EMAIL_LOGIN);
    }

    /**
     * Desi dime email SignUp
     */
    public void doEmailSignUpVolley(final Login login) {
        StringRequest request = new StringRequest(Request.Method.POST, Constants.URL.API_SIGNUP, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "SignUp Response: " + response);
                mSignUpListener.onSignUpSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSignUpListener.onError(error);
            }
        }) { @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(FIRST_NAME, login.name);
                params.put(EMAIL, login.email);
                params.put(PASSWORD, login.password);
                params.put(CONFIRM_PASSWORD, login.password);
                params.put(NUMBER, login.number);
                params.put(GCM_REGISTRATION_TOKEN, mPreference.getRegistrationId());
                return params;
            }
        };
        // Adding request to request queue
        request.setRetryPolicy(new DefaultRetryPolicy(Constants.MY_SOCKET_TIMEOUT_MS, Constants.MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.EMAIL_SIGNUP);
    }

    public interface LoginListener {
        //called method after successfully login
        void onLogin(String response, String loginMode);

        //called method on login error
        void onError(VolleyError error);
    }

    public interface SignUpListener {
        //called method after successfully login
        void onSignUpSuccess(String response);

        //called method on login error
        void onError(VolleyError error);
    }

}
