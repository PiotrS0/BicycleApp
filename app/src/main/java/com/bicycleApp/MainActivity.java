package com.bicycleApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import Data.MyDatabase;
import Services.NotificationService;

public class MainActivity extends AppCompatActivity {

    private Button tripsButton, infoButton, statsButton, highlightsButton, recordButton, toursButton;
    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, NotificationService.class));
        //makebasetour();
        tripsButton = findViewById(R.id.btn_trips);
        tripsButton.setOnClickListener((view -> {
            startActivity(new Intent(this, TripListActivity.class));
        }));
        recordButton = findViewById(R.id.btn_record);
        recordButton.setOnClickListener((view -> {
            startActivity(new Intent(this, RecordActivity.class));
        }));
        infoButton = (Button) findViewById(R.id.btn_info);
        infoButton.setOnClickListener((view -> {
            startActivity(new Intent(this, InfoActivity.class));
        }));
        toursButton = findViewById(R.id.btn_tours);
        toursButton.setOnClickListener((view -> {
            startActivity(new Intent(this, TourListActivity.class));
        }));
        statsButton = findViewById(R.id.btn_stats);
        statsButton.setOnClickListener((view -> {
            startActivity(new Intent(this, StatsActivity.class));
        }));
        highlightsButton = findViewById(R.id.btn_highlights);
        highlightsButton.setOnClickListener((view -> {
            startActivity(new Intent(this, HighlightListActivity.class));
        }));
    }

    private void makebasetour(){
        database = new MyDatabase(this, 1);
        database.clearTable("Highlight");
        //deleteRow("Highlight",2);
//        database.addTour("Title123",12.34,12.34,56.78,56.78,3670,56.70,"2022-01-20 19:02:35");
//        database.addTour("Title1243",12.34,12.34,56.78,56.78,3810,12.30,"2022-02-09 19:02:35");

//        Intent intent = new Intent(this, MapsShowPointActivity.class);
//        startActivity(intent);
//        database.clearTable("Trip");
//        database.deleteRow("Trip",15);
    }
}