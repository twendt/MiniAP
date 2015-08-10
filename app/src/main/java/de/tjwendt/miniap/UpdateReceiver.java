package de.tjwendt.miniap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class UpdateReceiver extends BroadcastReceiver {
    WifiManager wifiManager;
    String[] networkTypes = { "0G", "2G", "3G", "4G"};

    public UpdateReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        if (ssid.equals("\"MiniAP\"")) {
            StatusUpdater statusUpdater = new StatusUpdater(context);
            statusUpdater.update();
        }
    }

}
