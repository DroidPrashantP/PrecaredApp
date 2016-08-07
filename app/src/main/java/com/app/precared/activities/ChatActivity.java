package com.app.precared.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.app.precared.Apis.ChatApi;
import com.app.precared.R;
import com.app.precared.adapters.ChatAdapter;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.MyChats;
import com.app.precared.utils.PrecaredSharePreferences;
import com.app.precared.utils.StringUtils;
import com.app.precared.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ChatActivity extends AppCompatActivity implements Constants.MyChatKeys, ChatApi.ChatListener{

    private static final String TAG = ChatActivity.class.getSimpleName();
    private List<MyChats> mTicketsList = new ArrayList<MyChats>();
    private RecyclerView mRecyclerView;
    private EditText replyEditText;
    private long ticketId = 0;
    private ChatAdapter mAdapter;
    private LinearLayout mMainLayout;
    private LinearLayout mSelectedImagesLayout;
    private Toolbar mToolbar;
    private ChatApi mChatApi;
    private PrecaredSharePreferences mPrecaredSharePreferences;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initialization();
        setToolbar();
        initViews();

        fetchChatData();

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Constants.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };
    }

    private void initialization() {
        mPrecaredSharePreferences = new PrecaredSharePreferences(this);
        mChatApi = new ChatApi(this, this);
    }

    /**
     * fetch chat data from server
     */
    private void fetchChatData() {
        Utils.showProgress(ChatActivity.this, Constants.VolleyRequestTags.CHAT_LIST_REQUEST);
        mChatApi.executeSellerRequest(mPrecaredSharePreferences.getAccessToken());
    }


    /**
     * initializing views
     */
    private void initViews() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        replyEditText = (EditText) findViewById(R.id.replyEditText);
        mMainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mSelectedImagesLayout = (LinearLayout) findViewById(R.id.selectedImageLayout);


        findViewById(R.id.replyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(replyEditText.getText().length() > 0){
                    Utils.showProgress(ChatActivity.this, Constants.VolleyRequestTags.SEND_MEG_REQUEST);
                    mChatApi.sendMessageRequest(mPrecaredSharePreferences.getAccessToken(), replyEditText.getText().toString());

                }else {
                    Toast.makeText(ChatActivity.this, "Enter message.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * parse json object of ticket conversation
     */
    private void parseListResponse(JSONObject jsonObject) throws JSONException {
        try {
            MyChats myChats = new MyChats();
            myChats.sender_id = jsonObject.getString(SENDER_ID);
            myChats.recevier_id = jsonObject.getString(RECEIVER_ID);
            myChats.sender_name = jsonObject.getString(SENDER_NAME);
            myChats.recevier_name = jsonObject.getString(RECEIVER_NAME);
            myChats.message = jsonObject.getString(MESSAGES);
            myChats.id = jsonObject.getString(ID);
            mTicketsList.add(myChats);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * set ticket detail layout
     */
    private void setTicketLayout(List<MyChats> ticketsList) {
        if (ticketsList.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mAdapter = new ChatAdapter(this, ticketsList);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.scrollToPosition(ticketsList.size() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
               onBackPressed();
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
            ab.setTitle("Chat Summary");
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onChatResponse(String response, String apiType) {
        Utils.closeProgress();
        if (apiType.equalsIgnoreCase(Constants.MyChatKeys.API_CHAT_LIST)) {
            if (StringUtils.isNotEmpty(response)) {
                try {
                    JSONObject mainObject = new JSONObject(response);
                    JSONArray dataArray = mainObject.getJSONArray("data");
                    if (dataArray.length() > 0) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            parseListResponse(dataArray.getJSONObject(i));
                        }
                        setTicketLayout(mTicketsList);
                    } else {
                        Toast.makeText(ChatActivity.this, "Chat list is empty", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            if (StringUtils.isNotEmpty(response)) {
                Log.e("Chat reponse", response);
                try {
                    JSONObject mainObject = new JSONObject(response);
                    JSONObject jsonObject = mainObject.getJSONObject("data");
                    MyChats myChats = new MyChats();
                    myChats.sender_id = jsonObject.getString(SENDER_ID);
                    myChats.recevier_id = jsonObject.getString(RECEIVER_ID);
                    myChats.sender_name = jsonObject.getString(SENDER_NAME);
                    myChats.recevier_name = jsonObject.getString(RECEIVER_NAME);
                    myChats.message = jsonObject.getString(MESSAGES);
                    myChats.id = jsonObject.getString(ID);
                    mTicketsList.add(0, myChats);

                    replyEditText.setText("");
                    if (mAdapter != null) {
                        mAdapter.notifyItemInserted(mTicketsList.size() + 1);
                        mRecyclerView.scrollToPosition(mTicketsList.size() - 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onError(VolleyError volleyError, String apiType) {
        Utils.closeProgress();
        Utils.closeProgress();
        if (volleyError != null) {
            Log.e(TAG, volleyError.toString());
            if (volleyError instanceof NoConnectionError) {
                Toast.makeText(this, "Please connect to internet!", Toast.LENGTH_SHORT).show();
            }
            else if (volleyError instanceof ServerError) {
                Toast.makeText(this, "Server Error", Toast.LENGTH_SHORT).show();
            }  else if (volleyError instanceof TimeoutError) {
                Toast.makeText(this, "Request timeout!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     * */
    private void handlePushNotification(Intent intent) {
        String message = (String) intent.getStringExtra("message");
        if (message != null ) {
            try {
                parseListResponse(new JSONObject(message));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter.notifyDataSetChanged();
            if (mAdapter != null) {
                mAdapter.notifyItemInserted(mTicketsList.size() + 1);
                mRecyclerView.scrollToPosition(mTicketsList.size() - 1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constants.PUSH_NOTIFICATION));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
}
