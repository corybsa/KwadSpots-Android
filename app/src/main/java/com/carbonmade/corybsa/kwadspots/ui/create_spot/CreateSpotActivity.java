package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.datamodels.Spot;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsPresenter;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

public class CreateSpotActivity extends DaggerAppCompatActivity implements CreateSpotContract.View {
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.spot_picture_layout) ConstraintLayout mSpotPictureLayout;
    @BindView(R.id.spot_picture) ImageView mSpotPicture;
    @BindView(R.id.spot_picture_hint) TextView mSpotPictureHint;
    @BindView(R.id.spot_name) TextInputEditText mSpotName;
    @BindView(R.id.spot_type) Spinner mSpotType;
    @BindView(R.id.spot_rating) RatingBar mSpotRating;
    @BindView(R.id.spot_comment) TextInputEditText mSpotComment;
    @BindView(R.id.progress_bar) ProgressBar mProgressBar;

    @Inject CreateSpotPresenter mPresenter;

    private double mLatitude;
    private double mLongitude;
    private File mSpotImageFile;

    @OnClick(R.id.create_spot_button)
    void onCreateClicked() {
        mPresenter.createSpot(mSpotImageFile);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        mLatitude = intent.getDoubleExtra(SpotsFragment.KEY_LATITUDE, SpotsPresenter.GET_FPV.latitude);
        mLongitude = intent.getDoubleExtra(SpotsFragment.KEY_LONGITUDE, SpotsPresenter.GET_FPV.longitude);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create Spot");
        }

        mPresenter.takeView(this);

        mProgressBar.setIndeterminate(false);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spots_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpotType.setAdapter(adapter);

        mSpotPictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.pictureClicked();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPresenter.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case CreateSpotPresenter.PERMISSION_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CreateSpotPresenter.REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mPresenter.createBitmap(mSpotImageFile);
            } catch (IOException e) {
                // ignore for now
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return false;
    }

    @Override
    public void showThumbnail(Bitmap spotBitmap) {
        mSpotPictureHint.setVisibility(View.GONE);
        mSpotPicture.setImageBitmap(spotBitmap);
    }

    @Override
    public void showProgress(int progress, boolean indeterminate) {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setIndeterminate(indeterminate);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mProgressBar.setProgress(progress, true);
        } else {
            mProgressBar.setProgress(progress);
        }
    }

    @Override
    public void showError(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .create()
                .show();
    }

    @Override
    public HashMap<String, Object> getSpot() {
        HashMap<String, Object> spot = new HashMap<>();

        if(mSpotImageFile != null) {
            spot.put(Spot.FIELD_PICTURE, mSpotImageFile.getName());
        }

        spot.put(Spot.FIELD_NAME, mSpotName.getText().toString());
        spot.put(Spot.FIELD_TYPE, mSpotType.getSelectedItemPosition());
        spot.put(Spot.FIELD_RATING, mSpotRating.getRating());
        spot.put(Spot.FIELD_COMMENT, mSpotComment.getText().toString());
        spot.put(Spot.FIELD_LATITUDE, mLatitude);
        spot.put(Spot.FIELD_LONGITUDE, mLongitude);

        return spot;
    }

    @Override
    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;

            try {
                photoFile = mPresenter.createImageFile();
            } catch(IOException ex) {
                View view = findViewById(R.id.create_spot_layout);
                Snackbar.make(view, "Failed to get image.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openCamera();
                            }
                        })
                        .show();
            }

            if(photoFile != null) {
                mSpotImageFile = photoFile;
                Uri photoUri = FileProvider.getUriForFile(this, "com.carbonmade.corybsa.kwadspots", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, CreateSpotPresenter.REQUEST_IMAGE_CAPTURE);
            }
        }
    }
}
