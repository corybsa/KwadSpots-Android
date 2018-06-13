package com.carbonmade.corybsa.kwadspots.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.App;
import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.main.home.HomeFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.search.SearchFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainContract.View {
    private static final String KEY_FRAGMENT = "Fragment";

    @BindView(R.id.navigation) BottomNavigationView mNavigationView;

    @Inject
    SharedPreferences mPreferences;

    private MainPresenter mPresenter;
    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ((App)getApplication()).getNetworkComponent().inject(this);

        mPresenter = new MainPresenter(this);
        mPresenter.getSavedFragment(R.id.mainContent);
        mNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mPresenter.onNavigation(item.getItemId());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String fragment = savedInstanceState.getString(KEY_FRAGMENT);

        switch(fragment) {
            case "HomeFragment":
                loadHomeFragment();
                break;
            case "SearchFragment":
                loadSearchFragment();
                break;
            case "SpotsFragment":
                loadSpotsFragment();
                break;
            default:
                loadHomeFragment();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_FRAGMENT, mFragment.getClass().getSimpleName());
    }

    public void loadFragment(Fragment fragment) {
        mFragment = fragment;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContent, mFragment);
        transaction.commit();
    }

    public void loadHomeFragment() {
        loadFragment(new HomeFragment());
    }

    public void loadSpotsFragment() {
        loadFragment(new SpotsFragment());
    }

    public void loadSearchFragment() {
        loadFragment(new SearchFragment());
    }
}
