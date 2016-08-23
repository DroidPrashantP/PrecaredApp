package com.app.precared.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.precared.R;
import com.app.precared.activities.ChatActivity;
import com.app.precared.activities.SellerNotificationActivity;
import com.app.precared.interfaces.Constants;
import com.app.precared.models.NotificationRow;
import com.app.precared.utils.NotificationDBHandler;
import com.app.precared.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Prashant.
 */
public class GcmListenerService extends com.google.android.gms.gcm.GcmListenerService {
    private static final String TAG = GcmListenerService.class.getSimpleName();
    private static final int DEFAULT_DESCRIPTION_LENGTH = 50;
    private LocalBroadcastManager broadcaster;
    private String deal_id;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
        broadcaster = LocalBroadcastManager.getInstance(this);
        // Bundle[{message=Hello, collapse_key=do_not_collapse}]
        Log.e("BundleData", data.toString());
        String mainObject = data.getString("data");
        try {
            JSONObject mainJsonObject = new JSONObject(mainObject);
            String type = mainJsonObject.getString("type");
            String title = mainJsonObject.getString("title");
            String description = mainJsonObject.getString("description");
            String subType = "";
            if(mainJsonObject.has("sub_type")){
                subType = mainJsonObject.getString("sub_type");
            }

            Log.e("Reposne", ""+mainObject);

            if(("message").equalsIgnoreCase(type)){
                // verifying whether the app is in background or foreground
                if (!Utils.isAppIsInBackground(getApplicationContext())) {
                    // app is in foreground, broadcast the push message
                    Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
                    pushNotification.putExtra("message", mainObject);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(mainJsonObject.getString("data"));
                        if (jsonObject.has("message"));{
                            description = jsonObject.getString("message");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    createNotification(title, description,type,subType);
                }
            }
            if(("seller_request").equalsIgnoreCase(type)){
                createNotification(title, description,type,subType);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param title
     * @param description
     */
    public void createNotification(String title, String description, String type, String subtype) {
        NotificationDBHandler notificationDBHandler = new NotificationDBHandler(this);
        NotificationRow notificationRow = new NotificationRow(title, description, subtype);
        notificationDBHandler.addNotification(notificationRow);

        Intent notificationIntent = new Intent(this, ChatActivity.class);
        if(("message").equalsIgnoreCase(type)){
           // title = "You got new message";
            notificationIntent = new Intent(this, ChatActivity.class);
        }
        if(("seller_request").equalsIgnoreCase(type)){
           // title = "Status";
            notificationIntent = new Intent(this, SellerNotificationActivity.class);
        }

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 111, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentText(description);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(title);
        builder.setSmallIcon(R.drawable.chat_icon);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.chat_icon));
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        Notification notification = builder.build();
        notification.when = when;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(100000, notification);
    }

    /**
     * generate normal notification
     */
    private void generateNotification(Intent notificationIntent, String title, String description, int id) {
//        long when = System.currentTimeMillis();
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, Constants.NOTIFICATION_VALUES.LOGIN_NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
//        builder.setContentText(description);
//        builder.setContentIntent(pendingIntent);
//        builder.setContentTitle(title);
//        builder.setSmallIcon(R.drawable.ic_notification_small);
//        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
//        if (mZingoySharedPreference.isSoundAlert()) {
//            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
//        }
//        Notification notification = builder.build();
//        notification.when = when;
//        notification.flags |= Notification.FLAG_AUTO_CANCEL;
//        notificationManager.notify((int) getID(), notification);
    }

}
