package com.example.englishlearn.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import com.example.englishlearn.R;

public class BroadcastReceiverInternet extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(!isNextworkAvailable(context)) {
            Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNextworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null){
            return false;
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Network network =connectivityManager.getActiveNetwork();
            if(network == null){
                return false;
            }
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
            if(networkCapabilities == null){
                return false;
            }

            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);

        }else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }
}
