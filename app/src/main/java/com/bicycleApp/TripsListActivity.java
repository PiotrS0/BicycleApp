package com.bicycleApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

import Data.MyDatabase;
import Model.Trip;

public class TripsListActivity extends AppCompatActivity {

    private MyDatabase database;
    private List<Trip> tripList = new LinkedList<>();
    private Button button;
    private Cursor cursor;
    private ListView list;
    private TripAdapter tripAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);
        list = (ListView) findViewById(R.id.trips_list);
        button = findViewById(R.id.button);
        button.setOnClickListener((view -> {finish();}));
        database = new MyDatabase(this,1);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                placeholder(i);
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
        intent.putExtra("City", tripList.get(position).getCity());
        intent.putExtra("Notification", tripList.get(position).getNotification());
        startActivity(intent);
    }

}