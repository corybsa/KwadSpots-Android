package com.carbonmade.corybsa.kwadspots.ui.main.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carbonmade.corybsa.kwadspots.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment implements SearchContract.Presenter {
    @BindView(R.id.searchView) SearchView mSearchView;

    SearchPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        mPresenter = new SearchPresenter(this);

        return view;
    }
}
