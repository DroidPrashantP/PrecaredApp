package com.app.precared.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.precared.Apis.LoginApi;
import com.app.precared.R;
import com.app.precared.activities.HomeActivity;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Login;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.StringUtils;
import com.app.precared.utils.Utils;
import com.app.precared.utils.VolleyController;
import com.segment.analytics.Analytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class LoginFragment extends Fragment implements View.OnClickListener, TextWatcher, LoginApi.LoginListener {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private static Dialog progressDialog;
    private TextInputLayout mEmailInputLayout, mPasswordInputLayout;
    private TextInputEditText mPassword;
    private ImageView mVisibilityView;
    private ImageView mOrImageView;
    private Button mLoginBtn;
    private boolean isVisible;
    private LoginApi mLoginApi;
    private PrecaredSharePreferences precaredSharePreferences;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        precaredSharePreferences = new PrecaredSharePreferences(getActivity());
        mLoginApi = new LoginApi(getActivity(), this);
        Utils.hitGoogleAnalytics(getActivity(), Constants.GoogleAnalyticKey.LOGIN_FRAGMENT);
        Analytics.with(getActivity()).screen(null,Constants.GoogleAnalyticKey.LOGIN_FRAGMENT);
    }

    /**
     * initializing views
     */
    private void initView(View view) {
        // OnClick listeners
        view.findViewById(R.id.email_login_button).setOnClickListener(this);
        view.findViewById(R.id.forgetPasswordTextView).setOnClickListener(this);
        //TextInputLayout
        mEmailInputLayout = (TextInputLayout) view.findViewById(R.id.emailTextInputLayout);
        mPasswordInputLayout = (TextInputLayout) view.findViewById(R.id.passwordTextInputLayout);
        mPassword = (TextInputEditText) view.findViewById(R.id.passwordEditText);
        mVisibilityView = (ImageView) view.findViewById(R.id.visibilityView);
        mLoginBtn = (Button) view.findViewById(R.id.email_login_button);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailLogin();
            }
        });

        mPassword.addTextChangedListener(this);
        // mVisibilityView.setOnTouchListener(mPasswordVisibleTouchListener);
        mVisibilityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisible) {
                    isVisible = false;
                    mVisibilityView.setImageResource(R.drawable.ic_visibility);
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    isVisible = true;
                    mVisibilityView.setImageResource(R.drawable.ic_visibility_off);
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });

    }

    /**
     * Email Login
     */
    private void emailLogin() {
        if (validateData()) {
            Utils.showProgress(getActivity(), Constants.VolleyRequestTags.EMAIL_LOGIN);
            mLoginApi.executeEmailLogin(new Login(mEmailInputLayout.getEditText().getText().toString(),
                    mPasswordInputLayout.getEditText().getText().toString()));
        }
    }

    /**
     * validating input fields
     */
    private boolean validateData() {
        mEmailInputLayout.setError(null);
        mEmailInputLayout.setErrorEnabled(false);
        mPasswordInputLayout.setError(null);
        mPasswordInputLayout.setErrorEnabled(false);
        if (!StringUtils.isNotEmpty(mEmailInputLayout.getEditText().getText().toString())) {
            mEmailInputLayout.requestFocus();
            mEmailInputLayout.setErrorEnabled(true);
            mEmailInputLayout.setError(getActivity().getString(R.string.email_empty_error));
            return false;
        } else if (!Utils.isValidEmail(mEmailInputLayout.getEditText().getText().toString())) {
            mEmailInputLayout.requestFocus();
            mEmailInputLayout.setErrorEnabled(true);
            mEmailInputLayout.setError(getActivity().getString(R.string.invalid_email_error));
            return false;
        } else if (!StringUtils.isNotEmpty(mPasswordInputLayout.getEditText().getText().toString())) {
            mPasswordInputLayout.requestFocus();
            mPasswordInputLayout.setErrorEnabled(true);
            mPasswordInputLayout.setError(getActivity().getString(R.string.password_empty_error));
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mVisibilityView.setVisibility(editable.length() > 0 ? View.VISIBLE : View.GONE);
        if (editable.length() == 0) {
            isVisible = false;
            mVisibilityView.setImageResource(R.drawable.ic_visibility);
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    @Override
    public void onLogin(String response, String loginMode) {
        Utils.closeProgress();
        String success, refreshToken;
        try {
            JSONObject jsonObject = new JSONObject(response);

            success = jsonObject.getString("response");
            if (StringUtils.isNotEmpty(success)) {
                if ("Success".equalsIgnoreCase(success)) {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    precaredSharePreferences.setLoggedIn(true);
                    precaredSharePreferences.setUserId("" + dataObject.getString(Constants.LoginKeys.UID));
                    precaredSharePreferences.setEmail(dataObject.getString(Constants.LoginKeys.EMAIL));
                    precaredSharePreferences.setName(dataObject.getString(Constants.LoginKeys.NAME));
                    precaredSharePreferences.setAccessToken(dataObject.getString(Constants.LoginKeys.ACCESS_TOKEN));

//                    if (precaredSharePreferences.getAccessToken()!= null) {
//                        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), GCMRegistrationIDService.class);
//                        intent.putExtra("deviceToken", precaredSharePreferences.getAccessToken());
//                        getActivity().startService(intent);
//                    }
//                    getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
//                    getActivity().finish();
                     registerDeviceToServer(precaredSharePreferences.getRegistrationId(), getActivity());

                } else {
                    String msg = jsonObject.getString("message");
                    Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError volleyError) {
        Utils.closeProgress();
        if (volleyError != null) {
            Log.e(TAG, volleyError.toString());
            if (volleyError instanceof NoConnectionError) {
                Toast.makeText(getActivity(), "Please connect to internet!", Toast.LENGTH_SHORT).show();
            }
//            else if (volleyError.networkResponse.statusCode == 201) {
//                try {
//                    String response = new String(volleyError.networkResponse.data);
//                    JSONObject jsonResponse = new JSONObject(response);
//                    Log.d(TAG, "" + jsonResponse);
//                    Toast.makeText(getActivity(), "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
            else if (volleyError instanceof ServerError) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            } else if (volleyError instanceof AuthFailureError) {
                Toast.makeText(getActivity(), "Please Check email and password!", Toast.LENGTH_SHORT).show();
            } else if (volleyError instanceof TimeoutError) {
                Toast.makeText(getActivity(), "Request timeout!", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * execute Seller request
     */
    public void registerDeviceToServer(final String gcm_reg_id, final Context mContext) {

        String URL = Constants.URL.API_DEVICE_GCM_REGISTRAION;


        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Reg GCM Token", response);
                getActivity().startActivity(new Intent(mContext, HomeActivity.class));
                getActivity().finish();
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
                Log.e("Access token", precaredSharePreferences.getAccessToken());
                Log.e("gcm_reg_id", gcm_reg_id);
                params.put(Constants.Preferences.ACCESS_TOKEN, precaredSharePreferences.getAccessToken());
                params.put(Constants.Preferences.GCM_REGISTRATION_TOKEN,gcm_reg_id);

                return params;
            }
        };
        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.GCM_REGISTRAION_AFTER_LOGIN);
    }
}
