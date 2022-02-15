package com.bicycleApp;

import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.bicycleApp.databinding.ActivityMapShowRouteBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import Data.MyDatabase;
import Model.Highlight;

public class MapShowRouteActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapShowRouteBinding binding;
    private List<LatLng> points = new LinkedList<LatLng>();
    private MyDatabase database;
    private Cursor cursor;
    private int id;
    private double startLat, startLon, endLat, endLon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new MyDatabase(this, 1);
        id = getIntent().getIntExtra("TourId", 0);
        startLat = getIntent().getDoubleExtra("StartLat",0);
        startLon = getIntent().getDoubleExtra("StartLon",0);
        endLat = getIntent().getDoubleExtra("EndLat",0);
        endLon = getIntent().getDoubleExtra("EndLon",0);

        cursor = database.getTourPoints(id);
        while(cursor.moveToNext()){
            points.add(new LatLng(cursor.getDouble(2), cursor.getDouble(3)));
        }

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
        LatLng startLoc = new LatLng(startLat, startLon);
        LatLng endLoc = new LatLng(endLat, endLon);
        mMap.addMarker(new MarkerOptions().position(startLoc).title("Start Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.addMarker(new MarkerOptions().position(endLoc).title("End Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLoc, 10));

        Polyline polyline = googleMap.addPolyline(new PolylineOptions().clickable(true).addAll(points));
        polyline.setColor(Color.MAGENTA);
    }
}