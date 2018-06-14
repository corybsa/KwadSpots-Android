package com.carbonmade.corybsa.kwadspots.ui.main.spots;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.R;
import com.carbonmade.corybsa.kwadspots.di.ActivityScoped;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

@ActivityScoped
public class SpotsFragment extends DaggerFragment implements OnMapReadyCallback, SpotsContract.View {
    public static final int PERMISSION_LOCATION_ACCESS_LOCATION = 1;

    @BindView(R.id.map_view) MapView mMapView;

    @Inject SpotsPresenter mPresenter;

    private GoogleMap mGoogleMap;
    protected BottomSheetDialog mBottomSheetDialog;

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
        MapListener mapListener = new MapListener(this);
        mGoogleMap.setOnMapLongClickListener(mapListener);
        mGoogleMap.setOnMarkerClickListener(mapListener);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        mPresenter.onPause();

        if(mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        mPresenter.onDestroy();

        if(mBottomSheetDialog != null) {
            mBottomSheetDialog.dismiss();
        }
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
    public SpotsFragment getFragment() {
        return this;
    }

    public void focusCurrentLocation(Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
    }

    public class MapListener implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
        private SpotsFragment mSpotsFragment;

        MapListener(SpotsFragment fragment) {
            mSpotsFragment = fragment;
        }

        @Override
        public void onMapLongClick(LatLng latLng) {
            vibrate(80);

            mSpotsFragment.mBottomSheetDialog = new BottomSheetDialog(mSpotsFragment.requireContext());
            View view = getLayoutInflater().inflate(R.layout.fragment_spots_actions, null);
            mSpotsFragment.mBottomSheetDialog.setContentView(view);

            Button button = (Button)view.findViewById(R.id.spots_actions_add_photo);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mSpotsFragment.requireActivity(), "test", Toast.LENGTH_LONG).show();
                }
            });

            mSpotsFragment.mBottomSheetDialog.show();

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title("Test");
            Marker marker = drawMarker(options);
        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(mSpotsFragment.requireActivity(), marker.getTitle(), Toast.LENGTH_SHORT).show();
            return false;
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
}
