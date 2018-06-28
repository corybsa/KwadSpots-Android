package com.carbonmade.corybsa.kwadspots.ui.spot_info;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.datamodels.Spot;
import com.carbonmade.corybsa.kwadspots.datamodels.SpotComment;
import com.carbonmade.corybsa.kwadspots.helpers.Helpers;
import com.carbonmade.corybsa.kwadspots.services.SpotService;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class SpotInfoActivity extends DaggerAppCompatActivity implements SpotInfoContract.View {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.spot_image) ImageView mSpotImage;
    @BindView(R.id.spot_name) TextView mSpotName;
    @BindView(R.id.spot_comment) TextView mSpotComment;
    @BindView(R.id.spot_type) TextView mSpotType;
    @BindView(R.id.spot_rating) RatingBar mSpotRating;
    @BindView(R.id.spot_comments) RecyclerView mRecyclerView;

    @Inject SpotInfoPresenter mPresenter;
    @Inject Moshi mMoshi;
    @Inject Picasso mPicasso;
    @Inject SpotService mSpotService;

    private SpotInfoAdapter mSpotInfoAdapter;

    private Spot mSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_info);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        Intent intent = getIntent();
        String spotJson = intent.getStringExtra(SpotsFragment.KEY_SPOT);

        try {
            mSpot = mMoshi.adapter(Spot.class).fromJson(spotJson);
            String[] spotTypes = getResources().getStringArray(R.array.spots_array);

            if(actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(mSpot.getName());
                actionBar.setSubtitle(mSpot.getUser());
            }

            mPresenter.takeView(this);

            mSpotName.setText(mSpot.getName());
            mSpotComment.setText(mSpot.getComment());
            mSpotRating.setRating(mSpot.getRating());
            mSpotType.setText(spotTypes[(int)mSpot.getType()]);
            mPicasso.load(mSpot.getPicture())
                    .placeholder(R.drawable.ic_ks_transparent)
                    .into(mSpotImage);

            mPresenter.loadComments(mSpot.getId());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads {@code comments} into the {@link RecyclerView} sets the adapter and the scroll listener.
     *
     * @param comments the comments to add to the list view
     */
    @Override
    public void loadComments(final List<SpotComment> comments) {
        mSpotInfoAdapter = new SpotInfoAdapter(comments);
        final RecyclerView.LayoutManager manager = new LinearLayoutManager(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mSpotInfoAdapter);
        mRecyclerView.setLayoutManager(manager);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // if the last visible item is the last item in the data set, then load more comments.
                if(((LinearLayoutManager)manager).findLastVisibleItemPosition() == comments.size()) {
                    mPresenter.loadMore(mSpot.getId());
                }
            }
        });
    }

    /**
     * Loads more comments into the {@link RecyclerView}.
     *
     * @param comments the comments to add to the list view
     */
    @Override
    public void loadMore(List<SpotComment> comments) {
        int count = mSpotInfoAdapter.getItemCount();
        mSpotInfoAdapter.addData(comments);

        if(count == mSpotInfoAdapter.getItemCount()) {
            mSpotInfoAdapter.hideProgressBar();
        } else {
            mRecyclerView.setLayoutAnimation(null);
            mSpotInfoAdapter.showProgressBar();
            mSpotInfoAdapter.notifyDataSetChanged();
            mRecyclerView.scheduleLayoutAnimation();
        }
    }

    /**
     * Shows the specified message in an {@link android.support.v7.app.AlertDialog}.
     *
     * @param message the message to show.
     */
    @Override
    public void showError(String message) {
        Helpers.showAlert(this, message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.spot_info_toolbar, menu);
        Drawable icon = getResources().getDrawable(R.drawable.ic_edit_black_24dp);
        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        menu.findItem(R.id.action_comment).setIcon(icon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_comment:
                Toast.makeText(this, "Good job! you clicked create!", Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }
}
