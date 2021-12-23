package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.LinkedList;

import Data.MyDatabase;
import Model.Trip;

public class TripsListActivity extends AppCompatActivity {

    private MyDatabase database;
    private LinkedList<Trip> tripList = new LinkedList<>();
    private Button button;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);

        button = findViewById(R.id.button);
        button.setOnClickListener((view -> {placeholder();}));
        database = new MyDatabase(this,1);
        cursor = database.getAllTrips();
        while(cursor.moveToNext()){
            Trip trip = new Trip(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            if(cursor.getInt(3) == 0)
                trip.setNotification(false);

            tripList.add(trip);
        }

    }

    private void placeholder(){


        tripList.size();
        int a = 1;
    }


}