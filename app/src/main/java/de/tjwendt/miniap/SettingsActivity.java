package de.tjwendt.miniap;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
    public static String KEY_PREF_STATUS_URL = "pref_status_url";
    public static String KEY_PREF_SSID = "pref_ssid";
    public static String KEY_PREF_ACTIVE = "pref_active";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}