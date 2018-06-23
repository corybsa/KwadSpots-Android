package com.carbonmade.corybsa.kwadspots.ui.main.profile;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
final public class ProfilePresenter implements ProfileContract.Presenter {
    private ProfileContract.View mView;

    @Inject
    ProfilePresenter() {}

    @Override
    public void takeView(ProfileContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
