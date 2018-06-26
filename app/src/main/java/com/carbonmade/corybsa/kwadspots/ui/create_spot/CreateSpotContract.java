package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

interface CreateSpotContract {
    interface View extends BaseView<Presenter> {
        void openCamera();
        HashMap<String, Object> getSpot();
        void showThumbnail(Bitmap spotBitmap);
        void showProgress(int progress, boolean indeterminate);
        void showError(String message);
    }

    interface Presenter extends BasePresenter<View> {
        File createImageFile() throws IOException;
        void pictureClicked();
        void createSpot(File spotImageFile);
        void onSaveInstanceState(Bundle outState);
        void onRestoreInstanceState(Bundle savedInstanceState);
        void createBitmap(File spotImageFile) throws IOException;
    }
}
