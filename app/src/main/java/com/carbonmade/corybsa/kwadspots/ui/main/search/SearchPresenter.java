package com.carbonmade.corybsa.kwadspots.ui.main.search;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
final public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View mView;

    @Inject
    SearchPresenter() {

    }

    @Override
    public void takeView(SearchContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
