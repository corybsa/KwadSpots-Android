package com.carbonmade.corybsa.kwadspots.ui.main;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;

interface MainContract {
    interface View extends BaseView<Presenter> {
        void loadFragment(Fragment fragment);
        void loadHomeFragment();
        void loadSearchFragment();
        void loadSpotsFragment();
        FragmentManager getMainFragmentManager();
    }

    interface Presenter extends BasePresenter<View> {
        boolean onNavigation(@IdRes int id);
        void onCreate();
        void onRestoreInstanceState(Bundle savedInstanceState);
    }
}
