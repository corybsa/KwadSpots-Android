package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.helpers.FirestoreHelper;
import com.carbonmade.corybsa.kwadspots.helpers.StorageHelper;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsFragment;
import com.carbonmade.corybsa.kwadspots.ui.main.spots.SpotsPresenter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public class CreateSpotActivity extends DaggerAppCompatActivity implements CreateSpotContract.View {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_CAMERA = 2;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.spot_picture_layout) ConstraintLayout mSpotPictureLayout;
    @BindView(R.id.spot_picture) ImageView mSpotPicture;
    @BindView(R.id.spot_picture_hint) TextView mSpotPictureHint;
    @BindView(R.id.spot_name) TextInputEditText mSpotName;
    @BindView(R.id.spot_type) Spinner mSpotType;
    @BindView(R.id.spot_rating) RatingBar mSpotRating;
    @BindView(R.id.spot_comment) TextInputEditText mSpotComment;

    @Inject FirebaseStorage mFirebaseStorage;
    @Inject FirebaseFirestore mFirestore;
    @Inject CreateSpotPresenter mPresenter;

    private StorageHelper mStorageHelper;
    private FirestoreHelper mFirestoreHelper;
    private String mDocumentId;
    private double mLatitude;
    private double mLongitude;
    private Bitmap mSpotBitmap;
    private File mSpotImageFile;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        mStorageHelper = new StorageHelper(mFirebaseStorage);
        mFirestoreHelper = new FirestoreHelper(mFirestore);
        mDocumentId = intent.getStringExtra(SpotsFragment.KEY_DOCUMENT_ID);
        mLatitude = intent.getDoubleExtra(SpotsFragment.KEY_LATITUDE, SpotsPresenter.GET_FPV.latitude);
        mLongitude = intent.getDoubleExtra(SpotsFragment.KEY_LONGITUDE, SpotsPresenter.GET_FPV.longitude);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Create Spot");
        }

        mPresenter.takeView(this);

        mProgressBar = new ProgressBar(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spots_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpotType.setAdapter(adapter);

        mSpotPictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(CreateSpotActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(CreateSpotActivity.this, Manifest.permission.CAMERA)) {
                        new AlertDialog.Builder(CreateSpotActivity.this)
                                .setCancelable(true)
                                .setTitle("Camera permission necessary")
                                .setMessage("We need to access your camera to take a picture of this spot.")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requestCameraPermissions();
                                    }
                                })
                                .show();
                    } else {
                        requestCameraPermissions();
                    }
                } else {
                    openCamera();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mStorageHelper.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mStorageHelper.restoreUploadState(this, savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mSpotBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse("file:" + mSpotImageFile.getAbsolutePath()));
                mSpotPictureHint.setVisibility(View.GONE);
                mSpotPicture.setImageTintMode(PorterDuff.Mode.CLEAR);
                mSpotPicture.setImageBitmap(mSpotBitmap);
            } catch (IOException e) {
                // ignore for now
            }
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
            case R.id.home:
                finish();
                break;
            case R.id.action_create:
                if(mDocumentId != null && !mDocumentId.isEmpty()) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    mSpotBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    UploadListener uploadListener = new UploadListener(this);

                    mProgressBar.setIndeterminate(false);
                    mProgressBar.setProgress(0);
                    mStorageHelper.upload(mSpotImageFile.getName(), stream.toByteArray())
                            .addOnSuccessListener(uploadListener)
                            .addOnFailureListener(uploadListener)
                            .addOnProgressListener(uploadListener);
                }
                break;
        }

        return false;
    }

    private void requestCameraPermissions() {
        String[] permissions = {Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_CAMERA);
    }

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
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    class UploadListener implements OnSuccessListener<UploadTask.TaskSnapshot>, OnFailureListener, OnProgressListener<UploadTask.TaskSnapshot> {
        private Context mContext;

        UploadListener(Context context) {
            mContext = context;
        }

        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            HashMap<String, Object> spot = new HashMap<>();
            spot.put("picture", mSpotImageFile.getName());
            spot.put("name", mSpotName.getText().toString());
            spot.put("type", mSpotType.getSelectedItemPosition());
            spot.put("rating", mSpotRating.getRating());
            spot.put("comment", mSpotComment.getText().toString());
            spot.put("latitude", mLatitude);
            spot.put("longitude", mLongitude);

            mFirestoreHelper.updateSpot(mDocumentId, spot)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            new AlertDialog.Builder(mContext)
                                    .setMessage(e.getMessage())
                                    .setPositiveButton("Ok", null)
                                    .create()
                                    .show();
                        }
                    });
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mProgressBar.setProgress((int)progress, true);
            } else {
                mProgressBar.setProgress((int)progress);
            }
        }
    }
}
