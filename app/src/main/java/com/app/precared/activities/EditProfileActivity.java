package com.app.precared.activities;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.app.precared.Apis.LoginApi;
import com.app.precared.R;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Login;
import com.app.precared.utils.JSONUtil;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.StringUtils;
import com.app.precared.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity implements LoginApi.UpdateListener {

    private static final String TAG = EditProfileActivity.class.getName();
    private Toolbar mToolbar;
    private Button mUpdateBtn;
    private TextInputLayout mFirstNameTextInputLayout, mLastNameTextInputLayout, mPhoneTextInputLayout;
    private RelativeLayout mainLayoutWrapper;
    private LoginApi mLoginApi;
    private PrecaredSharePreferences mPrecaredSharePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setToolbar();
        findViewByIds();
        mLoginApi = new LoginApi(this, this);
        mPrecaredSharePreferences = new PrecaredSharePreferences(this);
        UpdateFiledFromPreference();
    }

    /**
     * update field data from preferences
     */
    private void UpdateFiledFromPreference() {
        mFirstNameTextInputLayout.getEditText().setText(mPrecaredSharePreferences.getName());
        mLastNameTextInputLayout.getEditText().setText(mPrecaredSharePreferences.getLastName());
        mPhoneTextInputLayout.getEditText().setText(mPrecaredSharePreferences.getPhoneNumber());

    }

    /**
     * find view by ids
     */
    private void findViewByIds() {
        mainLayoutWrapper = (RelativeLayout) findViewById(R.id.mainLayoutWrapper);
        mFirstNameTextInputLayout = (TextInputLayout) findViewById(R.id.firstNameTextInputLayout);
        mLastNameTextInputLayout = (TextInputLayout) findViewById(R.id.lastNameTextInputLayout);
        mPhoneTextInputLayout = (TextInputLayout) findViewById(R.id.mobileTextInputLayout);
        mUpdateBtn = (Button) findViewById(R.id.updateButton);
        mUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    /**
     * sign up
     */
    private void update() {
        if (validateData()) {
            String name = mFirstNameTextInputLayout.getEditText().getText().toString();
            String lastName = mLastNameTextInputLayout.getEditText().getText().toString();
            String phone = mPhoneTextInputLayout.getEditText().getText().toString();
            Utils.showProgress(EditProfileActivity.this, Constants.VolleyRequestTags.UPDATE_PROFILE);
            mLoginApi.updateUserProfileReuest(new Login(name,lastName,phone));
        }
    }
    /**
     * validating input fields
     */
    private boolean validateData() {
        Utils.setErrorDisabled(mFirstNameTextInputLayout);
        Utils.setErrorDisabled(mLastNameTextInputLayout);
        Utils.setErrorDisabled(mPhoneTextInputLayout);

        if (!StringUtils.isNotEmpty(mFirstNameTextInputLayout.getEditText().getText().toString())) {
            Utils.setInputLayoutError(mFirstNameTextInputLayout, getString(R.string.name_empty_error));
            return false;
        } else if (!StringUtils.isNotEmpty(mPhoneTextInputLayout.getEditText().getText().toString())) {
            Utils.setInputLayoutError(mPhoneTextInputLayout, getString(R.string.phone_numer_error));
            return false;
        }

        return true;
    }
    /**
     * Set Toolbar : Add app name as title and app logo as toolbar logo
     */
    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Edit Profile");
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateSuccess(String response) {
        Utils.closeProgress();
        Log.e("onUpdateSuccess", response);
        String success, refreshToken;
        try {
            JSONObject jsonObject = new JSONObject(response);
            success = jsonObject.getString("response");
            if (StringUtils.isNotEmpty(success)) {
                if ("Success".equalsIgnoreCase(success)) {
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    mPrecaredSharePreferences.setUserId("" + dataObject.getString(Constants.LoginKeys.UID));
                    mPrecaredSharePreferences.setEmail(JSONUtil.getJSONString(dataObject, Constants.LoginKeys.EMAIL));
                    mPrecaredSharePreferences.setName(JSONUtil.getJSONString(dataObject,Constants.LoginKeys.NAME));
                    mPrecaredSharePreferences.setFirstName(JSONUtil.getJSONString(dataObject,Constants.LoginKeys.FIRST_NAME));
                    mPrecaredSharePreferences.setLastName(JSONUtil.getJSONString(dataObject,Constants.LoginKeys.LAST_NAME));

                    mPrecaredSharePreferences.setAccessToken(JSONUtil.getJSONString(dataObject,Constants.LoginKeys.ACCESS_TOKEN));
                    mPrecaredSharePreferences.setReferralCode(JSONUtil.getJSONString(dataObject,Constants.LoginKeys.REFERRAL_CODE));
                    mPrecaredSharePreferences.setReferralUrl(JSONUtil.getJSONString(dataObject,Constants.LoginKeys.REF_URL));
                    mPrecaredSharePreferences.setReferralMsg(JSONUtil.getJSONString(dataObject,Constants.LoginKeys.REFERRAL_MESSAGE));
                    mPrecaredSharePreferences.setAmountEarned(JSONUtil.getJSONString(dataObject,Constants.LoginKeys.AMOUNT_EARNED));
                    mPrecaredSharePreferences.setAmountPending(JSONUtil.getJSONString(dataObject,Constants.LoginKeys.AMOUNT_PENDING));
                    mPrecaredSharePreferences.setTotalAmountEarned(JSONUtil.getJSONString(dataObject,Constants.LoginKeys.TOTOL_AMOUNT_EARNED));

                    Toast.makeText(this, "Profile update successfully." , Toast.LENGTH_SHORT).show();

                } else {
                    String msg = jsonObject.getString("message");
                    Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
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

                Toast.makeText(this, "Please connect to internet!", Toast.LENGTH_SHORT).show();
            }else if (volleyError.networkResponse.statusCode == 401) {
                try {
                    String response = new String(volleyError.networkResponse.data);
                    JSONObject jsonResponse = new JSONObject(response);
                    Toast.makeText(this, "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (volleyError instanceof ServerError) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
            } else if (volleyError instanceof TimeoutError) {
                Toast.makeText(this, "Request timeout!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
