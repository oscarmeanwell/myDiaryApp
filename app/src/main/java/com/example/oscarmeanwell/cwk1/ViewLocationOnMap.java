package com.example.oscarmeanwell.cwk1;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewLocationOnMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location_on_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (getIntent().getStringExtra("activity").equals("DisplayEntry")) {
            //if displaying one marker
            Double x = Double.parseDouble(getIntent().getStringExtra("coord"));
            Double y = Double.parseDouble(getIntent().getStringExtra("coord1"));
            LatLng Coords = new LatLng(x, y);
            mMap.addMarker(new MarkerOptions().position(Coords).title("Entry Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(Coords));
        }
        else{
            //NewIntent.co is array of coordinates for all diary entries
            //if called to display all locations
            for (String tmp : NewIntent.co){
                if (!tmp.equals("0.0,0.0")) {
                    String[] coords = tmp.split(",");
                    LatLng coordsnew = new LatLng(Double.parseDouble(coords[0]), Double.parseDouble(coords[1]));
                    mMap.addMarker(new MarkerOptions().position(coordsnew).title(tmp));
                }
            }
        }
    }
}
