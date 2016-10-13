package com.app.precared.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.app.precared.Apis.SellerApi;
import com.app.precared.R;
import com.app.precared.adapters.AddressListAdapter;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Address;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

public class AddSellerProduct extends AppCompatActivity implements SellerApi.AddressesListener, SellerApi.AddAddressesListener, SellerApi.SellerListener {

    private static final String TAG = AddSellerProduct.class.getName();
    private EditText mProdName, mProdDesc, mProdDefects, mProdShippingAddress;
    private Button mSubmit;
    private TableRow mAttachmentLayout;
    private RelativeLayout mMainLayout;
    private ArrayList<String> mFilePathList = new ArrayList<String>();
    private PrecaredSharePreferences mPrecaredSharePreferences;
    private NestedScrollView mNestedScrollView;
    private EditText mEdtLine1, mEdtLine2, mEdtCity, mEdtState, mEdtPincode, mEdtPhone;
    private RecyclerView mAddressRecyclerview;
    private SellerApi mSellerApi;
    private TextView mAddAddressTextView;
    private ArrayList<Address> addressArrayList = new ArrayList<Address>();
    private AddressListAdapter addressListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_seller_product);
        mPrecaredSharePreferences = new PrecaredSharePreferences(this);
        mSellerApi = new SellerApi(this, this);
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
        getAddress();

    }

    private void getAddress() {
        mSellerApi.getAddresses(mPrecaredSharePreferences.getAccessToken());
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
        mEdtLine1 = (EditText) findViewById(R.id.edtAddressLine1);
        mEdtLine2 = (EditText) findViewById(R.id.edtAddressLine2);
        mEdtCity = (EditText) findViewById(R.id.edtAddressCity);
        mEdtState = (EditText) findViewById(R.id.edtAddressState);
        mEdtPincode = (EditText) findViewById(R.id.edtAddressPincode);
        mEdtPhone= (EditText) findViewById(R.id.edtAddressMobile);
        mAddressRecyclerview = (RecyclerView) findViewById(R.id.addressRecyclerview);
        mAddressRecyclerview.setHasFixedSize(true);
        mAddressRecyclerview.setNestedScrollingEnabled(false);
        mAddressRecyclerview.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)));
        mAddAddressTextView = (TextView) findViewById(R.id.addAdreessText);
        mAddAddressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSuccessCustomAlert();
            }
        });
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
        params.put(Constants.SellerAddRequestKey.PRODUCT_ADDRESS_ID, ""+addressListAdapter.getAddressID());

        CustomMultipartRequest request = new CustomMultipartRequest(Request.Method.POST, Constants.URL.API_SELLER_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "" + response);
                Utils.closeProgress();
                ShowSnackbar("Your request submitted successfully.");

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       finish();
                    }
                },1000);


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

    public void setAddressLayout(ArrayList<Address> addressArrayList){
        addressListAdapter = new AddressListAdapter(this, addressArrayList);
        mAddressRecyclerview.setAdapter(addressListAdapter);
    }

    @Override
    public void onAddresses(String response) {
            if (StringUtils.isNotEmpty(response)) {
                try {
                    addressArrayList = new ArrayList<Address>();
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("primary_address")) {
                        //JSONObject primaryAddress = jsonObject.getJSONObject("primary_address");
                    }
                    if (jsonObject.has("data")) {
                        JSONArray allAddress = jsonObject.getJSONArray("data");
                        if (allAddress.length() > 0) {
                            for (int i = 0; i < allAddress.length(); i++) {
                                JSONObject addressObj = allAddress.getJSONObject(i);
                                addressArrayList.add(new Address(addressObj, (i == 0 ? true : false)));
                            }
                        }
                    }

                    if (addressArrayList.size() > 0) {
                        mAddressRecyclerview.setVisibility(View.VISIBLE);
                        setAddressLayout(addressArrayList);
                    }else {
                        mAddressRecyclerview.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    }

    @Override
    public void onAddAddresses(String response) {
        Utils.closeProgress();
        Log.e("onAddAddresses", response);
        if (StringUtils.isNotEmpty(response)) {
            if (addressArrayList == null){
                addressArrayList = new ArrayList<Address>();
            }
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("data")) {
                    JSONObject addressObj = jsonObject.getJSONObject("data");
                    addressArrayList.add(new Address(addressObj, false));
                }

                if (addressArrayList.size() > 0) {
                    Toast.makeText(AddSellerProduct.this, "Address added successfully.",Toast.LENGTH_SHORT).show();
                    mAddressRecyclerview.setVisibility(View.VISIBLE);
                    setAddressLayout(addressArrayList);
                    //addressListAdapter.notifyDataSetChanged();
                }else {
                    mAddressRecyclerview.setVisibility(View.GONE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onError(VolleyError error) {
        Log.e("onAddAddressesError", error.toString());
        Utils.closeProgress();
        Toast.makeText(AddSellerProduct.this, "Error",Toast.LENGTH_SHORT).show();

    }

    /**
     * Create custom alert dialogue
     */
    public void showSuccessCustomAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.new_address_layout);
        final EditText line1 = (EditText) dialog.findViewById(R.id.edtAddressLine1);
        final EditText line2 = (EditText) dialog.findViewById(R.id.edtAddressLine2);
        final EditText city = (EditText) dialog.findViewById(R.id.edtAddressCity);
        final EditText state = (EditText) dialog.findViewById(R.id.edtAddressState);
        final EditText pincode = (EditText) dialog.findViewById(R.id.edtAddressPincode);
        final EditText mobile_number = (EditText) dialog.findViewById(R.id.edtAddressMobile);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isNotEmpty(line1.getText().toString())){
                                if (StringUtils.isNotEmpty(mobile_number.getText().toString())){
                                    dialog.dismiss();
                                    Utils.showProgress(AddSellerProduct.this, Constants.VolleyRequestTags.ADD_ADDRESS);
                                    mSellerApi.addAddressesRequest(line1.getText().toString(),line2.getText().toString(),city.getText().toString(),state.getText().toString(),pincode.getText().toString(),mobile_number.getText().toString());
                                }else {
                                    line1.setError(getResources().getString(R.string.mobile_error));
                                }
                }else {
                    line1.setError(getResources().getString(R.string.address_one_error));
                }

            }
        });

    }

    @Override
    public void onSeller(String response, String apiType) {

    }

    @Override
    public void onError(VolleyError error, String apiType) {

    }
}
