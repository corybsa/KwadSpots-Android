package com.carbonmade.corybsa.kwadspots.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.carbonmade.corybsa.kwadspots.App;
import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.main.home.HomeFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.search.SearchFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainContract.View {
    @BindView(R.id.navigation) BottomNavigationView mNavigationView;

    private MainPresenter mPresenter;

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

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.mainContent, fragment);
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
