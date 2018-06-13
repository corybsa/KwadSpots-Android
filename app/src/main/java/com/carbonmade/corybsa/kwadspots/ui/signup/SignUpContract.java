package com.carbonmade.corybsa.kwadspots.ui.signup;

interface SignUpContract {
    interface View {
        void onAccountCreated();
        void onAccountFailed(String message);
    }

    interface Presenter {
        void onSignUpClicked(String email, String password);
    }
}
