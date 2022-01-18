package com.bicycleApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.LinkedList;
import java.util.List;

import Data.MyDatabase;
import Model.Trip;

public class TripListActivity extends AppCompatActivity {

    private MyDatabase database;
    private List<Trip> tripList = new LinkedList<>();
    private Cursor cursor;
    private ListView list;
    private TripAdapter tripAdapter;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);
        list = (ListView) findViewById(R.id.trips_list);
        database = new MyDatabase(this,1);
        toolbar = findViewById(R.id.topAppBarTripList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                placeholder(i);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(getApplicationContext(), TripAddActivity.class));
                return true;
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        tripList.removeAll(tripList);
        cursor = database.getAllTrips();
        while(cursor.moveToNext()){
            Trip trip = new Trip(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            if(cursor.getInt(3) == 0)
                trip.setNotification(false);

            tripList.add(trip);
        }

        tripAdapter = new TripAdapter(this, tripList);
        list.setAdapter(tripAdapter);
    }

    private void placeholder(int position){
        Intent intent = new Intent(this, TripDetailsActivity.class);
        intent.putExtra("Id", tripList.get(position).getId());
        intent.putExtra("Date", tripList.get(position).getDate());
        intent.putExtra("Title", tripList.get(position).getTitle());
        intent.putExtra("Notification", tripList.get(position).getNotification());
        startActivity(intent);
    }

}