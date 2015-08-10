package de.tjwendt.miniap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity implements MyAsyncListener {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll()
                .build();
        StrictMode.setThreadPolicy(policy);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void button_startOnClick(View v) {
        TextView txt1 = (TextView) findViewById(R.id.txt1);
        txt1.setText("Service started");
        StatusAlarms alarms = new StatusAlarms(this);
        alarms.start();
    }

    public void button_stopOnClick(View v) {
        TextView txt1 = (TextView) findViewById(R.id.txt1);
        txt1.setText("Service stopped");
        StatusAlarms alarms = new StatusAlarms(this);
        alarms.stop();

    }

    public void button_updateOnClick(View v) {
        StatusUpdater updater = new StatusUpdater(this);
        updater.update(this);
        //String jsonStr = updater.jsonStr;
        //EditText statusText = (EditText) findViewById(R.id.statusText);
        //statusText.setText(jsonStr);
    }

    public void onSuccessfulExecute(String result) {
        final String str = result;
        final EditText statusText = (EditText) findViewById(R.id.statusText);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusText.setText(str);
            }
        });
    }
}
