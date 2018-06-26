package com.carbonmade.corybsa.kwadspots.ui.spot_info;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.datamodels.SpotComment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SpotInfoAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 0;
    private final int VIEW_LOADING = 1;

    private Context mContext;
    private View mDataView;
    private List<SpotComment> mData;

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();

        if(viewType == VIEW_ITEM) {
            mDataView = LayoutInflater.from(mContext).inflate(R.layout.spot_info_comment_item, parent, false);
            return new
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class SpotInfoItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mComment;
        private TextView mCreatedBy;
        private TextView mCreatedDate;

        SpotInfoItemViewHolder(View view) {
            super(view);
            mComment = view.findViewById(R.id.spot_info_comments_comment);
            mCreatedBy = view.findViewById(R.id.spot_info_comments_name);
            mCreatedDate = view.findViewById(R.id.spot_info_comments_date);
            view.setOnClickListener(this);
        }

        void bindView(int position) {
            SpotComment comment = mData.get(position);

            mComment.setText(comment.getComment());
            mCreatedBy.setText(comment.getCreatedBy());
            mCreatedDate.setText(new SimpleDateFormat(SpotComment.DATE_FORMAT, Locale.US).format(comment.getCreatedDate()));
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
        }
    }
}
