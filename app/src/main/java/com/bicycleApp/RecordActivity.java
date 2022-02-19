package com.bicycleApp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import Data.MyDatabase;
import Services.TourRecordService;
import Utils.Utilities;

public class RecordActivity extends AppCompatActivity {

    private Button recordButton, stopButton, highlightButton;
    private boolean timerStarted = false;
    private double time = 0;
    private Intent serviceIntent;
    private TextView textView;
    private MaterialToolbar toolbar;
    private static int REQUEST_LOCATION_PERMISSION = 123;
    private boolean locationAvaliable = false, firstActivate = false;
    private MyDatabase database;
    private long id;
    private double distance, lat, lon, tripTime;
    private int sharedId;
    private String title;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        database = new MyDatabase(this, 1);
        toolbar = findViewById(R.id.topAppBarRecord);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sharedId = getIntent().getIntExtra("SharedId",0);
        title = getIntent().getStringExtra("Title");
        recordButton = findViewById(R.id.RecordButton);
        stopButton = findViewById(R.id.btn_record_stop);
        stopButton.setVisibility(View.INVISIBLE);
        highlightButton = findViewById(R.id.btn_record_highlight);
        highlightButton.setVisibility(View.INVISIBLE);
        textView = findViewById(R.id.textRecord);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(locationAvaliable == false)
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.localizationUnavaliable), Toast.LENGTH_LONG).show();
                else
                    startStopTimer();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
        highlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent highlightIntent = new Intent(getApplicationContext(), HighlightAddActivity.class);
                highlightIntent.putExtra("TourId", (int)id);
                highlightIntent.putExtra("Lat", lat);
                highlightIntent.putExtra("Lon", lon);
                startActivity(highlightIntent);
            }
        });

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationAvaliable = true;
        else
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

        serviceIntent = new Intent(getApplicationContext(), TourRecordService.class);
        registerReceiver(updateTime, new IntentFilter(TourRecordService.TIMER_UPDATED));

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        if(!gps_enabled) {
            new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.gpsUnavaliable))
                    .setPositiveButton(getResources().getString(R.string.openSettings), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            settings();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel),null)
                    .show();
        }
    }

    private void settings(){
        this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if((requestCode == REQUEST_LOCATION_PERMISSION) && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
            locationAvaliable = true;
        else
            locationAvaliable = false;

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void resetTimer()
    {
        stopTimer();
        tripTime = time;
        time = 0.0;
        textView.setText(Utilities.getTimeStringFromDouble(time));
        if(distance == 0.0){
            database.deleteRow("Trip", (int) id);
            database.deletePoints((int) id);
            Toast.makeText(this, getResources().getString(R.string.tourTooShort), Toast.LENGTH_LONG).show();
            finish();
        }
        else{
            Intent intent = new Intent(this, RecordSaveActivity.class);
            intent.putExtra("TourId", id);
            intent.putExtra("Time", tripTime);
            intent.putExtra("Distance", distance);
            intent.putExtra("Title", title);
            startActivity(intent);
        }

    }

    private void startStopTimer()
    {
        if (timerStarted)
            stopTimer();
        else
            startTimer();
    }

    private void startTimer()
    {
        if(firstActivate == false){
            if(sharedId == 0)
                id = database.addTourStart();
            else{
                database.updateTourStart(sharedId, title);
                id = sharedId;
            }

        }
        firstActivate = true;
        serviceIntent.putExtra(TourRecordService.TIME_EXTRA, time);
        serviceIntent.putExtra("TourId", id);
        startService(serviceIntent);
        timerStarted = true;
    }

    private void stopTimer()
    {
        stopService(serviceIntent);
        timerStarted = false;
    }

    private BroadcastReceiver updateTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopButton.setVisibility(View.VISIBLE);
            highlightButton.setVisibility(View.VISIBLE);
            time = intent.getDoubleExtra(TourRecordService.TIME_EXTRA, 0);
            distance = intent.getDoubleExtra("Distance", 0);
            lat = intent.getDoubleExtra("Lat",0);
            lon = intent.getDoubleExtra("Lon",0);
            tripTime = intent.getDoubleExtra("TripTime", 0.0);
            textView.setText(Utilities.getTimeStringFromDouble(time));
            if(time == 86400)
                stopService(serviceIntent);
        }

    };
}