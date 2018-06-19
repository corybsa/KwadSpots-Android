package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.carbonmade.corybsa.kwadspots.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class CreateSpotActivity extends DaggerAppCompatActivity implements CreateSpotContract.View {
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Inject CreateSpotPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create Spot");
        }

        mPresenter.takeView(this);
    }
}
