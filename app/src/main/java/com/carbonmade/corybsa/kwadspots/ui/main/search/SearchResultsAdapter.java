package com.carbonmade.corybsa.kwadspots.ui.main.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.datamodels.SearchResult;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 0;
    private final int VIEW_LOADING = 1;

    private List<SearchResult> mData;
    private Picasso mPicasso;
    private Context mContext;
    private View mDataView;
    private View mProgressBarView;

    SearchResultsAdapter(List<SearchResult> data, Picasso picasso) {
        mData = data;
        mPicasso = picasso;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        if(viewType == VIEW_ITEM) {
            mDataView = LayoutInflater.from(mContext).inflate(R.layout.search_result_item, parent, false);
            return new SearchResultItemViewHolder(mDataView);
        } else {
            mProgressBarView = LayoutInflater.from(mContext).inflate(R.layout.search_result_loading, parent, false);
            return new SearchResultLoadingViewHolder(mProgressBarView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SearchResultItemViewHolder) {
            ((SearchResultItemViewHolder)holder).bindView(position);
        } else if(holder instanceof SearchResultLoadingViewHolder) {
                SearchResultLoadingViewHolder searchResultLoadingViewHolder = (SearchResultLoadingViewHolder)holder;
                searchResultLoadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                searchResultLoadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position >= mData.size()) {
            return VIEW_LOADING;
        }

        return VIEW_ITEM;
    }

    void hideProgressBar() {
        mProgressBarView.findViewById(R.id.search_result_loading).setVisibility(View.GONE);
    }

    void showProgressBar() {
        mProgressBarView.findViewById(R.id.search_result_loading).setVisibility(View.VISIBLE);
    }

    void addData(List<SearchResult> data) {
        mData.addAll(data);
    }

    public void setData(List<SearchResult> data) {
        mData = data;
    }

    class SearchResultLoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        SearchResultLoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.search_result_loading);
        }
    }

    class SearchResultItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mSpotImage;
        private TextView mSpotName;
        private TextView mSpotType;

        SearchResultItemViewHolder(View view) {
            super(view);
            mSpotImage = view.findViewById(R.id.spot_image);
            mSpotName = view.findViewById(R.id.spot_name);
            mSpotType = view.findViewById(R.id.spot_type);
            view.setOnClickListener(this);
        }

        void bindView(int position) {
            SearchResult result = mData.get(position);

            mPicasso.load(result.getImage())
                    .placeholder(R.drawable.ic_ks_transparent)
                    .resize(48, 48)
                    .into(mSpotImage);

            mSpotName.setText(result.getName());
            mSpotType.setText(result.getType());
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, mSpotName.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}
