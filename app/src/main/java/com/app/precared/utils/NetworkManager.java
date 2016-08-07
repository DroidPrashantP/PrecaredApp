package com.app.precared.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Prashant on 08/1/16.
 */
public class NetworkManager {
    private static final String TAG = NetworkManager.class.getSimpleName();

    /**
     * Check Connection
     *
     * @param ctx
     * @return
     */
    public static boolean isConnectedToInternet(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = connectivityManager.getActiveNetworkInfo();
        return ni != null && ni.isAvailable() && ni.isConnected();
    }
}
