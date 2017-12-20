package com.sma.yamba;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sma.yamba.content.StatusContract;
import com.sma.yamba.services.RefreshService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            TimelineFragment timelineFragment = new TimelineFragment();
            getFragmentManager().beginTransaction().add(
                    android.R.id.content,
                    timelineFragment,
                    timelineFragment.getClass().getSimpleName()
            ).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;

            case R.id.action_tweet:
                //TODO Don't start a new activity if already in StatusActivity
                startActivity(new Intent(this, StatusActivity.class));
                return true;

            case R.id.action_refresh:
                startService(new Intent(this, RefreshService.class));
                return true;

            case R.id.action_purge:
                int rows = getContentResolver().delete(StatusContract.CONTENT_URI, null, null);
                Toast.makeText(this, "Deleted " + rows + " rows",
                        Toast.LENGTH_LONG).show();
                return true;

            default:
                return false;
        }
    }

}
