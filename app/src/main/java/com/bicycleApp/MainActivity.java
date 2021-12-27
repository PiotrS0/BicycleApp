package com.bicycleApp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import Data.MyDatabase;

public class MainActivity extends AppCompatActivity {

    private Button addButton, exitButton, listButton, infoButton, statsButton, highlightsButton, recordButton;
    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button) findViewById(R.id.addbutton);
        addButton.setOnClickListener((view -> {
            Intent intent = new Intent (this, AddTripActivity.class);
            startActivity(intent);
        }));
        listButton = findViewById(R.id.listbutton);
        listButton.setOnClickListener((view -> {
            Intent intent = new Intent(this, TripsListActivity.class);
            startActivity(intent);
        }));
        recordButton = findViewById(R.id.btn_record);
        exitButton = (Button) findViewById(R.id.exitbutton);
        exitButton.setOnClickListener((view -> {finish();}));
        infoButton = (Button) findViewById(R.id.infobutton);
        infoButton.setOnClickListener((view -> {
            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);
        }));
        statsButton = findViewById(R.id.btn_stats);
        statsButton.setOnClickListener((view -> {
            openStats();
        }));
        highlightsButton = findViewById(R.id.btn_highlights);
        highlightsButton.setOnClickListener((view -> {
            Intent intent = new Intent(this, HighlightsActivity.class);
            startActivity(intent);
        }));

        database = new MyDatabase(this, 1);
        Log.d("MAIN","BAZA1");
    }

    public void openStats(){
//        Intent intent = new Intent(this, InfoActivity.class);
//        startActivity(intent);
//        database.clearTable("Trip");
//        database.deleteRow("Trip",15);
    }

}