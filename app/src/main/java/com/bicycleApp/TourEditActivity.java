package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import Data.MyDatabase;
import Utils.Utilities;

public class TourEditActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView distanceText, timeText, speedText;
    private EditText titleText;
    private double time, distance;
    private String title;
    private int id;
    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_edit);
        toolbar = findViewById(R.id.topAppBarTourEdit);
        distanceText = findViewById(R.id.text_tour_edit_distance);
        timeText = findViewById(R.id.text_tour_edit_time);
        speedText = findViewById(R.id.text_tour_edit_avg_speed);
        titleText = findViewById(R.id.text_tour_edit_title);
        id = getIntent().getIntExtra("TourId", 0);
        title = getIntent().getStringExtra("Title");
        time = getIntent().getDoubleExtra("Time", 0);
        distance = getIntent().getDoubleExtra("Distance", 0);
        titleText.setText(title);
        int hours = (int) time % 86400 / 3600;
        int minutes = (int) time % 86400 % 3600 / 60;
        int seconds = (int) time % 86400 % 3600 % 60;
        timeText.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
        distanceText.setText(""+ Utilities.roundTo2DecimalPlace(distance)+" km");
        speedText.setText(""+Utilities.roundTo2DecimalPlace(distance/(time/3600))+" km/h");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                saveChanges();
                return false;
            }
        });
    }

    private void saveChanges(){
        database = new MyDatabase(this, 1);
        title = titleText.getText().toString();
        if(title.equals(""))
            title = "Tour";
        database.saveTour(id, title);
        database.close();

        Intent intent = new Intent(this, TourDetailsActivity.class);
        intent.putExtra("TitleResult", title);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}