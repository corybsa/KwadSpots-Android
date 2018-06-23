package com.carbonmade.corybsa.kwadspots.ui.main.home;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
final public class HomePresenter implements HomeContract.Presenter {
    private HomeContract.View mView;

    @Inject
    HomePresenter() {

    }

    @Override
    public void takeView(HomeContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
