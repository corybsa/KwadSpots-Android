package com.carbonmade.corybsa.kwadspots.ui.main.profile;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class ProfilePresenter implements ProfileContract.Presenter {
    private ProfileContract.View mProfileView;

    @Inject
    public ProfilePresenter() {}

    @Override
    public void takeView(ProfileContract.View view) {
        mProfileView = view;
    }

    @Override
    public void dropView() {
        mProfileView = null;
    }
}
