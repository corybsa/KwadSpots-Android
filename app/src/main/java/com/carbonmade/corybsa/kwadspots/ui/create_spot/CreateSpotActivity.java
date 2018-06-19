package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class CreateSpotActivity extends DaggerAppCompatActivity implements CreateSpotContract.View {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.spot_type) Spinner mSpinner;

    @Inject CreateSpotPresenter mPresenter;

    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        mLatitude = intent.getDoubleExtra(SpotsFragment.KEY_LATITUDE, SpotsPresenter.GET_FPV.latitude);
        mLongitude = intent.getDoubleExtra(SpotsFragment.KEY_LONGITUDE, SpotsPresenter.GET_FPV.longitude);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create Spot");
        }

        mPresenter.takeView(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spots_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.typeSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing.
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_spots_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_create:
                Toast.makeText(this, "Spot created!", Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }
}
