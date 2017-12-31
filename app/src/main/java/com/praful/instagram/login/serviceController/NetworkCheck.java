package com.praful.instagram.login.serviceController;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.praful.instagram.login.serviceController.VolleyController;

public class NetworkCheck extends BroadcastReceiver
{
    public static ConnectivityReceiverListener connectionListner;
    public NetworkCheck()
    {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
        if (connectionListner != null) {
            connectionListner.onNetworkChanged(isConnected);
        }
    }

    public static boolean isNewtworkOK() {
        ConnectivityManager cm = (ConnectivityManager) VolleyController.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkChanged(boolean isConnected);
    }
}