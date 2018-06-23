package com.carbonmade.corybsa.kwadspots.ui.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
final public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;

    @Inject
    MainPresenter() {

    }

    @Override
    public boolean onNavigation(@IdRes int id) {
        switch(id) {
            case R.id.navigation_home:
                mView.loadHomeFragment();
                return true;
            case R.id.navigation_spots:
                mView.loadSpotsFragment();
                return true;
            case R.id.navigation_search:
                mView.loadSearchFragment();
                return true;
            case R.id.navigation_profile:
                mView.loadProfileFragment();
                return true;
        }

        return false;
    }

    @Override
    public void onCreate() {
        Fragment fragment = mView.getMainFragmentManager().findFragmentById(R.id.main_content);

        if(fragment == null) {
            mView.loadHomeFragment();
        } else {
            mView.loadFragment(fragment);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        String fragment = savedInstanceState.getString(MainActivity.KEY_FRAGMENT);

        switch(fragment) {
            case "HomeFragment":
                mView.loadHomeFragment();
                break;
            case "SearchFragment":
                mView.loadSearchFragment();
                break;
            case "SpotsFragment":
                mView.loadSpotsFragment();
                break;
            case "ProfileFragment":
                mView.loadProfileFragment();
                break;
            default:
                mView.loadHomeFragment();
                break;
        }
    }

    @Override
    public void takeView(MainContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
