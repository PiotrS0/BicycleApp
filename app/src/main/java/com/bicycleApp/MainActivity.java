package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import Data.MyDatabase;

public class MainActivity extends AppCompatActivity {

    private Button addButton, exitButton, listButton, infoButton, statsButton, highlightsButton;
    private SensorManager sensorManager;
    private java.util.List<Sensor> sensorList;
    private Sensor sensorLight;
    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.addbutton);
        addButton.setOnClickListener((view -> {openAdd();}));

        exitButton = (Button) findViewById(R.id.exitbutton);
        exitButton.setOnClickListener((view -> {openExit();}));

        listButton = findViewById(R.id.listbutton);
        listButton.setOnClickListener((view -> {openList();}));

        infoButton = (Button) findViewById(R.id.infobutton);
        infoButton.setOnClickListener((view -> {openInfo();}));

        statsButton = findViewById(R.id.btn_stats);
        statsButton.setOnClickListener((view -> {openStats();}));

        highlightsButton = findViewById(R.id.btn_highlights);
        highlightsButton.setOnClickListener((view -> {openHighlights();}));

        database = new MyDatabase(this, 1);
        Log.d("MAIN","BAZA1");
    }


    public void openAdd(){
        Intent intent = new Intent (this, AddTripActivity.class);
        startActivity(intent);
    }

    public void openList(){
        Intent intent = new Intent(this, TripsListActivity.class);
        startActivity(intent);

    }

    public void openInfo(){
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    public void openStats(){
//        Intent intent = new Intent(this, InfoActivity.class);
//        startActivity(intent);
//        database.clearTable("Trip");
//        database.deleteRow("Trip",15);
    }

    public void openHighlights(){
        Intent intent = new Intent(this, HighlightsActivity.class);
        startActivity(intent);
    }

    public void openExit(){
        finish();
    }
}