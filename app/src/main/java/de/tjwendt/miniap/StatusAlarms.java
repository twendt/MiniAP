package de.tjwendt.miniap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

class StatusAlarms {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Context context;
    private StatusUpdater updater;

    public StatusAlarms(Context context) {
        this.context = context;
        this.alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(this.context.getApplicationContext(), UpdateReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(this.context.getApplicationContext(), 0, myIntent, 0);
        updater = new StatusUpdater(context);
    }

    public void start() {
        alarmMgr = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);

        alarmMgr.setRepeating(AlarmManager.RTC,
                System.currentTimeMillis(),
                10 * 1000, alarmIntent);
        Toast.makeText(context, "Alarms enabled", Toast.LENGTH_SHORT);
    }

    public void stop() {
        alarmMgr.cancel(alarmIntent);
        updater.cancelNotification();
        Toast.makeText(context, "Alarms disabled", Toast.LENGTH_SHORT);
    }
}
