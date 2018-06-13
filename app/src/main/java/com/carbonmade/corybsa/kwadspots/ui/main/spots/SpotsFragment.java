package com.carbonmade.corybsa.kwadspots.ui.main.spots;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.carbonmade.corybsa.kwadspots.App;
import com.carbonmade.corybsa.kwadspots.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SpotsFragment extends Fragment implements OnMapReadyCallback, SpotsContract.View {
    public static final int PERMISSION_LOCATION_ACCESS_LOCATION = 1;

    @BindView(R.id.mapView) MapView mMapView;

    private SpotsPresenter mPresenter;
    private GoogleMap mGoogleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spots, container, false);
        ButterKnife.bind(this, view);
        ((App)getActivity().getApplication()).getNetworkComponent().inject(this);

        mPresenter = new SpotsPresenter(this);

        mMapView.invalidate();
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        MapListener mapListener = new MapListener(this);
        mGoogleMap.setOnMapLongClickListener(mapListener);
        mGoogleMap.setOnMarkerClickListener(mapListener);
        mPresenter.getCurrentLocation();

        enableMyLocationIfPermitted();

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_LOCATION_ACCESS_LOCATION:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.getCurrentLocation();
                }
                break;
        }
    }

    private void enableMyLocationIfPermitted() {
        if(
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermissions();
        } else {
            mGoogleMap.setMyLocationEnabled(true);
        }
    }

    private void requestLocationPermissions() {
        String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
        ActivityCompat.requestPermissions(requireActivity(), perms, PERMISSION_LOCATION_ACCESS_LOCATION);
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

    public void focusCurrentLocation(Location location) {
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15f));
    }

    public void onLocationResolved(Location location) {
        focusCurrentLocation(location);
    }

    public class MapListener implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener {
        private SpotsFragment mSpotsFragment;

        public MapListener(SpotsFragment fragment) {
            mSpotsFragment = fragment;
        }

        @Override
        public void onMapLongClick(LatLng latLng) {
            BottomSheetDialog sheet = new BottomSheetDialog(mSpotsFragment.requireContext());
            View view = getLayoutInflater().inflate(R.layout.fragment_spots_actions, null);
            sheet.setContentView(view);

            Button button = (Button)view.findViewById(R.id.spots_actions_add_photo);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mSpotsFragment.getActivity(), "test", Toast.LENGTH_LONG).show();
                }
            });

            sheet.show();

            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title("Test");
            Marker marker = drawMarker(options);
        }

        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(mSpotsFragment.getActivity(), marker.getTitle(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
