package com.app.precared.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.precared.Apis.SellerApi;
import com.app.precared.R;
import com.app.precared.adapters.SellerAdapter;
import com.app.precared.adapters.StateListAdapter;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Seller;
import com.app.precared.models.State;
import com.app.precared.utils.JSONUtil;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.Utils;
import com.app.precared.utils.VolleyController;
import com.segment.analytics.Analytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SellerActivity extends AppCompatActivity implements SellerApi.SellerListener, SellerApi.AddAddressesListener,SellerApi.AddressesListener{

    private static final String TAG = SellerActivity.class.getName();
    private Toolbar mToolbar;
    private Button mAddSellerRequest;
    private SellerApi mSellerApi;
    private PrecaredSharePreferences mPrecaredSharePreferences;
    private ArrayList<Seller> mSellersList = new ArrayList<Seller>();
    private ArrayList<State> mStateList = new ArrayList<State>();
    private RecyclerView mRecyclerView, mStateRecyclerView;
    private SellerAdapter mSellerAdapter;
    private TextView merrorMsg;
    private TextView mListWrapper;
    private TextView amountEarnedText,amountPendingText, TotalAmountEarnedText;
    private StateListAdapter mStateListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbar();
        findViewByIds();
        intitialization();

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
           // executeSellerRequest(bundle.getString("sub_type"));
            executeSellerRequest("all");
        }else {
            executeSellerRequest("all");
        }
        //executeSellerCountsRequest();
        Utils.hitGoogleAnalytics(this, Constants.GoogleAnalyticKey.SELLER_ACTIVITY);
        Analytics.with(this).screen(null,Constants.GoogleAnalyticKey.SELLER_ACTIVITY);
    }

    /**
     * execute seller request
     * @param type
     */
    public void executeSellerRequest(String type) {
        mListWrapper.setText(type.toUpperCase().replace("_"," "));
        Utils.showProgress(this, Constants.SellerKeys.API_SELLER_LISTING);
        mSellerApi.executeSellerRequest(mPrecaredSharePreferences.getAccessToken(), type);
    }

    /**
     * execute seller request
     */
    private void executeSellerCountsRequest() {
      //  Utils.showProgress(this, Constants.SellerKeys.API_SELLER_COUNTS);
        mSellerApi.executeSellerCountsRequest(mPrecaredSharePreferences.getAccessToken());
    }


    /**N
     * initialization classes object
     */
    private void intitialization() {
        mPrecaredSharePreferences = new PrecaredSharePreferences(this);
        mSellerApi = new SellerApi(this, this);
    }

    /**
     * mapping ids
     */
    private void findViewByIds() {
        amountEarnedText = (TextView) findViewById(R.id.amountEarnedText);
        amountPendingText = (TextView) findViewById(R.id.amountPendingtext);
        TotalAmountEarnedText = (TextView) findViewById(R.id.totalAmountEarnedtext);
        merrorMsg = (TextView) findViewById(R.id.errorMsg);
        mListWrapper = (TextView) findViewById(R.id.listWrapper);

        mAddSellerRequest = (Button) findViewById(R.id.startSellingBtn);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStateRecyclerView = (RecyclerView) findViewById(R.id.StateRecyclerView);
        mStateRecyclerView.setHasFixedSize(true);
        mStateRecyclerView.setNestedScrollingEnabled(false);
        mStateRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mAddSellerRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellerActivity.this, AddSellerProduct.class));
            }
        });
    }

    /**
     * Set Toolbar : Add app name as title and app logo as toolbar logo
     */
    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Seller");
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public void onSeller(String response, String apiType) {

        mSellersList.clear();
        Log.e("Response", response);
        if (apiType.equalsIgnoreCase(Constants.SellerKeys.API_SELLER_LISTING)){
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("data")) {
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    if (dataArray.length() > 0) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        merrorMsg.setVisibility(View.GONE);
                        mListWrapper.setVisibility(View.VISIBLE);
                        for (int i = 0; i < dataArray.length(); i++) {
                            Seller seller = new Seller();
                            seller.name = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.NAME);
                            seller.id = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.ID);
                            seller.description = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.DESCRIPTION);
                            seller.seller_price = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.SELLER_PRICE);
                            seller.selling_price = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.SELLING_PRICE);
                            seller.category_name = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.CATEGORY_NAME);
                            seller.display_state = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.DISPLAY_STATE);
                            seller.image_url = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.IMAGE_URL);
                            seller.view_count = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.VIEW_COUNT);
                            seller.myPrice = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.MY_PRICE);
                            seller.precaredPrice = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.PRECARED_PRICE);
                            seller.serviceTax = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.SERVICE_TAX);
                            seller.refurbishCash = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.REFURBISH_PRICE);
                            seller.canHold = JSONUtil.getJSONBoolean(dataArray.getJSONObject(i), Constants.SellerKeys.CAN_HOLD);
                            seller.canPublish = JSONUtil.getJSONBoolean(dataArray.getJSONObject(i), Constants.SellerKeys.CAN_PUBLISH);
                            seller.productUrl = JSONUtil.getJSONString(dataArray.getJSONObject(i), Constants.SellerKeys.PRODUCT_URL);
                            seller.selleName = mPrecaredSharePreferences.getName();
                            seller.selleEmail = mPrecaredSharePreferences.getEmail();
                            seller.selleID = mPrecaredSharePreferences.getUserId();
                            mSellersList.add(seller);
                        }
                        setSellerEarnData();
                        setSellerAdapter();

                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        merrorMsg.setVisibility(View.VISIBLE);
                        mListWrapper.setVisibility(View.GONE);
                    }
                }else {
                    mRecyclerView.setVisibility(View.GONE);
                    merrorMsg.setVisibility(View.VISIBLE);
                    mListWrapper.setVisibility(View.GONE);
                }

                JSONObject seller_request_details = jsonObject.getJSONObject("seller_request_details");
                JSONArray meta_info = seller_request_details.getJSONArray("meta_info");
                if (mStateList.size() > 0){
                    mStateList.clear();
                }
                for (int i = 0; i < meta_info.length(); i++) {
                    State state = new State();
                    state.name = JSONUtil.getJSONString(meta_info.getJSONObject(i), "name");
                    state.stateName = JSONUtil.getJSONString(meta_info.getJSONObject(i), "state_name");
                    state.count = JSONUtil.getJSONIntValue(meta_info.getJSONObject(i), "count");
                    mStateList.add(state);
                }

                setSellerStateAdapter();

                Utils.closeProgress();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private void setSellerEarnData() {
        amountEarnedText.setText("Rs "+mPrecaredSharePreferences.getAmountEarned());
        amountPendingText.setText("Rs "+mPrecaredSharePreferences.getAmountPending());
        TotalAmountEarnedText.setText("Rs "+mPrecaredSharePreferences.getTotalAmountEarned());
    }

    /**
     * set seller adapter
     */
    private void setSellerAdapter() {
        mSellerAdapter = new SellerAdapter(this, mSellersList);
        mRecyclerView.setAdapter(mSellerAdapter);
    }

    /**
     * set seller adapter
     */
    private void setSellerStateAdapter() {
        mStateListAdapter = new StateListAdapter(this, mStateList);
        mStateRecyclerView.setAdapter(mStateListAdapter);
    }

    @Override
    public void onError(VolleyError volleyError, String apiType) {
        Utils.closeProgress();

        if (volleyError != null) {
            Log.e(TAG, volleyError.toString());
            if (volleyError instanceof NoConnectionError) {
                Toast.makeText(this, "Please connect to internet!", Toast.LENGTH_SHORT).show();
            }else if (volleyError.networkResponse.statusCode == 201) {
                try {
                    String response = new String(volleyError.networkResponse.data);
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d(TAG, "" + jsonResponse);
                    Toast.makeText(this, "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (volleyError instanceof ServerError) {
                try {
                    String response = new String(volleyError.networkResponse.data);
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d(TAG, "" + jsonResponse);
                    Toast.makeText(this, "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }  else if (volleyError instanceof TimeoutError) {
                Toast.makeText(this, "Request timeout!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private void sendFilterRequest(String type) {
        executeSellerRequest(type);
    }

    @Override
    public void onAddAddresses(String response) {

    }

    @Override
    public void onAddresses(String response) {

    }

    @Override
    public void onError(VolleyError error) {

    }

    public void updateSellerProduct(final String key, String id) {
        Utils.showProgress(this, Constants.VolleyRequestTags.UPDATE_SELLER_PRODUCT);
        String url = Constants.URL.UPDATE_SELLER_PRODUCT+id+"/update_state.json";
        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.closeProgress();
                Log.d(TAG, "updateSellerProduct: " + response);
                Toast.makeText(SellerActivity.this, "Product state updated.",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utils.closeProgress();
                if (volleyError != null) {
                    Log.e(TAG, volleyError.toString());
                    if (volleyError instanceof NoConnectionError) {
                        Toast.makeText(SellerActivity.this, "Please connect to internet!", Toast.LENGTH_SHORT).show();
                    }else if (volleyError.networkResponse.statusCode == 500) {
                        try {
                            String response = new String(volleyError.networkResponse.data);
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d(TAG, "" + jsonResponse);
                            Toast.makeText(SellerActivity.this, "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (volleyError.networkResponse.statusCode == 201) {
                        try {
                            String response = new String(volleyError.networkResponse.data);
                            JSONObject jsonResponse = new JSONObject(response);
                            Log.d(TAG, "" + jsonResponse);
                            Toast.makeText(SellerActivity.this, "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (volleyError instanceof ServerError) {
                        Toast.makeText(SellerActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }  else if (volleyError instanceof TimeoutError) {
                        Toast.makeText(SellerActivity.this, "Request timeout!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }){ @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();
            params.put("state", key);
            params.put(Constants.LoginKeys.ACCESS_TOKEN, mPrecaredSharePreferences.getAccessToken());
            return params;
        }
        };
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.UPDATE_SELLER_PRODUCT);

    }
}
