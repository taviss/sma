package com.sma.yamba;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            DetailsFragment detailsFragment = new DetailsFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, detailsFragment, detailsFragment.getClass().getSimpleName()).commit();
        }
    }

}
