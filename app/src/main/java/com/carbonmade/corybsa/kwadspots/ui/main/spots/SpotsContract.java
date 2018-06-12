package com.carbonmade.corybsa.kwadspots.ui.main.spots;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

interface SpotsContract {
    interface View {
        Marker drawMarker(MarkerOptions options);
        Circle drawCircle(CircleOptions options);
    }

    interface Presenter {
        void getCurrentLocation();
        void onPause();
        void onResume();
    }
}
