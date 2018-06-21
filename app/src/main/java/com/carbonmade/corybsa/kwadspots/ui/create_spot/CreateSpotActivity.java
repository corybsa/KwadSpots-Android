package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.helpers.FirestoreHelper;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsPresenter;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class CreateSpotActivity extends DaggerAppCompatActivity implements CreateSpotContract.View {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.spot_picture) ConstraintLayout mSpotPicture;
    @BindView(R.id.spot_name) TextInputEditText mSpotName;
    @BindView(R.id.spot_type) Spinner mSpotType;
    @BindView(R.id.spot_rating) RatingBar mSpotRating;
    @BindView(R.id.spot_comment) TextInputEditText mSpotComment;

    @Inject CreateSpotPresenter mPresenter;

    private FirestoreHelper mFirestoreHelper;
    private String mDocumentId;
    private double mLatitude;
    private double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        mFirestoreHelper = new FirestoreHelper();
        mDocumentId = intent.getStringExtra(SpotsFragment.KEY_DOCUMENT_ID);
        mLatitude = intent.getDoubleExtra(SpotsFragment.KEY_LATITUDE, SpotsPresenter.GET_FPV.latitude);
        mLongitude = intent.getDoubleExtra(SpotsFragment.KEY_LONGITUDE, SpotsPresenter.GET_FPV.longitude);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create Spot");
        }

        mPresenter.takeView(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spots_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpotType.setAdapter(adapter);

        mSpotPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            mImageView.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_spots_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_create:
                if(mDocumentId != null && !mDocumentId.isEmpty()) {
                    HashMap<String, Object> spot = new HashMap<>();
                    spot.put("picture", "");
                    spot.put("name", mSpotName.getText().toString());
                    spot.put("type", mSpotType.getSelectedItemPosition());
                    spot.put("rating", mSpotRating.getRating());
                    spot.put("comment", mSpotComment);
                    spot.put("latitude", mLatitude);
                    spot.put("longitude", mLongitude);

                    mFirestoreHelper.updateSpot(mDocumentId, spot);
                }
                break;
        }

        return false;
    }
}
