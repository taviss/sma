package com.sma.yamba.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.sma.yamba.services.RefreshService;

/**
 * Created by octavian.salcianu on 12/20/2017.
 */

public class BootReceiver extends BroadcastReceiver {
    private final static String TAG = BootReceiver.class.getName();
    private static final long DEFAULT_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        long interval = Long.parseLong(preferences.getString("interval", Long.toString(DEFAULT_INTERVAL)));

        PendingIntent operation = PendingIntent.getService(
                context,
                -1,
                new Intent(context, RefreshService.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        if(interval == 0) {
            alarmManager.cancel(operation);
            Log.d(TAG, "cancel operation");
        } else {
            alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), interval, operation);
            Log.d(TAG, "repeat for " + interval);
        }

        Log.d(TAG, "onReceive");
    }
}
