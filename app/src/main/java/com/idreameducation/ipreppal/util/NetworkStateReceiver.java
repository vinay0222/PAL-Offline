package com.idreameducation.ipreppal.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.idreameducation.ipreppal.pal.activity.PracticeTopicActivity;

import java.util.HashSet;
import java.util.Set;

public class NetworkStateReceiver extends BroadcastReceiver {

    public static Set<NetworkStateReceiverListener> listeners;
    public static Boolean connected;

    public NetworkStateReceiver() {
        listeners = new HashSet<NetworkStateReceiverListener>();
        connected = null;
    }

    public void onReceive(Context context, Intent intent) {
        if(intent == null || intent.getExtras() == null)
            return;

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = manager.getActiveNetworkInfo();

        if(ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else if(intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,Boolean.FALSE)) {
            connected = false;
        }

        notifyStateToAll();
    }

    public static void notifyStateToAll() {
        for(NetworkStateReceiverListener listener : listeners)
            notifyState(listener);
    }

    public static void notifyState(NetworkStateReceiverListener listener) {
        if(connected == null || listener == null)
            return;

        if(connected == true)
        {
            listener.networkAvailable();
            try {
                PracticeTopicActivity.dialog.dismiss();
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }
        else
            listener.networkUnavailable();
    }

    public void addListener(NetworkStateReceiverListener l) {
        listeners.add(l);
        notifyState(l);
    }

    public void removeListener(NetworkStateReceiverListener l) {
        listeners.remove(l);
    }

    public interface NetworkStateReceiverListener {
        void networkAvailable();
        void networkUnavailable();
    }
}