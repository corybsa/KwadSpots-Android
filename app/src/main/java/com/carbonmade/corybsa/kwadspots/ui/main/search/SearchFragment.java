package com.carbonmade.corybsa.kwadspots.ui.main.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.carbonmade.corybsa.kwadspots.App;
import com.carbonmade.corybsa.kwadspots.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends Fragment implements SearchContract.Presenter {
    public static final String CLASS_NAME = SearchFragment.class.getSimpleName();

    @BindView(R.id.searchView) SearchView mSearchView;

    SearchPresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);
        ((App)getActivity().getApplication()).getNetworkComponent().inject(this);

        mPresenter = new SearchPresenter(this);

        return view;
    }
}
