package com.carbonmade.corybsa.kwadspots.ui.login;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginActivity mActivity;

    public LoginPresenter(LoginActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onLoginClicked(String username, String password) {
        // TODO: Add actual auth logic.

        mActivity.onLoginSuccess();
    }
}
