package com.carbonmade.corybsa.kwadspots.ui.login;

public class LoginPresenter implements LoginContract.Presenter {
    private LoginActivity mActivity;

    public LoginPresenter(LoginActivity activity) {
        mActivity = activity;
    }

    @Override
    public void onLoginClicked(String username, String password) {
        // TODO: Remove this before prod release.
        username = "test";
        password = "test";

        if(username.isEmpty()) {
            mActivity.onLoginFailure("Username cannot be empty.");
        } else if(password.isEmpty()) {
            mActivity.onLoginFailure("Password cannot be empty.");
        } else {
            mActivity.onLoginSuccess();
        }
    }
}
