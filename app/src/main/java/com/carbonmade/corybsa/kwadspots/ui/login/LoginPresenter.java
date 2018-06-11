package com.carbonmade.corybsa.kwadspots.ui.login;

import android.support.v7.app.AppCompatActivity;

interface BasePresenter<T> {
    void onAttach(T activity);
}

interface ILoginPresenter<T> extends BasePresenter<T> {
    void onLoginClicked(String username, String password);
}

public class LoginPresenter<T> implements ILoginPresenter<LoginActivity> {
    private LoginActivity mActivity;

    @Override
    public void onAttach(LoginActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onLoginClicked(String username, String password) {
        mActivity.onLoginClick(null);
    }
}
