package com.app.precared.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import android.widget.RelativeLayout;
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
import com.app.precared.activities.LoginActivity;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Login;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.StringUtils;
import com.app.precared.utils.Utils;
import com.app.precared.utils.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment implements LoginApi.SignUpListener, TextWatcher {

    private static final String TAG = SignUpFragment.class.getSimpleName();
    private Button mSignupBtn;
    private TextInputLayout mNameTextInputLayout, mEmailTextInputLayout, mPasswordTextInputLayout, mConfirmPasswordTextInputLayout, mNumberTextInputLayout;
    private LoginApi mLoginApi;
    private TextInputEditText mPassword, mConfirmPassword;
    private boolean isVisible = false;
    private ImageView mVisibilityView;
    private PrecaredSharePreferences mPrecaredSharePreferences;
    private RelativeLayout mainLayoutWrapper;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        mLoginApi = new LoginApi(getActivity(), this);
        mPrecaredSharePreferences = new PrecaredSharePreferences(getActivity());
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

    private void initView(View view) {
        mainLayoutWrapper = (RelativeLayout) view.findViewById(R.id.mainLayoutWrapper);
        mNameTextInputLayout = (TextInputLayout) view.findViewById(R.id.nameTextInputLayout);
        mEmailTextInputLayout = (TextInputLayout) view.findViewById(R.id.emailTextInputLayout);
        mPasswordTextInputLayout = (TextInputLayout) view.findViewById(R.id.passwordTextInputLayout);
        mConfirmPasswordTextInputLayout = (TextInputLayout) view.findViewById(R.id.confirmpasswordTextInputLayout);
        mNumberTextInputLayout = (TextInputLayout) view.findViewById(R.id.numberTextInputLayout);
        mPassword = (TextInputEditText) view.findViewById(R.id.passwordEditText);
        mConfirmPassword = (TextInputEditText) view.findViewById(R.id.confirmpasswordEditText);
        mVisibilityView = (ImageView) view.findViewById(R.id.visibilityView);
        mSignupBtn = (Button) view.findViewById(R.id.signUpButton);
        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signUp();
            }
        });
    }

    /**
     * sign up
     */
    private void signUp() {
        if (validateData()) {
            String name = mNameTextInputLayout.getEditText().getText().toString();
            String email = mEmailTextInputLayout.getEditText().getText().toString();
            String password = mPasswordTextInputLayout.getEditText().getText().toString();
            String number = mNumberTextInputLayout.getEditText().getText().toString();
            Utils.showProgress(getActivity(), Constants.VolleyRequestTags.EMAIL_SIGNUP);
            mLoginApi.doEmailSignUpVolley(new Login(name, email, password, number));
        }
    }

    /**
     * validating input fields
     */
    private boolean validateData() {
        Utils.setErrorDisabled(mNameTextInputLayout);
        Utils.setErrorDisabled(mEmailTextInputLayout);
        Utils.setErrorDisabled(mPasswordTextInputLayout);
        Utils.setErrorDisabled(mConfirmPasswordTextInputLayout);
        Utils.setErrorDisabled(mNumberTextInputLayout);
        if (!StringUtils.isNotEmpty(mNameTextInputLayout.getEditText().getText().toString())) {
            Utils.setInputLayoutError(mNameTextInputLayout, getString(R.string.name_empty_error));
            return false;
        } else if (!StringUtils.isNotEmpty(mEmailTextInputLayout.getEditText().getText().toString())) {
            Utils.setInputLayoutError(mEmailTextInputLayout, getString(R.string.email_empty_error));
            return false;
        } else if (!Utils.isValidEmail(mEmailTextInputLayout.getEditText().getText().toString())) {
            Utils.setInputLayoutError(mEmailTextInputLayout, getString(R.string.invalid_email_error));
            return false;
        } else if (!StringUtils.isNotEmpty(mPasswordTextInputLayout.getEditText().getText().toString())) {
            Utils.setInputLayoutError(mPasswordTextInputLayout, getString(R.string.password_empty_error));
            return false;
        }else if (!StringUtils.isNotEmpty(mConfirmPasswordTextInputLayout.getEditText().getText().toString())) {
            Utils.setInputLayoutError(mConfirmPasswordTextInputLayout, getString(R.string.password_empty_error));
            return false;
        }else if (!((mConfirmPasswordTextInputLayout.getEditText().getText().toString()).equalsIgnoreCase(mPasswordTextInputLayout.getEditText().getText().toString()))) {
            Utils.setInputLayoutError(mConfirmPasswordTextInputLayout, getString(R.string.confirm_password_empty_error));
            return false;
        }
        else if (!StringUtils.isNotEmpty(mNumberTextInputLayout.getEditText().getText().toString())) {
            Utils.setInputLayoutError(mNumberTextInputLayout, getString(R.string.phone_numer_error));
            return false;
        }

        return true;
    }

    @Override
    public void onSignUpSuccess(String response) {
        Utils.closeProgress();
        String success, refreshToken;
        try {
            JSONObject jsonObject = new JSONObject(response);
            success = jsonObject.getString("response");
            if (StringUtils.isNotEmpty(success)) {
                String msg = jsonObject.getString("message");
                if ("Success".equalsIgnoreCase(success)) {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    mPrecaredSharePreferences.setLoggedIn(true);
                    mPrecaredSharePreferences.setUserId("" + dataObject.getString(Constants.LoginKeys.UID));
                    mPrecaredSharePreferences.setEmail(dataObject.getString(Constants.LoginKeys.EMAIL));
                    mPrecaredSharePreferences.setName(dataObject.getString(Constants.LoginKeys.NAME));
                    mPrecaredSharePreferences.setAccessToken(dataObject.getString(Constants.LoginKeys.ACCESS_TOKEN));

                    Snackbar.make(mainLayoutWrapper," "+msg,Snackbar.LENGTH_SHORT).show();

                    mNameTextInputLayout.getEditText().setText("");
                    mEmailTextInputLayout.getEditText().setText("");
                    mPasswordTextInputLayout.getEditText().setText("");
                    mConfirmPasswordTextInputLayout.getEditText().setText("");
                    mNumberTextInputLayout.getEditText().setText("");

                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            registerDeviceToServer(mPrecaredSharePreferences.getRegistrationId(), getActivity());
                        }
                    },1000);

                } else {

                    Snackbar.make(mainLayoutWrapper," "+msg,Snackbar.LENGTH_SHORT).show();
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
            }else if (volleyError.networkResponse.statusCode == 401) {
                try {
                    String response = new String(volleyError.networkResponse.data);
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d(TAG, "" + jsonResponse);
                    Toast.makeText(getActivity(), "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (volleyError instanceof ServerError) {
                Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
            } else if (volleyError instanceof AuthFailureError) {
                Toast.makeText(getActivity(), "Please Check email and password!", Toast.LENGTH_SHORT).show();
            } else if (volleyError instanceof TimeoutError) {
                Toast.makeText(getActivity(), "Request timeout!", Toast.LENGTH_SHORT).show();
            }
        }
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

    /**
     * execute Seller request
     */
    public void registerDeviceToServer(final String gcm_reg_id, final Context mContext) {

        Utils.showProgress(getActivity(), Constants.VolleyRequestTags.GCM_REGISTRAION_AFTER_LOGIN);
        String URL = Constants.URL.API_DEVICE_GCM_REGISTRAION;
        StringRequest request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.closeProgress();
                Log.e("Reg GCM Token", response);
                getActivity().startActivity(new Intent(mContext, HomeActivity.class));
                getActivity().finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Utils.closeProgress();
                Log.e("Reg GCM Token Error", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.e("Access token", mPrecaredSharePreferences.getAccessToken());
                Log.e("gcm_reg_id", gcm_reg_id);
                params.put(Constants.Preferences.ACCESS_TOKEN, mPrecaredSharePreferences.getAccessToken());
                params.put(Constants.Preferences.GCM_REGISTRATION_TOKEN,""+gcm_reg_id);

                return params;
            }
        };
        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.GCM_REGISTRAION_AFTER_LOGIN);
    }
}
