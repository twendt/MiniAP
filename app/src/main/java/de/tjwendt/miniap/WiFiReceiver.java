package de.tjwendt.miniap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WiFiReceiver extends BroadcastReceiver {
    private WifiManager wifiManager;

    public WiFiReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        NetworkInfo networkInfo = intent
                .getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        NetworkInfo.State state;
        if (networkInfo != null) {
            state = networkInfo.getState();
        } else {
            return;
        }
        if (!state.toString().equals("CONNECTED") && !state.toString().equals("DISCONNECTED")) {
            return;
        }
        StatusUpdater statusUpdater = new StatusUpdater(context);
        StatusAlarms alarms = new StatusAlarms(context);
        String ssid = wifiInfo.getSSID();
        if (ssid.equals("\"MiniAP\"")) {
            statusUpdater.update();
            alarms.start();
        } else {
            alarms.stop();
    }
    }

}