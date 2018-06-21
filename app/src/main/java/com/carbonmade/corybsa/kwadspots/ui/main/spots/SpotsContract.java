package com.carbonmade.corybsa.kwadspots.ui.main.spots;

import android.location.Location;

import com.carbonmade.corybsa.kwadspots.ui.BasePresenter;
import com.carbonmade.corybsa.kwadspots.ui.BaseView;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

interface SpotsContract {
    interface View extends BaseView<Presenter> {
        Marker drawMarker(MarkerOptions options);
        Circle drawCircle(CircleOptions options);
        void focusCurrentLocation(Location location);
        void createSpotSuccess(String documentId, LatLng latLng);
        void createSpotFailed(String message);
    }

    interface Presenter extends BasePresenter<View> {
        void getCurrentLocation();
        void onMarkerAdd(LatLng latLng);
        void onPause();
        void onResume();
        void onDestroy();
    }
}
