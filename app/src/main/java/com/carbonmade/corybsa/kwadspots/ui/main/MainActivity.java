package com.carbonmade.corybsa.kwadspots.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.carbonmade.corybsa.kwadspots.App;
import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.main.home.HomeFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.search.SearchFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainContract.View {
    protected static final String KEY_FRAGMENT = "Fragment";

    @BindView(R.id.navigation) BottomNavigationView mNavigationView;

    @Inject MainPresenter mPresenter;
    @Inject Lazy<HomeFragment> mHomeFragmentProvider;
    @Inject Lazy<SearchFragment> mSearchFragmentProvider;
    @Inject Lazy<SpotsFragment> mSpotsFragmentProvider;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mPresenter.takeView(this);
        mPresenter.onCreate();
        mNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mPresenter.onNavigation(item.getItemId());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPresenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FRAGMENT, mFragment.getClass().getSimpleName());
    }


    @Override
    public void loadFragment(Fragment fragment) {
        mFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContent, mFragment);
        transaction.commit();
    }

    @Override
    public void loadHomeFragment() {
        loadFragment(mHomeFragmentProvider.get());
    }

    @Override
    public void loadSpotsFragment() {
        loadFragment(mSpotsFragmentProvider.get());
    }

    @Override
    public void loadSearchFragment() {
        loadFragment(mSearchFragmentProvider.get());
    }


    @Override
    public FragmentManager getMainFragmentManager() {
        return getSupportFragmentManager();
    }
}
