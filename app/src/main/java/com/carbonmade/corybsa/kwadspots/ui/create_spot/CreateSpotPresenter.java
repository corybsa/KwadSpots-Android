package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
final public class CreateSpotPresenter implements CreateSpotContract.Presenter {
    private CreateSpotContract.View mView;

    @Inject
    CreateSpotPresenter() {}

    @Override
    public void typeSelected(int position) {

    }

    @Override
    public void takeView(CreateSpotContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
