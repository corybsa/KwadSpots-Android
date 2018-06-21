package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import android.os.Environment;

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

@ActivityScoped
final public class CreateSpotPresenter implements CreateSpotContract.Presenter {
    private CreateSpotContract.View mView;

    @Inject
    CreateSpotPresenter() {}

    @Override
    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = ((CreateSpotActivity)mView).getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void takeView(CreateSpotContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }
}
