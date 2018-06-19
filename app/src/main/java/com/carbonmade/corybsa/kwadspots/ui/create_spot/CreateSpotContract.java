package com.carbonmade.corybsa.kwadspots.ui.create_spot;

import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;

interface CreateSpotContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<View> {
        void typeSelected(int position);
    }
}
