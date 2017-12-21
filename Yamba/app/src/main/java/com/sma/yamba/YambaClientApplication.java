package com.sma.yamba;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.marakana.android.yamba.clientlib.YambaClient;

/**
 * Created by octavian.salcianu on 12/21/2017.
 */

public class YambaClientApplication extends Application implements YambaApplication {
    private static final String TAG = YambaClientApplication.class.getName();

    private YambaClient yambaClient;

    @Override
    public void onCreate() {
        super.onCreate();
        updateYambaClient();
        YambaApplicationHolder.setApplication(this);
    }

    @Override
    public Application getApplication() {
        return this;
    }

    @Override
    public YambaClient getYambaClient() {
        return this.yambaClient;
    }

    /*
    @Override
    public void setYambaClient(YambaClient yambaClient) {
        this.yambaClient = yambaClient;
    }*/

    @Override
    public void updateYambaClient() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String username = preferences.getString("username", "");
        String password = preferences.getString("password", "");
        String apiRoot = preferences.getString("api_root", "");

        Log.i(TAG, "Retrieved username as " + username);
        Log.i(TAG, "Retrieved apiRoot as " + apiRoot);
        //Log.d(TAG, "Retrieved password as " + password);

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            startActivity(new Intent(this, SettingsActivity.class));
        } else {
            if (TextUtils.isEmpty(apiRoot)) {
                yambaClient = new YambaClient(username, password);
            } else {
                yambaClient = new YambaClient(username, password, apiRoot);
            }
        }
    }
}
