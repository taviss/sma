package com.sma.yamba.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String username = preferences.getString("username", "");
        final String password = preferences.getString("password", "");
        final String apiRoot = preferences.getString("api_root", "");

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Handler mHandler = new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Please update your username and password",
                            Toast.LENGTH_LONG).show();
                }
            });

            return;
        }

        Log.d(TAG, "onStart");

        YambaClient yambaClient;

        if(TextUtils.isEmpty(apiRoot)) {
            yambaClient = new YambaClient(username, password);
        } else {
            yambaClient = new YambaClient(username, password, apiRoot);
        }

        try {
            List<YambaClient.Status> timeline = yambaClient.getTimeline(20);
            for(YambaClient.Status status : timeline) {
                Log.i(TAG, String.format("%s: %s", status.getUser(), status.getMessage()));
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