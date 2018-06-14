package com.carbonmade.corybsa.kwadspots.ui.login;

import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;

interface LoginContract {
    interface View extends BaseView<Presenter> {
        void onLoginSuccess();
        void onLoginFailure(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void onLoginClicked(String username, String password);
        void onResume();
    }
}
