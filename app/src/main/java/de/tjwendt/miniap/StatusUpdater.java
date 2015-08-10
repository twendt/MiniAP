package de.tjwendt.miniap;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class StatusUpdater {

    NotificationManager notificationMgr;
    WifiManager wifiManager;
    String[] networkTypes = { "0G", "2G", "3G", "4G"};
    Context context;
    public String networkName = "";
    public String signalStrength = "";
    public String batteryState = "";
    public String jsonStr = "";
    public String statusUrl;
    public String tpSsid;
    public Boolean active;

    public StatusUpdater(Context context) {
        this.context = context;
        notificationMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        tpSsid = sharedPref.getString(SettingsActivity.KEY_PREF_SSID, "");
        statusUrl = sharedPref.getString(SettingsActivity.KEY_PREF_STATUS_URL, "");
        active = sharedPref.getBoolean(SettingsActivity.KEY_PREF_ACTIVE, true);
    }

    public void update() {
        new UpdateStatus(null).execute();
    }

    public void update(MyAsyncListener listener) {
        new UpdateStatus(listener).execute();
    }

    public void cancelNotification() {
        notificationMgr.cancelAll();
    }

    private String makePostRequest() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(statusUrl);

        //Add data to post request
        try {
            httpPost.setEntity(
                    new ByteArrayEntity("{\"module\": \"status\", \"action\": 0}".getBytes()));
        } catch (Exception e) {
            return null;
        }

        //Execute the Post request
        JSONObject json;
        String content;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            content = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            return "";
        }
        return content;
    }

    private class UpdateStatus extends AsyncTask<Void, Void, String> {
        MyAsyncListener listener;

        public UpdateStatus(MyAsyncListener listener) {
            this.listener = listener;
        }

        @Override
        protected String doInBackground(Void... params) {
            wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            if (ssid.equals("\"" + tpSsid + "\"")) {
                jsonStr = makePostRequest();
                if (jsonStr != null && !jsonStr.isEmpty()) {
                    try {
                        JSONObject json = new JSONObject(jsonStr);
                        JSONObject wan = json.getJSONObject("wan");
                        JSONObject battery = json.getJSONObject("battery");
                        int network = wan.getInt("networkType");
                        networkName = networkTypes[network];
                        signalStrength = wan.getString("signalStrength");
                        batteryState = battery.getString("voltage");
                        int resId = context.getResources().getIdentifier("ic_stat_" + networkName.toLowerCase(), "drawable", context.getPackageName());
                        String status = "Network: " + networkName + "\nSignalStrength: " + signalStrength + "\nBattery State: " + batteryState;
                        Notification n = new Notification.Builder(context)
                                .setContentTitle("TP Link Status")
                                .setContentText(status)
                                .setOngoing(true)
                                .setSmallIcon(resId).build();
                        notificationMgr.notify(0, n);
                    } catch (Exception e) {
                        Toast.makeText(context, "TP Link: Failed to parse json",
                                Toast.LENGTH_SHORT).show();
                        return "";
                    }
                    if (listener != null) {
                        listener.onSuccessfulExecute(jsonStr);
                    }
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
