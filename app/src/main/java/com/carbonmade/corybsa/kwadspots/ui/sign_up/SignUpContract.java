package com.carbonmade.corybsa.kwadspots.ui.sign_up;

import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;

interface SignUpContract {
    interface View extends BaseView<Presenter> {
        void onAccountCreated();
        void onAccountFailed(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void onSignUpClicked(String email, String password, String verifyPassword);
    }
}
