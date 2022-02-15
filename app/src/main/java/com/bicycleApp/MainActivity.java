package com.bicycleApp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import Data.MyDatabase;
import Services.NotificationService;

public class MainActivity extends AppCompatActivity {

    private Button listButton, infoButton, statsButton, highlightsButton, recordButton, toursButton;
    private MyDatabase database;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, NotificationService.class));

        listButton = findViewById(R.id.listbutton);
        listButton.setOnClickListener((view -> {
            Intent intent = new Intent(this, TripListActivity.class);
            startActivity(intent);
        }));
        recordButton = findViewById(R.id.btn_record);
        recordButton.setOnClickListener((view -> {
            Intent intent = new Intent(this,RecordActivity.class);
            startActivity(intent);
        }));
        infoButton = (Button) findViewById(R.id.infobutton);
        infoButton.setOnClickListener((view -> {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }));
        toursButton = findViewById(R.id.archivesbutton);
        toursButton.setOnClickListener((view -> {
            //database.addTour("Title1",12.34,12.34,56.78,56.78,6670,56.70,"2022-01-20 19:02:35");
            //database.addTour("Title2",12.34,12.34,56.78,56.78,3810,12.30,"2022-02-09 19:02:35");
            startActivity(new Intent(this, TourListActivity.class));
        }));
        statsButton = findViewById(R.id.btn_stats);
        statsButton.setOnClickListener((view -> {
            startActivity(new Intent(this, StatsActivity.class));
        }));
        highlightsButton = findViewById(R.id.btn_highlights);
        highlightsButton.setOnClickListener((view -> {
            Intent intent = new Intent(this, HighlightListActivity.class);
            startActivity(intent);
        }));

        database = new MyDatabase(this, 1);
    }

    public void openActivityTest(){
        Intent intent = new Intent(this, MapsShowPointActivity.class);
        startActivity(intent);
//        database.clearTable("Trip");
//        database.deleteRow("Trip",15);
    }

    private void makebasetour(){
        //database.clearTable("Tour");
        database.addTour("Title123",12.34,12.34,56.78,56.78,3670,56.70,"2022-01-20 19:02:35");
        database.addTour("Title1243",12.34,12.34,56.78,56.78,3810,12.30,"2022-02-09 19:02:35");
    }

}