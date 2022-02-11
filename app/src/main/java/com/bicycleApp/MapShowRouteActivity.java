package com.bicycleApp;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.bicycleApp.databinding.ActivityMapShowRouteBinding;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

public class MapShowRouteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapShowRouteBinding binding;
    private LinkedList<LatLng> points = new LinkedList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapShowRouteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.clickable(true);
        polylineOptions.addAll(points);


//        PolylineOptions polylineOptions1 = new PolylineOptions();
//
//        Blammy blammy = new Blammy(polylineOptions1, points);
//
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
//            oos.writeObject(blammy);
//            oos.flush();
//            oos.close();
//            byteArrayOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        //#####################################

//        Polyline polyline = googleMap.addPolyline(new PolylineOptions().clickable(true).add(
//           new LatLng(34.56,12.34),
//                new LatLng(34.56,12.34),
//                new LatLng(33.56,12.34),
//                new LatLng(32.56,12.34),
//                new LatLng(31.56,12.34),
//                new LatLng(30.56,12.34),
//                new LatLng(29.56,12.34),
//                new LatLng(28.56,12.34),
//                new LatLng(27.56,12.34),
//                new LatLng(26.56,12.34),
//                new LatLng(25.56,12.34),
//                new LatLng(24.56,12.34),
//                new LatLng(23.56,12.34),
//                new LatLng(22.56,12.34),
//                new LatLng(21.56,22.34),
//                new LatLng(20.56,12.34),
//                new LatLng(19.56,12.34),
//                new LatLng(18.56,12.34),
//                new LatLng(17.56,18.34),
//                new LatLng(16.56,12.34),
//                new LatLng(15.56,12.34),
//                new LatLng(14.56,12.34),
//                new LatLng(24.56,12.34),
//                new LatLng(34.56,12.34),
//                new LatLng(44.56,36.34),
//                new LatLng(54.56,12.34),
//                new LatLng(64.56,12.34),
//                new LatLng(74.56,12.34),
//                new LatLng(15.56,25.34),
//                new LatLng(20.56,12.34),
//                new LatLng(26.56,12.34),
//                new LatLng(18.56,12.34)
//
//        ));



        //####################################


    }
}