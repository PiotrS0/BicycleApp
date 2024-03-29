package com.bicycleApp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import Data.MyDatabase;
import Utils.Utilities;

public class TourDetailsActivity extends AppCompatActivity {

    private MyDatabase database;
    private MaterialToolbar toolbar;
    private double time, distance;
    private int id;
    private TextView titleText, timeText, distanceText, speedText;
    private String title, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_details);
        database = new MyDatabase(this, 1);
        id = this.getIntent().getIntExtra("Id",0);
        time = this.getIntent().getDoubleExtra("Time",0.0);
        distance = this.getIntent().getDoubleExtra("Distance", 0.0);
        toolbar = findViewById(R.id.topAppBarTourDetails);
        titleText = findViewById(R.id.text_tour_details_title);
        timeText = findViewById(R.id.text_tour_details_time);
        distanceText = findViewById(R.id.text_tour_details_distance);
        speedText = findViewById(R.id.text_tour_details_avg_speed);
        title = getIntent().getStringExtra("Title");
        date = getIntent().getStringExtra("Date");
        titleText.setText(title);
        timeText.setText(Utilities.getTimeStringFromDouble(time));
        distanceText.setText("" + Utilities.roundTo2DecimalPlace(distance) +" km");
        speedText.setText(""+Utilities.roundTo2DecimalPlace(distance/(time/3600))+" km/h");
        toolbar.setTitle(date.substring(0,date.length()-9));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("delete")){
                    try {
                        deleteItem();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(item.getTitle().equals("route"))
                    showRoute();
                if(item.getTitle().equals("edit"))
                    editItem();
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK)
            titleText.setText(data.getStringExtra("TitleResult"));
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showRoute(){
        Intent intent = new Intent(this, MapShowRouteActivity.class);
        intent.putExtra("TourId", id);
        intent.putExtra("StartLat", getIntent().getDoubleExtra("StartLat",0));
        intent.putExtra("StartLon", getIntent().getDoubleExtra("StartLon",0));
        intent.putExtra("EndLat", getIntent().getDoubleExtra("EndLat",0));
        intent.putExtra("EndLon", getIntent().getDoubleExtra("EndLon",0));
        startActivity(intent);
    }

    private void editItem(){
        Intent intent = new Intent(this, TourEditActivity.class);
        intent.putExtra("TourId", id);
        intent.putExtra("Time", time);
        intent.putExtra("Distance", distance);
        intent.putExtra("Title", title);
        intent.putExtra("Date", date);
        startActivityForResult(intent, 1);
    }

    private void deleteItem() throws InterruptedException {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.sureDeleteTour));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        database.deleteRow("Trip",id);
                        database.deletePoints(id);
                        Toast.makeText(TourDetailsActivity.this,getResources().getString(R.string.tourDeleted),Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}