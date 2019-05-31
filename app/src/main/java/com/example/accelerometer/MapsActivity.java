package com.example.accelerometer;

import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    String[] latitude;
    String[] longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        System.out.println("Maps");
        Intent intent3 = getIntent();
        latitude = intent3.getStringArrayExtra("String latitude");
        longitude = intent3.getStringArrayExtra("String longitude");
    }
    public void onResume(){
        super.onResume();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        /*Double latitude1 = Double.parseDouble(latitude[latitude.length - 1]);
        Double longitude1 = Double.parseDouble(latitude[longitude.length - 1]);
        for (int i = 1; latitude[latitude.length-i] != null; i++) {
            latitude1 = Double.parseDouble(latitude[latitude.length - i]);
            longitude1 = Double.parseDouble(latitude[longitude.length - i]);
        }*/

        String latitude1 = null, longitude1 = null;
        int i = 1;
        while ((latitude[latitude.length -i] == null) || (longitude[longitude.length -i] == null)){
            i++;
        }
        latitude1 = latitude[latitude.length -i];
        longitude1 = longitude[longitude.length -i];
        System.out.println(latitude1+", "+longitude1);
        Double latitude2 = Double.parseDouble(latitude1);
        Double longitude2 = Double.parseDouble(longitude1);
        System.out.println(latitude2+", "+longitude2);
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(latitude2, longitude2);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
