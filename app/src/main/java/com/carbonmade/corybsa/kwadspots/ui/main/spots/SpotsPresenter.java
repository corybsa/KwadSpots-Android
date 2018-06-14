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

import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

@ActivityScoped
public class SpotsPresenter implements SpotsContract.Presenter, LocationListener, OnSuccessListener<Location> {
    public static final LatLng GET_FPV = new LatLng(27.3451935, -82.5385566);
    private static final long SECOND = 1000;
    private static final long LOCATION_UPDATE_INTERVAL = 30 * SECOND;
    private static final long LOCATION_UPDATE_INTERVAL_FASTEST = 15 * SECOND;

    private SpotsContract.View mSpotsView;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates;
    private Context mContext;
    private Activity mActivity;

    @Inject
    SpotsPresenter(Context context, MainActivity activity) {
        mContext = context;
        mActivity = activity;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(LOCATION_UPDATE_INTERVAL_FASTEST);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null) {
                    return;
                }

                // TODO: get spots close to the location

                mRequestingLocationUpdates = true;
            }
        };
    }

    @Override
    public void onPause() {
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        if(!mRequestingLocationUpdates) {
            startLocationUpdates();
        }
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
    public void onSuccess(Location location) {
        if(location != null) {
            mSpotsView.focusCurrentLocation(location);
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

    private void startLocationUpdates() {
        if(ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        mRequestingLocationUpdates = false;
    }

    @Override
    public void takeView(SpotsContract.View view) {
        mSpotsView = view;
    }

    @Override
    public void dropView() {
        mSpotsView = null;
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
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(mActivity, 6);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        }
    }


}
