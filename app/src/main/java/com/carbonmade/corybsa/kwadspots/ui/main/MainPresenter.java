package com.carbonmade.corybsa.kwadspots.ui.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mMainView;

    @Inject
    MainPresenter() {

    }

    @Override
    public boolean onNavigation(@IdRes int id) {
        switch(id) {
            case R.id.navigation_home:
                mMainView.loadHomeFragment();
                return true;
            case R.id.navigation_spots:
                mMainView.loadSpotsFragment();
                return true;
            case R.id.navigation_search:
                mMainView.loadSearchFragment();
                return true;
        }

        return false;
    }

    @Override
    public void onCreate() {
        Fragment fragment = mMainView.getMainFragmentManager().findFragmentById(R.id.mainContent);

        if(fragment == null) {
            mMainView.loadHomeFragment();
        } else {
            mMainView.loadFragment(fragment);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String fragment = savedInstanceState.getString(MainActivity.KEY_FRAGMENT);

        switch(fragment) {
            case "HomeFragment":
                mMainView.loadHomeFragment();
                break;
            case "SearchFragment":
                mMainView.loadSearchFragment();
                break;
            case "SpotsFragment":
                mMainView.loadSpotsFragment();
                break;
            default:
                mMainView.loadHomeFragment();
                break;
        }
    }

    @Override
    public void takeView(MainContract.View view) {
        mMainView = view;
    }

    @Override
    public void dropView() {
        mMainView = null;
    }
}
