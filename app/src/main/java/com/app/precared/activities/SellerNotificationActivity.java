package com.app.precared.activities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.app.precared.R;
import com.app.precared.adapters.NotificationListAdapter;
import com.app.precared.adapters.ProductListAdapter;
import com.app.precared.models.NotificationRow;
import com.app.precared.models.Product;
import com.app.precared.utils.NotificationDBHandler;

import java.util.ArrayList;

public class SellerNotificationActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;
    private NotificationListAdapter mNotificationListAdapter;
    private NotificationDBHandler mNotificationDBHandler;
    private LinearLayout emptyWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_notification);
        mNotificationDBHandler = new NotificationDBHandler(this);
        findViewByIds();
        setToolbar();
        setProductList();
    }

    private void setProductList() {
        ArrayList<NotificationRow> notiifcationList = mNotificationDBHandler.getAllNotifications();
        if(notiifcationList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyWrapper.setVisibility(View.GONE);
            mNotificationListAdapter = new NotificationListAdapter(this, notiifcationList);
            mRecyclerView.setAdapter(mNotificationListAdapter);
        }else {
            mRecyclerView.setVisibility(View.GONE);
            emptyWrapper.setVisibility(View.VISIBLE);
        }
    }

    private void findViewByIds() {
        emptyWrapper = (LinearLayout) findViewById(R.id.emptyWrapper);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));    }

    /**
     * Set Toolbar : Add app name as title and app logo as toolbar logo
     */
    private void setToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle("Notification");
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()  == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
