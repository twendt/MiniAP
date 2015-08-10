package de.tjwendt.miniap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;

public class UpdateReceiver extends BroadcastReceiver {
    private WifiManager wifiManager;
    String[] networkTypes = { "0G", "2G", "3G", "4G"};
    private String tpSsid;
    private Boolean active;

    public UpdateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        tpSsid = sharedPref.getString(SettingsActivity.KEY_PREF_SSID, "");
        active = sharedPref.getBoolean(SettingsActivity.KEY_PREF_ACTIVE, true);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        if (ssid.equals("\"" + tpSsid + "\"")) {
            StatusUpdater statusUpdater = new StatusUpdater(context);
            statusUpdater.update();
        }
    }

}
