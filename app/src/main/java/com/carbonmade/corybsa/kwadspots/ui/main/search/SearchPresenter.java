package com.carbonmade.corybsa.kwadspots.ui.main.search;

import com.carbonmade.corybsa.kwadspots.datamodels.SearchResult;
import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

@ActivityScoped
final public class SearchPresenter implements SearchContract.Presenter {
    private SearchContract.View mView;

    @Inject
    SearchPresenter() {

    }

    @Override
    public void loadMore() {
        List<SearchResult> fakeData = new ArrayList<>();
        fakeData.add(new SearchResult("", "More spots 1", "Just more spots"));
        fakeData.add(new SearchResult("", "More spots 2", "Just more spots"));
        fakeData.add(new SearchResult("", "More spots 3", "Just more spots"));
        fakeData.add(new SearchResult("", "More spots 4", "Just more spots"));
        fakeData.add(new SearchResult("", "More spots 5", "Just more spots"));

        mView.loadMore(fakeData);
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
