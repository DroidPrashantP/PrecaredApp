package com.app.precared.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.precared.R;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Image;
import com.app.precared.utils.CustomMultipartRequest;
import com.app.precared.utils.JSONUtil;
import com.app.precared.utils.NetworkManager;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.StringUtils;
import com.app.precared.utils.Utils;
import com.app.precared.utils.VolleyController;
import com.segment.analytics.Analytics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSellerProduct extends AppCompatActivity {

    private static final String TAG = AddSellerProduct.class.getName();
    private EditText mProdName, mProdDesc, mProdDefects, mProdShippingAddress;
    private Button mSubmit;
    private TableRow mAttachmentLayout;
    private RelativeLayout mMainLayout;
    private ArrayList<String> mFilePathList = new ArrayList<String>();
    private PrecaredSharePreferences mPrecaredSharePreferences;
    private NestedScrollView mNestedScrollView;
    private CheckBox mAutoFillCheckBox;
    private EditText mEdtLine1, mEdtLine2, mEdtCity, mEdtState, mEdtPincode, mEdtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seller_product);
        mPrecaredSharePreferences = new PrecaredSharePreferences(this);
        setToolbar();
        mapIDs();
        Utils.hitGoogleAnalytics(this, Constants.GoogleAnalyticKey.ADD_SELLER_PRODUCT);
        Analytics.with(this).screen(null,Constants.GoogleAnalyticKey.ADD_SELLER_PRODUCT);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;

        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }

        mNestedScrollView.setMinimumHeight(screenHeight - actionBarHeight);
    }

    /**
     * map view ids
     */
    private void mapIDs() {
        mNestedScrollView = (NestedScrollView) findViewById(R.id.nestedScrollView);
        mMainLayout = (RelativeLayout) findViewById(R.id.mainWrapper);
        mProdName = (EditText) findViewById(R.id.edtProductName);
        mProdDesc = (EditText) findViewById(R.id.edtProductDesc);
        mProdDefects = (EditText) findViewById(R.id.edtProductDefects);
        mAttachmentLayout = (TableRow) findViewById(R.id.attachmentLayout);
        mSubmit = (Button) findViewById(R.id.btnSubmit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitSellerRequest();
            }
        });
        mAttachmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachFile();
            }
        });
        mAutoFillCheckBox = (CheckBox) findViewById(R.id.checkBox);
        mEdtLine1 = (EditText) findViewById(R.id.edtAddressLine1);
        mEdtLine2 = (EditText) findViewById(R.id.edtAddressLine2);
        mEdtCity = (EditText) findViewById(R.id.edtAddressCity);
        mEdtState = (EditText) findViewById(R.id.edtAddressState);
        mEdtPincode = (EditText) findViewById(R.id.edtAddressPincode);
        mEdtPhone= (EditText) findViewById(R.id.edtAddressMobile);
        mAutoFillCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    if (StringUtils.isNotEmpty(mPrecaredSharePreferences.getAddress())) {
                        JSONObject addressObj = null;
                        try {
                            addressObj = new JSONObject(mPrecaredSharePreferences.getAddress());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (StringUtils.isNotEmpty(addressObj.toString())) {
                            mEdtLine1.setText(JSONUtil.getJSONString(addressObj, "line1"));
                            mEdtLine2.setText(JSONUtil.getJSONString(addressObj, "line2"));
                            mEdtCity.setText(JSONUtil.getJSONString(addressObj, "city"));
                            mEdtState.setText(JSONUtil.getJSONString(addressObj, "state"));
                            mEdtPincode.setText(JSONUtil.getJSONString(addressObj, "pincode"));
                            mEdtPhone.setText(JSONUtil.getJSONString(addressObj, "mobile_no"));
                        }
                    }

                }else {
                    mEdtLine1.setText("");
                    mEdtLine2.setText("");
                    mEdtCity.setText("");
                    mEdtState.setText("");
                    mEdtPhone.setText("");
                    mEdtPincode.setText("");
                }
            }
        });

        if(mAutoFillCheckBox.isChecked()){
            if (StringUtils.isNotEmpty(mPrecaredSharePreferences.getAddress())) {
                JSONObject addressObj = null;
                try {
                    addressObj = new JSONObject(mPrecaredSharePreferences.getAddress());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotEmpty(addressObj.toString())) {
                    mEdtLine1.setText(JSONUtil.getJSONString(addressObj, "line1"));
                    mEdtLine2.setText(JSONUtil.getJSONString(addressObj, "line2"));
                    mEdtCity.setText(JSONUtil.getJSONString(addressObj, "city"));
                    mEdtState.setText(JSONUtil.getJSONString(addressObj, "state"));
                    mEdtPincode.setText(JSONUtil.getJSONString(addressObj, "pincode"));
                    mEdtPhone.setText(JSONUtil.getJSONString(addressObj, "mobile_no"));
                }
            }
        }
    }

    /**
     * called method to attach files
     */
    private void attachFile() {
        Intent intent = new Intent(this, AlbumSelectActivity.class);
        //set limit on number of images that can be selected, default is 10
        intent.putExtra(Constants.AttachmentsKeys.INTENT_EXTRA_LIMIT, 5);
        startActivityForResult(intent, Constants.AttachmentsKeys.REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.AttachmentsKeys.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.AttachmentsKeys.INTENT_EXTRA_IMAGES);
            mFilePathList.clear();
            for (int i = 0; i < images.size(); i++) {
                Log.d(TAG, "Name: " + images.get(i).name);
                Log.d(TAG, "Path: " + images.get(i).path);
                mFilePathList.add(images.get(i).path);
            }
            setSelectedImageLayout(mFilePathList);
        }
    }

    /**
     * set attachment layout
     */
    private void setSelectedImageLayout(List<String> selectedImageList) {
        if (selectedImageList.size() > 0) {
            mAttachmentLayout.setVisibility(View.VISIBLE);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.selectedImageLayout);
            final float scale = getResources().getDisplayMetrics().density;
            int dpWidthInPx = (int) (50 * scale);
            int dpHeightInPx = (int) (50 * scale);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpWidthInPx, dpHeightInPx);
            if (linearLayout.getChildCount() > 0)
                linearLayout.removeAllViews();
            for (int i = 0; i < selectedImageList.size(); i++) {
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(params);
                imageView.setPadding(5, 5, 5, 5);
                Picasso.with(this)
                        .load(new File(selectedImageList.get(i)))
                        .resize(100, 100)
                        .placeholder(R.drawable.precared_logo).into(imageView);
                linearLayout.addView(imageView);
            }
        } else {
            mAttachmentLayout.setVisibility(View.GONE);
        }
    }

    /**
     * submit seller request
     */
    private void submitSellerRequest() {
        if (mProdName.getText().length() == 0) {
            ShowSnackbar(getString(R.string.text_please_enter_product_name));
        } else {
            if (mProdDesc.getText().length() == 0) {
                ShowSnackbar(getString(R.string.text_please_enter_product_desc));
            } else {
                if (NetworkManager.isConnectedToInternet(this)) {
                    submitTicket();
                } else {
                    ShowSnackbar(getString(R.string.no_internet_connection_text));
                }
            }
        }
    }

    private void submitTicket() {
        Utils.showProgress(this,Constants.VolleyRequestTags.SUBMIT_SELLER_REQUEST);
        Map<String, String> params = new HashMap<>();
        params.put(Constants.SellerAddRequestKey.PRODUCT_NAME, "" + mProdName.getText().toString());
        params.put(Constants.SellerAddRequestKey.PRODUCT_DESCRIPTION, "" + mProdDesc.getText().toString());
        params.put(Constants.SellerAddRequestKey.PRODUCT_Efects, mProdDesc.getText().toString());
        params.put(Constants.SellerAddRequestKey.PRODUCT_Category, "");
        params.put(Constants.Preferences.ACCESS_TOKEN, mPrecaredSharePreferences.getAccessToken());

        CustomMultipartRequest request = new CustomMultipartRequest(Request.Method.POST, Constants.URL.API_SELLER_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "" + response);
                Utils.closeProgress();
                ShowSnackbar("Your request submitted successfully.");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", error.toString());
                Utils.closeProgress();

            }
        }, params, Utils.setHeaders(mPrecaredSharePreferences.getAccessToken()), mFilePathList);
        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.SUBMIT_SELLER_REQUEST);
    }

    /**
     * show error method
     */
    public void ShowSnackbar(String msg) {
        Snackbar snackbar = Snackbar
                .make(mMainLayout, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    /**
     * Set Toolbar : Add app name as title and app logo as toolbar logo
     */
    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Add Seller Request");
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
