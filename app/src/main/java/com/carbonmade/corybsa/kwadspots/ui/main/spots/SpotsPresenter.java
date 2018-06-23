package com.carbonmade.corybsa.kwadspots.ui.main.spots;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.carbonmade.corybsa.kwadspots.datamodels.Spot;
import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.carbonmade.corybsa.kwadspots.helpers.FirestoreHelper;
import com.carbonmade.corybsa.kwadspots.ui.main.MainActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.moshi.Moshi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

@ActivityScoped
final public class SpotsPresenter implements SpotsContract.Presenter, LocationListener, OnSuccessListener<Location> {
    public static final LatLng GET_FPV = new LatLng(27.3451935, -82.5385566);
    private static final long SECOND = 1000;
    private static final long LOCATION_UPDATE_INTERVAL = 30 * SECOND;
    private static final long LOCATION_UPDATE_INTERVAL_FASTEST = 15 * SECOND;

    private SpotsContract.View mView;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates;
    private Context mContext;
    private Activity mActivity;
    private FirebaseFirestore mFirestore;
    private FirestoreHelper mFirestoreHelper;
    private List<Marker> mMarkers;
    private Moshi mMoshi;

    @Inject
    SpotsPresenter(Context context, MainActivity activity, FirebaseFirestore firestore, Moshi moshi) {
        mContext = context;
        mActivity = activity;
        mFirestore = firestore;
        mFirestoreHelper = new FirestoreHelper(mFirestore);
        mMoshi = moshi;
        mMarkers = new ArrayList<>();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_UPDATE_INTERVAL_FASTEST);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        initLocationListener();
    }

    @Override
    public void onPause() {
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        initLocationListener();
        if(!mRequestingLocationUpdates) {
            startLocationUpdates();
        }

        getSpots();
    }

    @Override
    public void onDestroy() {
        stopLocationUpdates();
        mLocationRequest = null;
        mLocationCallback = null;
    }

    @Override
    public void getCurrentLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(mContext);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        LocationResolver resolver = new LocationResolver();

        task.addOnSuccessListener(resolver);
        task.addOnFailureListener(resolver);
    }

    @Override
    public void onMarkerAdd(LatLng latLng) {
        for(Marker marker : mMarkers) {
            float[] distance = {0};
            Location.distanceBetween(
                    marker.getPosition().latitude,
                    marker.getPosition().longitude,
                    latLng.latitude,
                    latLng.longitude,
                    distance
            );

            if(distance[0] < 50) {
                mView.showError("That's too close to another spot!");
                return;
            }
        }

        HashMap<String, Object> spot = new HashMap<>();

        spot.put(Spot.FIELD_LATITUDE, latLng.latitude);
        spot.put(Spot.FIELD_LONGITUDE, latLng.longitude);

        mView.createSpotSuccess(latLng);
    }

    @Override
    public void cameraMoved() {
        mView.clearMap();
        getSpots();
    }

    @Override
    public void mapReady() {
        getSpots();
    }

    @Override
    public void onSuccess(Location location) {
        if(location != null) {
            mView.focusCurrentLocation(location);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void takeView(SpotsContract.View view) {
        mView = view;
    }

    @Override
    public void dropView() {
        mView = null;
    }

    private void startLocationUpdates() {
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        mRequestingLocationUpdates = false;
    }

    private void initLocationListener() {
        if(mLocationCallback == null) {
            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if(locationResult == null) {
                        return;
                    }

                    mRequestingLocationUpdates = true;
                }
            };
        }
    }

    private void getSpots() {
        LatLngBounds bounds = mView.getVisibleMap();

        if(bounds != null) {
            mFirestoreHelper.getSpots(bounds)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                mMarkers.clear();

                                for(QueryDocumentSnapshot document : task.getResult()) {
                                    Spot spot = new Spot(document);
                                    MarkerOptions options = new MarkerOptions();
                                    options.position(new LatLng(spot.getLatitude(), spot.getLongitude()));
                                    options.title(spot.getName());
                                    options.snippet(mMoshi.adapter(Spot.class).toJson(spot));
                                    mMarkers.add(mView.drawMarker(options));
                                }
                            }
                        }
                    });
        }
    }

    private class LocationResolver implements OnSuccessListener<LocationSettingsResponse>, OnFailureListener {
        @Override
        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
            if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mFusedLocationClient.getLastLocation().addOnSuccessListener(SpotsPresenter.this);
            }
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            if(e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException)e;
                    resolvable.startResolutionForResult(mActivity, 6);
                } catch(IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        }
    }
}
