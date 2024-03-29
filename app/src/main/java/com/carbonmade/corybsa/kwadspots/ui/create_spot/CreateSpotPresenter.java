package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.datamodels.Spot;
import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.carbonmade.corybsa.kwadspots.services.SpotService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

@ActivityScoped
final public class CreateSpotPresenter implements CreateSpotContract.Presenter {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PERMISSION_CAMERA = 2;

    private CreateSpotContract.View mView;
    private CreateSpotActivity mActivity;
    private Bitmap mSpotBitmap;

    @Inject SpotService mSpotService;

    @Inject
    CreateSpotPresenter() {}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mSpotService.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        UploadListener uploadListener = new UploadListener();
        mSpotService.restoreUploadState(savedInstanceState)
                .addOnSuccessListener(uploadListener)
                .addOnFailureListener(uploadListener)
                .addOnProgressListener(uploadListener);
    }
    /**
     * Checks for camera permissions and then informs the view that it should open the camera.
     */
    @Override
    public void pictureClicked() {
        if(ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.CAMERA)) {
                new AlertDialog.Builder(mActivity)
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
            mView.openCamera();
        }
    }

    /**
     * Creates a temporary image file.
     *
     * @return returns a {@link File}.
     * @throws IOException if the file could not be created.
     */
    @Override
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = mActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    /**
     * Converts an image file to a bitmap.
     *
     * @param spotImageFile the image file to convert.
     * @throws IOException
     */
    @Override
    public void createBitmap(File spotImageFile) throws IOException {
        mSpotBitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), Uri.parse("file:" + spotImageFile.getAbsolutePath()));
        mView.showThumbnail(mSpotBitmap);
    }

    /**
     * Compresses the image passed in and then uploads it along with the spot information.
     * If the image is null then just the spot information will be uploaded.
     * @param spotImageFile the image file to upload.
     */
    @Override
    public void createSpot(File spotImageFile) {
        if(spotImageFile != null) {
            new ImageCompresser(spotImageFile.getName()).execute();
        } else {
            uploadSpot();
        }
    }

    @Override
    public void takeView(CreateSpotContract.View view) {
        mView = view;
        mActivity = (CreateSpotActivity)mView;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    /**
     * Gets spot information from the view and then inserts it into the database.
     */
    private void uploadSpot() {
        HashMap<String, Object> spot = mView.getSpot();

        if(!spot.containsKey(Spot.FIELD_NAME) || spot.get(Spot.FIELD_NAME).toString().isEmpty()) {
            mView.showError("The spot must have a name.");
            return;
        }

        if(!spot.containsKey(Spot.FIELD_TYPE) || (int)spot.get(Spot.FIELD_TYPE) < 0) {
            mView.showError("The spot must have a type.");
            return;
        }

        if(!spot.containsKey(Spot.FIELD_RATING) || (float)spot.get(Spot.FIELD_RATING) == 0.0f) {
            mView.showError("The spot must have a rating.");
            return;
        }

        FirestoreListener listener = new FirestoreListener();

        mSpotService.putSpot(mView.getSpot())
                .addOnSuccessListener(listener)
                .addOnFailureListener(listener);
    }

    private void requestCameraPermissions() {
        String[] permissions = {Manifest.permission.CAMERA};
        ActivityCompat.requestPermissions(mActivity, permissions, PERMISSION_CAMERA);
    }

    class UploadListener implements OnSuccessListener<UploadTask.TaskSnapshot>, OnFailureListener, OnProgressListener<UploadTask.TaskSnapshot> {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            uploadSpot();
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
            int progress = (int)((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
            mView.showProgress(progress, false);
        }
    }

    class FirestoreListener implements OnSuccessListener<DocumentReference>, OnFailureListener {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            mActivity.finish();
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            mView.showError(e.getMessage());
        }
    }

    /**
     * Class to compress image file in a separate thread.
     */
    class ImageCompresser extends AsyncTask<Void, Integer, ByteArrayOutputStream> {
        private String mFilename;

        ImageCompresser(String fileName) {
            mFilename = fileName;
        }

        @Override
        protected void onPostExecute(ByteArrayOutputStream stream) {
            super.onPostExecute(stream);
            UploadListener uploadListener = new UploadListener();

            mView.showProgress(0, false);
            mSpotService.upload(mFilename, stream.toByteArray())
                    .addOnSuccessListener(uploadListener)
                    .addOnFailureListener(uploadListener)
                    .addOnProgressListener(uploadListener);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mView.showProgress(0, true);
        }

        @Override
        protected ByteArrayOutputStream doInBackground(Void... v) {
            publishProgress(0);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            mSpotBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream;
        }
    }
}
