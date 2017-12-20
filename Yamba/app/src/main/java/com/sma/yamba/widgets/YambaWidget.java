package com.sma.yamba.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.widget.RemoteViews;

import com.sma.yamba.MainActivity;
import com.sma.yamba.R;
import com.sma.yamba.content.StatusContract;

/**
 * Implementation of App Widget functionality.
 */
public class YambaWidget extends AppWidgetProvider {
    private static final String TAG = YambaWidget.class.getName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Cursor cursor = context.getContentResolver().query(StatusContract.CONTENT_URI, null, null, null, StatusContract.DEFAULT_SORT);

        if(!cursor.moveToFirst())
            return;

        String user = cursor.getString(cursor.getColumnIndex(StatusContract.Column.USER));
        String message = cursor.getString(cursor.getColumnIndex(StatusContract.Column.MESSAGE));
        long createdAt = cursor.getLong(cursor.getColumnIndex(StatusContract.Column.CREATED_AT));

        PendingIntent operation = PendingIntent.getActivity(
                context,
                -1,
                new Intent(context, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        for (int appWidgetId : appWidgetIds) {
            RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.yamba_widget);

            view.setTextViewText(R.id.list_item_text_user, user);
            view.setTextViewText(R.id.list_item_text_message, message);
            view.setTextViewText(R.id.list_item_created_at, DateUtils.getRelativeTimeSpanString(createdAt));
            view.setOnClickPendingIntent(R.id.list_item_text_user, operation);
            view.setOnClickPendingIntent(R.id.list_item_text_message, operation);

            appWidgetManager.updateAppWidget(appWidgetId, view);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        this.onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(new ComponentName(context, YambaWidget.class)));
    }
}

