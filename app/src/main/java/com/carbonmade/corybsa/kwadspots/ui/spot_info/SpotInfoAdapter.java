package com.carbonmade.corybsa.kwadspots.ui.spot_info;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.datamodels.SpotComment;
import com.carbonmade.corybsa.kwadspots.helpers.Helpers;
import com.carbonmade.corybsa.kwadspots.services.SpotService;

import java.util.Date;
import java.util.List;

public class SpotInfoAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 0;
    private final int VIEW_LOADING = 1;

    private Context mContext;
    private View mDataView;
    private List<SpotComment> mData;
    private View mProgressBarView;

    SpotInfoAdapter(List<SpotComment> comments) {
        mData = comments;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        if(viewType == VIEW_ITEM) {
            mDataView = LayoutInflater.from(mContext).inflate(R.layout.spot_info_comment_item, parent, false);
            return new SpotInfoItemViewHolder(mDataView);
        } else {
            mProgressBarView = LayoutInflater.from(mContext).inflate(R.layout.search_result_loading, parent, false);
            return new SpotInfoLoadingViewHolder(mProgressBarView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof SpotInfoItemViewHolder) {
            ((SpotInfoItemViewHolder)holder).bindView(position);
        } else if(holder instanceof SpotInfoLoadingViewHolder) {
            SpotInfoLoadingViewHolder spotInfoLoadingViewHolder = (SpotInfoLoadingViewHolder)holder;
            spotInfoLoadingViewHolder.progressBar.setVisibility(View.VISIBLE);
            spotInfoLoadingViewHolder.progressBar.setIndeterminate(true);

            if(mData.size() <= SpotService.COMMENT_PAGE_SIZE) {
                hideProgressBar();
            }
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

    void addData(List<SpotComment> data) {
        mData.addAll(data);
    }

    public void setData(List<SpotComment> data) {
        mData = data;
    }

    class SpotInfoLoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        SpotInfoLoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.search_result_loading);
        }
    }

    class SpotInfoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mComment;
        private TextView mUser;
        private TextView mDate;

        SpotInfoItemViewHolder(View view) {
            super(view);
            mComment = view.findViewById(R.id.spot_info_comments_comment);
            mUser = view.findViewById(R.id.spot_info_comments_name);
            mDate = view.findViewById(R.id.spot_info_comments_date);
            view.setOnClickListener(this);
        }

        void bindView(int position) {
            SpotComment comment = mData.get(position);

            mComment.setText(comment.getComment());
            mUser.setText(comment.getCreatedBy());
            mDate.setText(Helpers.getDateDifference(comment.getCreatedDate(), new Date()));
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "Item clicked", Toast.LENGTH_SHORT).show();
        }
    }
}
