package com.carbonmade.corybsa.kwadspots.ui.password_reset;

import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;

interface PasswordResetContract {
    interface View extends BaseView<Presenter> {
        void onSubmitSuccess();
        void onSubmitFailed(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void onSubmitClicked(String email);
    }
}
