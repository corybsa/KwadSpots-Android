package com.carbonmade.corybsa.kwadspots.ui.main.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carbonmade.corybsa.kwadspots.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class SearchFragment extends DaggerFragment implements SearchContract.View {
    @BindView(R.id.search_view) SearchView mSearchView;

    @Inject SearchPresenter mPresenter;

    @Inject
    public SearchFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        mPresenter.takeView(this);

        return view;
    }
}
