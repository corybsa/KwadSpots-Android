package com.carbonmade.corybsa.kwadspots.ui.settings;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

@ActivityScoped
public class SettingsPresenter implements SettingsContract.Presenter {
    private SettingsContract.View mView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Inject
    SettingsPresenter(FirebaseAuth auth) {
        mAuth = auth;
        mUser = auth.getCurrentUser();
    }

    @Override
    public void takeView(SettingsContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
