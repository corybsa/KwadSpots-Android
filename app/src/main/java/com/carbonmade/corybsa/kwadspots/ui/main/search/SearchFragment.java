package com.carbonmade.corybsa.kwadspots.ui.main.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.datamodels.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

public class SearchFragment extends DaggerFragment implements SearchContract.View {
    @BindView(R.id.search_view) SearchView mSearchView;
    @BindView(R.id.search_results) RecyclerView mRecyclerView;
    @BindView(R.id.search_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject SearchPresenter mPresenter;
    @Inject Picasso mPicasso;

    private SearchResultsAdapter mSearchResultsAdapter;

    @Inject
    public SearchFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        mPresenter.takeView(this);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.drib_purple, R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "test", Toast.LENGTH_SHORT).show();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        loadData();

        return view;
    }

    void setupRecyclerView(final List<SearchResult> data) {
        mSearchResultsAdapter = new SearchResultsAdapter(data, mPicasso);
        final RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSearchResultsAdapter);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // 1 is down
                if(((LinearLayoutManager)manager).findLastVisibleItemPosition() == data.size()) {
                    mPresenter.loadMore();
                }
            }
        });
    }

    void loadData() {
        List<SearchResult> fakeData = new ArrayList<>();
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "Savona", "I like to call this the wasteland."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "Angel's place", "Angel loves to host events at his house!"));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "Savona", "I like to call this the wasteland."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "Angel's place", "Angel loves to host events at his house!"));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));
        fakeData.add(new SearchResult("JPEG_20180626_085647_1625970571241687142.jpg", "The Spot", "This is THE spot."));

        setupRecyclerView(fakeData);
    }

    @Override
    public void loadMore(List<SearchResult> results) {
        int count = mSearchResultsAdapter.getItemCount();
        mSearchResultsAdapter.addData(results);

        if(count == mSearchResultsAdapter.getItemCount()) {
            mSearchResultsAdapter.hideProgressBar();
        } else {
            mRecyclerView.setLayoutAnimation(null);
            mSearchResultsAdapter.showProgressBar();
            mSearchResultsAdapter.notifyDataSetChanged();
            mRecyclerView.scheduleLayoutAnimation();
        }
    }
}
