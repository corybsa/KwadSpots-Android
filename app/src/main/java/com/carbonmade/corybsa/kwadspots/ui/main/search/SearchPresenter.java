package com.carbonmade.corybsa.kwadspots.ui.main.search;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import javax.inject.Inject;

@ActivityScoped
public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View mSearchView;

    @Inject
    public SearchPresenter() {

    }

    @Override
    public void takeView(SearchContract.View view) {
        mSearchView = view;
    }

    @Override
    public void dropView() {
        mSearchView = null;
    }
}
