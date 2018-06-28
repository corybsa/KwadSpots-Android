package com.carbonmade.corybsa.kwadspots.ui.main;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.ui.login.LoginActivity;
import com.carbonmade.corybsa.kwadspots.ui.main.home.HomeFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.profile.ProfileFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.search.SearchFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Field;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainContract.View {
    protected static final String KEY_FRAGMENT = "Fragment";

    @BindView(R.id.navigation) BottomNavigationView mBottomNavigationView;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Inject FirebaseAuth mAuth;
    @Inject MainPresenter mPresenter;
    @Inject Lazy<HomeFragment> mHomeFragmentLazy;
    @Inject Lazy<SearchFragment> mSearchFragmentLazy;
    @Inject Lazy<SpotsFragment> mSpotsFragmentLazy;
    @Inject Lazy<ProfileFragment> mProfileFragmentLazy;

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        disableShiftMode(mBottomNavigationView);

        mPresenter.takeView(this);
        mPresenter.onCreate();
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
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

        transaction.setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out);

        transaction.replace(R.id.main_content, mFragment);
        transaction.commit();
    }

    @Override
    public void loadHomeFragment() {
        loadFragment(mHomeFragmentLazy.get());
    }

    @Override
    public void loadSpotsFragment() {
        loadFragment(mSpotsFragmentLazy.get());
    }

    @Override
    public void loadSearchFragment() {
        loadFragment(mSearchFragmentLazy.get());
    }

    @Override
    public void loadProfileFragment() {
        loadFragment(mProfileFragmentLazy.get());
    }

    @Override
    public FragmentManager getMainFragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_logout:
                mAuth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        return false;
    }

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("disableShiftMode", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("disableShiftMode", "Unable to change value of shift mode", e);
        }
    }
}
