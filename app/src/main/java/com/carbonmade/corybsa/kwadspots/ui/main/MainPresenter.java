package com.carbonmade.corybsa.kwadspots.ui.main;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.carbonmade.corybsa.kwadspots.R;

public class MainPresenter implements MainContract.Presenter {
    private MainActivity mActivity;

    public MainPresenter(MainActivity activity) {
        mActivity = activity;
    }

    @Override
    public boolean onNavigation(@IdRes int id) {
        switch(id) {
            case R.id.navigation_home:
                mActivity.loadHomeFragment();
                return true;
            case R.id.navigation_spots:
                mActivity.loadSpotsFragment();
                return true;
            case R.id.navigation_search:
                mActivity.loadSearchFragment();
                return true;
        }

        return false;
    }

    @Override
    public void getSavedFragment(@IdRes int id) {
        Fragment fragment = mActivity.getSupportFragmentManager().findFragmentById(id);

        if(fragment == null) {
            mActivity.loadHomeFragment();
        } else {
            mActivity.loadFragment(fragment);
        }
    }
}
