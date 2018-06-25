package com.carbonmade.corybsa.kwadspots.ui.spot_info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.datamodels.Spot;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;
import com.squareup.moshi.Moshi;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class SpotInfoActivity extends DaggerAppCompatActivity implements SpotInfoContract.View {
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Inject SpotInfoPresenter mPresenter;
    @Inject Moshi mMoshi;

    private Spot mSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        String spotJson = intent.getStringExtra(SpotsFragment.KEY_SPOT);

        try {
            mSpot = mMoshi.adapter(Spot.class).fromJson(spotJson);

            if(actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mSpot.getName());
            }

            mPresenter.takeView(this);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO: Change this to the actual toolbar menu.
        getMenuInflater().inflate(R.menu.create_spots_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return false;
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }
}
