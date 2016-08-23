package com.app.precared.fragments;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.precared.R;
import com.app.precared.adapters.NotificationListAdapter;
import com.app.precared.adapters.ProductListAdapter;
import com.app.precared.models.NotificationRow;
import com.app.precared.models.Product;

import java.util.ArrayList;


public class QueryResponseFragment extends Fragment {

    private  NotificationListAdapter mNotificationListAdapter;
    private RecyclerView mRecyclerView;

    public QueryResponseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_status_notification, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewByIds(view);
        setProductList();
    }

    private void findViewByIds(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setProductList() {
        mNotificationListAdapter = new NotificationListAdapter(getActivity(), new ArrayList<NotificationRow>());
        mRecyclerView.setAdapter(mNotificationListAdapter);
    }
}
