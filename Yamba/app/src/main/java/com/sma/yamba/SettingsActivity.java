package com.sma.yamba;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            SettingsFragment settingsFragment = new SettingsFragment();
            getFragmentManager().beginTransaction().add(
                    android.R.id.content,
                    settingsFragment,
                    settingsFragment.getClass().getSimpleName()
            ).commit();
        }
    }

}
