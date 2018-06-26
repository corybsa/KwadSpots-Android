package com.carbonmade.corybsa.kwadspots.ui.spot_info;

import com.carbonmade.corybsa.kwadspots.datamodels.SpotComment;
import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;

import java.util.List;

interface SpotInfoContract {
    interface View extends BaseView<Presenter> {
        void showError(String message);
        void loadComments(List<SpotComment> comments);
    }

    interface Presenter extends BasePresenter<View> {
        void loadComments(String spotId);
    }
}
