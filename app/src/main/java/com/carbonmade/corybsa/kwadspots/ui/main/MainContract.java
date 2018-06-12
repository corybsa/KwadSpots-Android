package com.carbonmade.corybsa.kwadspots.ui.main;

import android.support.annotation.IdRes;

interface MainContract {
    interface View {

    }

    interface Presenter {
        boolean onNavigation(@IdRes int id);
        void getSavedFragment(@IdRes int id);
    }
}
