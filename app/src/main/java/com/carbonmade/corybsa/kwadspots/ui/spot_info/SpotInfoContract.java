package com.carbonmade.corybsa.kwadspots.ui.spot_info;

import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;

interface SpotInfoContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void loadComments(String spotId);
    }
}
