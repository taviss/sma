package com.sma.yamba.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sma.yamba.MainActivity;

/**
 * Created by octavian.salcianu on 12/20/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    private static final String TAG = NotificationReceiver.class.getName();
    private static final int NOTIFICATION_ID = 42;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int count = intent.getIntExtra("count", 0);

        PendingIntent operation = PendingIntent.getActivity(
                context,
                -1,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_ONE_SHOT
        );

        Notification notification = new Notification.Builder(context)
                .setContentTitle("New tweets")
                .setContentText(count + " new tweets")
                .setSmallIcon(android.R.drawable.sym_action_email)
                .setContentIntent(operation)
                .setAutoCancel(true)
                .getNotification();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
