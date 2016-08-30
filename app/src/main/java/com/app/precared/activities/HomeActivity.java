package com.app.precared.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.precared.R;
import com.app.precared.interfaces.Constants;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.StringUtils;
import com.app.precared.utils.Utils;
import com.segment.analytics.Analytics;

public class HomeActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Menu mMenu;
    private RelativeLayout mHeaderView;
    private Button mChatWithAdmin, mAddProduct, mProductList,mAddSellerRequest, mReferFriend;
    private PrecaredSharePreferences mPrecaredSharePreferences;
    private TextView mUserNameTextView;
    private TextView mUserEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPrecaredSharePreferences = new PrecaredSharePreferences(this);
        setToolbar();
        findIDs();
        initializeNavigationDrawer();
        setUpHeaderView();
        Utils.hitGoogleAnalytics(this, Constants.GoogleAnalyticKey.HOME_ACTIVITY);
        Analytics.with(this).screen(null,Constants.GoogleAnalyticKey.HOME_ACTIVITY);
    }

    private void findIDs() {
        mChatWithAdmin = (Button) findViewById(R.id.chatWithAdmin);
        mAddProduct = (Button) findViewById(R.id.addProductBtn);
        mProductList = (Button) findViewById(R.id.productListBtn);
        mAddSellerRequest = (Button) findViewById(R.id.addSellerRequest);
        mReferFriend = (Button) findViewById(R.id.referFriendBtn);

        mChatWithAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ChatActivity.class));
            }
        });

        mAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, SellerActivity.class));
            }
        });
        mProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ProductListing.class));
            }
        });
        mAddSellerRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, AddSellerProduct.class));
            }
        });

        mReferFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent sendIntent = new Intent();
                sendIntent.setType("text/plain");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Refer a Friend");
                sendIntent.putExtra(Intent.EXTRA_TEXT, mPrecaredSharePreferences.getReferralMsg()+"\n"+mPrecaredSharePreferences.getReferralUrl());
                startActivity(Intent.createChooser(sendIntent, "Refer"));
            }
        });

//        rippleEffect(mChatWithAdmin);
//        rippleEffect(mAddProduct);
//        rippleEffect(mProductList);
//        rippleEffect(mAddSellerRequest);
//        rippleEffect(mReferFriend);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Set Toolbar : Add app name as title and app logo as toolbar logo
     */
    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("");
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * initialize navigation drawer and set up navigation content data
     */
    private void initializeNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);

        if (mNavigationView != null) {
            setUpDrawerContent(mNavigationView);
        }

    }

    /**
     * set up navigation content data
     */
    private void setUpDrawerContent(NavigationView mNavigationView) {
        mNavigationView.setVerticalScrollBarEnabled(false);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_edit_account:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(HomeActivity.this, EditProfileActivity.class));
                        break;
                    case R.id.item_my_account:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(HomeActivity.this, SellerActivity.class));
                        break;
                    case R.id.item_my_address:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(HomeActivity.this, MyAddressActivity.class));
                        break;
                    case R.id.item_notification:
                        mDrawerLayout.closeDrawers();
                        startActivity(new Intent(HomeActivity.this, SellerNotificationActivity.class));
                        break;
                    case R.id.item_logout:
                        mDrawerLayout.closeDrawers();
                        showLogoutDialog();
                        break;
                    case R.id.item_refer_and_earn:
                        mDrawerLayout.closeDrawers();
                        final Intent sendIntent = new Intent();
                        sendIntent.setType("text/plain");
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Refer a Friend");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, mPrecaredSharePreferences.getReferralMsg()+"\n"+mPrecaredSharePreferences.getReferralUrl()+"\n"+"User this code "+mPrecaredSharePreferences.getReferralCode());
                        startActivity(Intent.createChooser(sendIntent, "Refer"));
                        break;
                }
                return true;
            }
        });
    }

    /**
     * set User Layout
     */
    private void setUpHeaderView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        mHeaderView = (RelativeLayout) inflater.inflate(R.layout.layout_drawer_header, null, false);
        // get widgets
        mUserNameTextView = (TextView) mHeaderView.findViewById(R.id.userNameTextView);
        mUserEmailTextView = (TextView) mHeaderView.findViewById(R.id.userEmailTextView);

        mNavigationView.addHeaderView(mHeaderView);
        setUpHeaderViewData();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(toggle);
    }

    /**
     * set header view data
     */
    private void setUpHeaderViewData() {
        if (mPrecaredSharePreferences.isLoggedIn()) {
            String userName = mPrecaredSharePreferences.getName();
            String email = mPrecaredSharePreferences.getEmail();
            mUserNameTextView.setText(StringUtils.isNotEmpty(userName) ? userName : getString(R.string.sign_in_text));
            mUserEmailTextView.setText(StringUtils.isNotEmpty(email) ? email : "");
        }
    }

    /**
     * show logout dialog
     */
    private void showLogoutDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle(getString(R.string.logout_title)).setMessage(getString(R.string.logout_msg)).setPositiveButton(getString(R.string.yes_text), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPrecaredSharePreferences.clearPreferences();
                finish();

            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.secondary_text));
    }

    public void onClickKnowMore(View view) {

    }

    public void rippleEffect(View view){
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = this.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        view.setBackgroundResource(backgroundResource);
    }
}
