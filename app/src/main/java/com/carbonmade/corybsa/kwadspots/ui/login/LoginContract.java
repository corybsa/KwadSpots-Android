package com.carbonmade.corybsa.kwadspots.ui.login;

interface LoginContract {
    interface View {
        void onLoginSuccess();
        void onLoginFailure(String message);
    }

    interface Presenter {
        void onLoginClicked(String username, String password);
    }
}
