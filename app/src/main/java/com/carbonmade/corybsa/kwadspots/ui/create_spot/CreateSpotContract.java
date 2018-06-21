package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;

import java.io.File;
import java.io.IOException;

interface CreateSpotContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {
        File createImageFile() throws IOException;
    }
}
