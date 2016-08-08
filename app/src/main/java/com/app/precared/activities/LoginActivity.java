package com.app.precared.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.precared.R;
import com.app.precared.fragments.LoginFragment;
import com.app.precared.fragments.SignUpFragment;
import com.app.precared.gcm.GCMRegistrationAsyncTask;
import com.app.precared.interfaces.Constants;
import com.app.precared.services.GCMRegistrationIDService;
import com.app.precared.utils.NetworkManager;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.Utils;
import com.app.precared.utils.VolleyController;
import com.segment.analytics.Analytics;
import com.segment.analytics.Properties;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int[] TITLES = {
            R.string.login_title,
            R.string.sign_up_title
    };
    private static final int RC_SIGN_IN = 9001;
    private static final int REQUEST_GET_ACCOUNTS = 0;
    private List<Fragment> mFragments = new ArrayList<Fragment>() {{
        add(new LoginFragment());
        add(new SignUpFragment());
    }};
    private RelativeLayout mMainLayout;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private PrecaredSharePreferences mPrecaredSharePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPrecaredSharePreferences = new PrecaredSharePreferences(this);
        setupViewPager();
        registerDevice();
        Utils.hitGoogleAnalytics(this, Constants.GoogleAnalyticKey.LOGIN_ACTIVITY);
        Analytics.with(this).screen(null,Constants.GoogleAnalyticKey.LOGIN_ACTIVITY);

    }

    /**
     * set up viewpager
     */
    private void setupViewPager() {
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        /* fetching tab title from string resource */
        List<String> titlesList = new ArrayList<>();
        for (int titleResId : TITLES) {
            titlesList.add(getString(titleResId));
        }
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragments, titlesList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(mViewPager);

    }

    /**
     * called method to register device on server
     */
    private void registerDevice() {
        if (TextUtils.isEmpty(mPrecaredSharePreferences.getRegistrationId()) && NetworkManager.isConnectedToInternet(this) && Utils.checkPlayServices(this)) {
            new GCMRegistrationAsyncTask(this).execute();
        } else
            Log.d(TAG, mPrecaredSharePreferences.getRegistrationId());

    }


}

