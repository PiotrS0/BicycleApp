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
    private Button testButton, baseButton;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



//        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Log.d("DSANDNSADNSANDSNADNSA","DSADSADSADSADSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
//        manager.notify(1, builder.build());

        startService(new Intent(this, NotificationService.class));

        testButton = findViewById(R.id.testButton);
        testButton.setOnClickListener((view -> {
            startActivity(new Intent(this, HighlightAddActivity.class));
        }));

        baseButton = findViewById(R.id.baseButton);
        baseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makebasetour();
            }
        });

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
        Intent intent = new Intent(this, MapsShowPointActivity.class);
        startActivity(intent);
//        database.clearTable("Trip");
//        database.deleteRow("Trip",15);
    }

    private void makebasetour(){
        //database.clearTable("Tour");
        database.addTour("Title123",12.34,12.34,56.78,56.78,1234,56.70,"2022-01-20 19:02:35");
    }

}