package com.app.precared.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.app.precared.Apis.SellerApi;
import com.app.precared.R;
import com.app.precared.adapters.SellerAdapter;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Seller;
import com.app.precared.utils.JSONUtil;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SellerActivity extends AppCompatActivity implements SellerApi.SellerListener , View.OnClickListener{

    private static final String TAG = SellerActivity.class.getName();
    private Toolbar mToolbar;
    private Button mAddSellerRequest;
    private SellerApi mSellerApi;
    private PrecaredSharePreferences mPrecaredSharePreferences;
    private ArrayList<Seller> mSellersList = new ArrayList<Seller>();
    private RecyclerView mRecyclerView;
    private SellerAdapter mSellerAdapter;
    private TextView mPendingCount, mInProgressCount, mLiveCount, mReturnCount, mNewCount, mAllCount;
    private RelativeLayout mPendingLayout, mInProgressLayout, mLiveLayout, mReturnLayout, mNewLayout, mAllLayout;
    private TextView merrorMsg;
    private TextView mListWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbar();
        findViewByIds();
        intitialization();
        executeSellerRequest("all");
        executeSellerCountsRequest();
    }

    /**
     * execute seller request
     * @param type
     */
    private void executeSellerRequest(String type) {
        mListWrapper.setText(type.toUpperCase().replace("_"," ") +" Product");
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


    /**
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
        mPendingCount = (TextView) findViewById(R.id.pending_count);
        mInProgressCount = (TextView) findViewById(R.id.progress_count);
        mLiveCount = (TextView) findViewById(R.id.live_count);
        mReturnCount = (TextView) findViewById(R.id.return_count);
        mNewCount = (TextView) findViewById(R.id.new_count);
        mAllCount = (TextView) findViewById(R.id.all_count);

        mPendingLayout = (RelativeLayout) findViewById(R.id.pendingLayout);
        mInProgressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        mLiveLayout = (RelativeLayout) findViewById(R.id.liveLayout);
        mReturnLayout = (RelativeLayout) findViewById(R.id.returnLayout);
        mNewLayout = (RelativeLayout) findViewById(R.id.NewLayout);
        mAllLayout = (RelativeLayout) findViewById(R.id.AllLayout);

        merrorMsg = (TextView) findViewById(R.id.errorMsg);
        mListWrapper = (TextView) findViewById(R.id.listWrapper);

        mPendingLayout.setOnClickListener(this);
        mInProgressLayout.setOnClickListener(this);
        mLiveLayout.setOnClickListener(this);
        mReturnLayout.setOnClickListener(this);
        mNewLayout.setOnClickListener(this);
        mAllLayout.setOnClickListener(this);

        mAddSellerRequest = (Button) findViewById(R.id.startSellingBtn);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        Utils.closeProgress();
        mSellersList.clear();
        Log.e("Response", response);
        if (apiType.equalsIgnoreCase(Constants.SellerKeys.API_SELLER_LISTING)){
            try {
                JSONObject jsonObject = new JSONObject(response);
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
                        if (dataArray.getJSONObject(i).has("seller")) {
                            JSONObject sellerObject = dataArray.getJSONObject(i).getJSONObject("seller");
                            seller.selleName = JSONUtil.getJSONString(sellerObject, Constants.SellerKeys.SELLER_NAME);
                            seller.selleEmail = JSONUtil.getJSONString(sellerObject, Constants.SellerKeys.SELLER_EMAIL);
                            seller.selleID = JSONUtil.getJSONString(sellerObject, Constants.SellerKeys.SELLER_ID);
                        }
                        mSellersList.add(seller);
                    }
                    setSellerAdapter();

                } else {
                    mRecyclerView.setVisibility(View.GONE);
                    merrorMsg.setVisibility(View.VISIBLE);
                    mListWrapper.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject dataObject = jsonObject.getJSONObject("data");
                String id = dataObject.getString("id");
                JSONObject sellerRequestsCount = dataObject.getJSONObject("seller_requests");
                setSellerDashboardCount(sellerRequestsCount);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * set dashboard counts
     * @param sellerRequestsCount
     */
    private void setSellerDashboardCount(JSONObject sellerRequestsCount) {
        try {
            int pendingCount = sellerRequestsCount.getInt("Pending");
            int inProcessCount = sellerRequestsCount.getInt("In Process");
            int liveCount = sellerRequestsCount.getInt("Live");
            int returnCount = sellerRequestsCount.getInt("Return");
            int newCount = sellerRequestsCount.getInt("New");
            int allCount = sellerRequestsCount.getInt("All");

            if (pendingCount > 0){
                mPendingCount.setVisibility(View.VISIBLE);
                mPendingCount.setText(""+pendingCount);
            }else {
                mPendingCount.setVisibility(View.GONE);
            }
            if (inProcessCount > 0){
                mInProgressCount.setVisibility(View.VISIBLE);
                mInProgressCount.setText(""+inProcessCount);
            }else {
                mInProgressCount.setVisibility(View.GONE);
            }
            if (liveCount > 0){
                mLiveCount.setVisibility(View.VISIBLE);
                mLiveCount.setText(""+liveCount);
            }else {
                mLiveCount.setVisibility(View.GONE);
            }
            if (returnCount > 0){
                mReturnCount.setVisibility(View.VISIBLE);
                mReturnCount.setText(""+returnCount);
            }else {
                mReturnCount.setVisibility(View.GONE);
            }
            if (newCount > 0){
                mNewCount.setVisibility(View.VISIBLE);
                mNewCount.setText(""+newCount);
            }else {
                mNewCount.setVisibility(View.GONE);
            }

            if (allCount > 0){
                mAllCount.setVisibility(View.VISIBLE);
                mAllCount.setText(""+allCount);
            }else {
                mAllCount.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * set seller adapter
     */
    private void setSellerAdapter() {
        mSellerAdapter = new SellerAdapter(this, mSellersList);
        mRecyclerView.setAdapter(mSellerAdapter);
    }

    @Override
    public void onError(VolleyError volleyError, String apiType) {
        Utils.closeProgress();

        if (volleyError != null) {
            Log.e(TAG, volleyError.toString());
            if (volleyError instanceof NoConnectionError) {
                Toast.makeText(this, "Please connect to internet!", Toast.LENGTH_SHORT).show();
            }else if (volleyError.networkResponse.statusCode == 500) {
                try {
                    String response = new String(volleyError.networkResponse.data);
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d(TAG, "" + jsonResponse);
                    Toast.makeText(this, "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (volleyError.networkResponse.statusCode == 201) {
                try {
                    String response = new String(volleyError.networkResponse.data);
                    JSONObject jsonResponse = new JSONObject(response);
                    Log.d(TAG, "" + jsonResponse);
                    Toast.makeText(this, "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (volleyError instanceof ServerError) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.pendingLayout:
               sendFilterRequest("ready");
               break;
           case R.id.progressLayout:
               sendFilterRequest("in_progress");
               break;
           case R.id.liveLayout:
               sendFilterRequest("live");
               break;
           case R.id.returnLayout:
               sendFilterRequest("return");
               break;
           case R.id.NewLayout:
               sendFilterRequest("new_request");
               break;
           case R.id.AllLayout:
               sendFilterRequest("all");
               break;
       }
    }

    private void sendFilterRequest(String type) {
        executeSellerRequest(type);
    }
}
