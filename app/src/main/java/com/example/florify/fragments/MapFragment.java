package com.example.florify.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.florify.R;
import com.example.florify.helpers.MapResolver;
import com.example.florify.helpers.NetworkHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapFragment extends Fragment  implements OnMapReadyCallback {
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private MapResolver mapResolver;
    private NetworkHelper networkHelper;
    private String[] plant_names;
    private ImageButton magnifier;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_map, container, false);

        networkHelper = new NetworkHelper();
        networkHelper.checkLocation(view.getContext());

        mapResolver = new MapResolver(getContext());


        initMap();
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        if(networkHelper.checkLocation(getContext()) && mapResolver.getLastKnownLocation() != null) {

            moveCamera(new LatLng(mapResolver.getLastKnownLocation().getLatitude(), mapResolver.getLastKnownLocation().getLongitude()), 1f);
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
        else {

            Toast.makeText(getContext(), "GPS not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void initMap() {

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_explore);

        mapFragment.getMapAsync(MapFragment.this);
    }

    private void moveCamera(LatLng loc, float zoom) {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, zoom));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;

                            return;
                        }
                    }

                    mLocationPermissionsGranted = true;

                    initMap();
                }
            }
        }
    }

}
