package com.app.precared.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.precared.R;
import com.app.precared.adapters.AddressListAdapter;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.Address;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.StringUtils;
import com.app.precared.utils.Utils;
import com.app.precared.utils.VolleyController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAddressActivity extends AppCompatActivity {

    private AddressListAdapter addressListAdapter;
    private RecyclerView mAddressRecyclerview;
    private PrecaredSharePreferences mPrecaredSharePreferences;
    private ArrayList<Address> addressArrayList;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        mPrecaredSharePreferences = new PrecaredSharePreferences(this);
        mAddressRecyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        mAddressRecyclerview.setHasFixedSize(true);
        mAddressRecyclerview.setNestedScrollingEnabled(false);
        mAddressRecyclerview.setLayoutManager((new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)));
        setToolbar();
        getAddress();
    }

    /**
     * Set Toolbar : Add app name as title and app logo as toolbar logo
     */
    private void setToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("My Addresses");
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAddress() {
        Utils.showProgress(this, Constants.VolleyRequestTags.SELLER_COUNTS_REQUEST);
        String URL = Constants.URL.API_GET_ADDRESSES+"?access_token="+mPrecaredSharePreferences.getAccessToken();
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Utils.closeProgress();
                if (StringUtils.isNotEmpty(response)) {
                    try {
                        addressArrayList = new ArrayList<Address>();
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("primary_address")) {
                          //  JSONObject primaryAddress = jsonObject.getJSONObject("primary_address");
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
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utils.closeProgress();
                if (volleyError != null) {
                    if (volleyError instanceof NoConnectionError) {
                        Toast.makeText(MyAddressActivity.this, "Please connect to internet!", Toast.LENGTH_SHORT).show();
                    }else if (volleyError.networkResponse.statusCode == 500) {
                        try {
                            String response = new String(volleyError.networkResponse.data);
                            JSONObject jsonResponse = new JSONObject(response);
                            Toast.makeText(MyAddressActivity.this, "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else if (volleyError.networkResponse.statusCode == 201) {
                        try {
                            String response = new String(volleyError.networkResponse.data);
                            JSONObject jsonResponse = new JSONObject(response);
                            Toast.makeText(MyAddressActivity.this, "" + jsonResponse.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (volleyError instanceof ServerError) {
                        Toast.makeText(MyAddressActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }  else if (volleyError instanceof TimeoutError) {
                        Toast.makeText(MyAddressActivity.this, "Request timeout!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Adding request to request queue
        VolleyController.getInstance().addToRequestQueue(request, Constants.VolleyRequestTags.SELLER_COUNTS_REQUEST);
    }

    public void setAddressLayout(ArrayList<Address> addressArrayList){
        addressListAdapter = new AddressListAdapter(this, addressArrayList);
        mAddressRecyclerview.setAdapter(addressListAdapter);
    }
}
