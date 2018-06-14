package com.carbonmade.corybsa.kwadspots.ui.main.home;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mHomeView;

    @Inject
    public HomePresenter() {

    }

    @Override
    public void takeView(HomeContract.View view) {
        mHomeView = view;
    }

    @Override
    public void dropView() {
        mHomeView = null;
    }
}
