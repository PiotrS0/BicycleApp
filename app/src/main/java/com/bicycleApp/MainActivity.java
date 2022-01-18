package com.bicycleApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import Data.MyDatabase;

public class MainActivity extends AppCompatActivity {

    private Button exitButton, listButton, infoButton, statsButton, highlightsButton, recordButton, toursButton;
    private MyDatabase database;
    private Button testButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener((view -> {
            startActivity(new Intent(this, HighlightAddActivity.class));
        }));

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
        exitButton = (Button) findViewById(R.id.exitbutton);
        exitButton.setOnClickListener((view -> {finish();}));
        infoButton = (Button) findViewById(R.id.infobutton);
        infoButton.setOnClickListener((view -> {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }));
        toursButton = findViewById(R.id.archivesbutton);
        toursButton.setOnClickListener((view -> {
            startActivity(new Intent(this, TourListActivity.class));
        }));
        statsButton = findViewById(R.id.btn_stats);
        statsButton.setOnClickListener((view -> {
            //startActivity(new Intent(this, StatsActivity.class));
            openStats();
        }));
        highlightsButton = findViewById(R.id.btn_highlights);
        highlightsButton.setOnClickListener((view -> {
            Intent intent = new Intent(this, HighlightListActivity.class);
            startActivity(intent);
        }));

        database = new MyDatabase(this, 1);
    }

    public void openStats(){
        Intent intent = new Intent(this, MapPointPickerActivity.class);
        startActivity(intent);
//        database.clearTable("Trip");
//        database.deleteRow("Trip",15);
    }

}