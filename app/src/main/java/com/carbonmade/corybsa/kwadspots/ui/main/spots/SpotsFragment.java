package com.carbonmade.corybsa.kwadspots.ui.main.spots;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.datamodels.Spot;
import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.carbonmade.corybsa.kwadspots.helpers.Helpers;
import com.carbonmade.corybsa.kwadspots.ui.create_spot.CreateSpotActivity;
import com.carbonmade.corybsa.kwadspots.ui.spot_info.SpotInfoActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.moshi.Moshi;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class SpotsFragment extends DaggerFragment implements OnMapReadyCallback, SpotsContract.View {
    public static final int PERMISSION_LOCATION_ACCESS_LOCATION = 1;
    public static final String KEY_LATITUDE = "Latitude";
    public static final String KEY_LONGITUDE = "Longitude";
    public static final String KEY_SPOT = "Spot";

    @BindView(R.id.map_view) MapView mMapView;

    @Inject SpotsPresenter mPresenter;
    @Inject Moshi mMoshi;
    @Inject Picasso mPicasso;
    @Inject FirebaseStorage mFirebaseStorage;

    private GoogleMap mGoogleMap;

    @Inject
    public SpotsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spots, container, false);
        ButterKnife.bind(this, view);

        mPresenter.takeView(this);

        mMapView.invalidate();
        mMapView.onCreate(savedInstanceState);

        loadMap();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MapListener mapListener = new MapListener();
        mGoogleMap.setOnMapLongClickListener(mapListener);
        mGoogleMap.setOnCameraIdleListener(mapListener);
        mGoogleMap.setOnInfoWindowClickListener(mapListener);
        mGoogleMap.setInfoWindowAdapter(new SpotInfoWindow(getContext(), mMoshi, mPicasso));
        mPresenter.getCurrentLocation();

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);

        if(
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            mGoogleMap.setMyLocationEnabled(true);
        }

        mPresenter.mapReady();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        clearMap();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_LOCATION_ACCESS_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.getCurrentLocation();
                    mMapView.getMapAsync(this);
                }
                break;
        }
    }

    private void loadMap() {
        if(
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            if(
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
                new AlertDialog.Builder(requireContext())
                        .setCancelable(true)
                        .setTitle("Location permission necessary")
                        .setMessage("We need your location to show you Spots in your area.")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestLocationPermissions();
                            }
                        })
                        .show();
            } else {
                requestLocationPermissions();
            }
        } else {
            mMapView.getMapAsync(this);
        }
    }

    private void requestLocationPermissions() {
        String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
        requestPermissions(perms, PERMISSION_LOCATION_ACCESS_LOCATION);
    }

    @Override
    public Marker drawMarker(MarkerOptions options) {
        if(mGoogleMap != null) {
            return mGoogleMap.addMarker(options);
        }

        return null;
    }

    @Override
    public Circle drawCircle(CircleOptions options) {
        if(mGoogleMap != null) {
            return mGoogleMap.addCircle(options);
        }

        return null;
    }

    @Override
    public void createSpotSuccess(LatLng latLng) {
        Intent intent = new Intent(SpotsFragment.this.getActivity(), CreateSpotActivity.class);
        intent.putExtra(KEY_LATITUDE, latLng.latitude);
        intent.putExtra(KEY_LONGITUDE, latLng.longitude);
        startActivity(intent);
    }

    /**
     * Shows the specified message in an {@link android.support.v7.app.AlertDialog}.
     *
     * @param message the message to show.
     */
    @Override
    public void showError(String message) {
        Helpers.showAlert(getActivity(), message);
    }

    @Override
    public LatLngBounds getVisibleMap() {
        if(mGoogleMap != null) {
            return mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
        } else {
            return null;
        }
    }

    @Override
    public void clearMap() {
        if(mGoogleMap != null) {
            mGoogleMap.clear();
        }
    }

    public void focusCurrentLocation(Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
    }

    public class MapListener implements GoogleMap.OnMapLongClickListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnInfoWindowClickListener {
        MapListener() {

        }

        @Override
        public void onMapLongClick(LatLng latLng) {
            vibrate(30);
            mPresenter.onMarkerAdd(latLng);
        }

        @Override
        public void onCameraIdle() {
            if(!mPresenter.isInfoWindowShown()) {
                mPresenter.cameraMoved();
            }
        }

        @Override
        public void onInfoWindowClick(Marker marker) {
            Intent intent = new Intent(getActivity(), SpotInfoActivity.class);
            intent.putExtra(KEY_SPOT, marker.getSnippet());
            startActivity(intent);
        }

        private void vibrate(long duration) {
            Vibrator vibrator = (Vibrator)SpotsFragment.this.requireActivity().getSystemService(Context.VIBRATOR_SERVICE);

            if(vibrator != null && vibrator.hasVibrator()) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(duration);
                }
            }
        }
    }

    class SpotInfoWindow implements GoogleMap.InfoWindowAdapter {
        private Context mContext;
        private Moshi mMoshi;
        private Picasso mPicasso;

        SpotInfoWindow(Context context, Moshi moshi, Picasso picasso) {
            mContext = context;
            mMoshi = moshi;
            mPicasso = picasso;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(final Marker marker) {
            try {
                LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.spot_info_window, null);

                Spot spot = mMoshi.adapter(Spot.class).fromJson(marker.getSnippet());
                TextView title = view.findViewById(R.id.spot_info_name);
                ImageView picture = view.findViewById(R.id.spot_info_picture);
                RatingBar rating = view.findViewById(R.id.spot_info_rating);

                title.setText(spot.getName());
                rating.setRating(spot.getRating());

                mPicasso.load(spot.getPicture())
                        .placeholder(R.drawable.ic_ks_transparent)
                        .resize(40, 40)
                        .into(picture, new Callback() {
                            @Override
                            public void onSuccess() {
                                if(marker.isInfoWindowShown()) {
                                    marker.hideInfoWindow();
                                    marker.showInfoWindow();
                                }
                            }

                            @Override
                            public void onError() {
                            }
                        });

                return view;
            } catch(IOException e) {
                return null;
            }
        }


    }
}
