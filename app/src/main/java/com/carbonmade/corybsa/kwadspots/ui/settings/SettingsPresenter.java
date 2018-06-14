package com.carbonmade.corybsa.kwadspots.ui.settings;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

@ActivityScoped
public class SettingsPresenter implements SettingsContract.Presenter {
    private SettingsContract.View mSettingsView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Inject
    SettingsPresenter(FirebaseAuth auth) {
        mAuth = auth;
        mUser = auth.getCurrentUser();
    }

    @Override
    public void takeView(SettingsContract.View view) {
        mSettingsView = view;
    }

    @Override
    public void dropView() {
        mSettingsView = null;
    }
}
