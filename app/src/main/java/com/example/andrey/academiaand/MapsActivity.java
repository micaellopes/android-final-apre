package com.example.andrey.academiaand;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.andrey.academiaand.model.Academia;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Academia academia = AcademiaController.getInstace().getAcademia();
        LatLng joaoPessoa = new LatLng( Double.parseDouble(academia.getLatitude() ),
                Double.parseDouble( academia.getLongitude() ));
        mMap.addMarker(new MarkerOptions().position(joaoPessoa).title( academia.getName() ));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(joaoPessoa));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(joaoPessoa, 110.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(joaoPessoa));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public MarkerOptions createMarker(LatLng latLng, String title, String snipet){
        MarkerOptions marker = new MarkerOptions();
        marker.title(title);
        marker.position(latLng);
        marker.snippet(snipet);
        return marker;
    }
}
