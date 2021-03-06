package com.sma.yamba.services;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;
import com.sma.yamba.YambaApplication;
import com.sma.yamba.YambaApplicationHolder;
import com.sma.yamba.content.DbHelper;
import com.sma.yamba.content.StatusContract;

import java.util.List;

public class RefreshService extends IntentService {
    public static final String TAG = RefreshService.class.getName();

    public RefreshService() {
        super(TAG);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onStart");

        ContentValues values = new ContentValues();

        //YambaClient yambaClient = YambaApplicationHolder.getInstance().getYambaClient();
        YambaClient yambaClient = ((YambaApplication) getApplication()).getYambaClient();

        try {
            List<YambaClient.Status> timeline = yambaClient.getTimeline(20);
            int count = 0;
            for(YambaClient.Status status : timeline) {
                values.clear();
                values.put(StatusContract.Column.ID, status.getId());
                values.put(StatusContract.Column.USER, status.getUser());
                values.put(StatusContract.Column.MESSAGE, status.getMessage());
                values.put(StatusContract.Column.CREATED_AT, status.getCreatedAt().getTime());
                Uri uri = getContentResolver().insert(StatusContract.CONTENT_URI, values);

                if(uri != null) {
                    count++;
                    Log.i(TAG, String.format("%s: %s", status.getUser(), status.getMessage()));
                }
            }

            if(count > 0) {
                sendBroadcast(new Intent("com.sma.yamba.action.NEW_STATUSES").putExtra("count", count));
            }
        } catch(YambaClientException e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }

        return;
    }

    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }
}
