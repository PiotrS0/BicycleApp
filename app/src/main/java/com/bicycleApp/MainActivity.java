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
}