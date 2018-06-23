package com.carbonmade.corybsa.kwadspots.ui.main.search;

import com.carbonmade.corybsa.kwadspots.datamodels.SearchResult;
import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;

import java.util.List;

interface SearchContract {
    interface View extends BaseView<Presenter> {
        void loadMore(List<SearchResult> results);
    }

    interface Presenter extends BasePresenter<View> {
        void loadMore();
    }
}
