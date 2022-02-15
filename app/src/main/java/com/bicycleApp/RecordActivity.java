package com.bicycleApp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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

public class RecordActivity extends AppCompatActivity {

    private Button recordButton, stopButton, highlightButton;
    private boolean timerStarted = false;
    private double time = 0;
    private Intent serviceIntent;
    private TextView textView;
    private MaterialToolbar toolbar;
    private static int REQUEST_LOCATION_PERMISSION = 123;
    private boolean locationAvaliable = false;
    private MyDatabase database;
    private long id;
    private double distance;

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
                    Toast.makeText(getApplicationContext(), "Lokalizacja niedostępna", Toast.LENGTH_LONG).show();
                else{
                    startStopTimer();
                }
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
                startActivity(new Intent(getApplicationContext(), HighlightAddActivity.class));
            }
        });

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            locationAvaliable = true;
        else
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);

        serviceIntent = new Intent(getApplicationContext(), TourRecordService.class);
        registerReceiver(updateTime, new IntentFilter(TourRecordService.TIMER_UPDATED));

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
        Intent intent = new Intent(this, RecordSaveActivity.class);
        intent.putExtra("TourId", id);
        intent.putExtra("Time", time);
        intent.putExtra("Distance", distance);
        time = 0.0;
        textView.setText(getTimeStringFromDouble(time));
        startActivity(intent);
        //binding.timeTV.text = getTimeStringFromDouble(time)
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
        if(timerStarted == false)
            id = database.addTourStart();
        serviceIntent.putExtra(TourRecordService.TIME_EXTRA, time);
        serviceIntent.putExtra("TourId", id);
        serviceIntent.putExtra("ISPAUSE", false);
        startService(serviceIntent);
        recordButton.setText("Pauza");
//        binding.startStopButton.text = "Stop"
//        binding.startStopButton.icon = getDrawable(R.drawable.ic_baseline_pause_24)
        timerStarted = true;
    }

    private void stopTimer()
    {
        serviceIntent.putExtra("ISPAUSE", true);
        stopService(serviceIntent);
        recordButton.setText("Start");
//        binding.startStopButton.text = "Start"
//        binding.startStopButton.icon = getDrawable(R.drawable.ic_baseline_play_arrow_24)
        timerStarted = false;
    }

    private BroadcastReceiver updateTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stopButton.setVisibility(View.VISIBLE);
            highlightButton.setVisibility(View.VISIBLE);
            time = intent.getDoubleExtra(TourRecordService.TIME_EXTRA, 0);
            distance = intent.getDoubleExtra("Distance", 0);
            textView.setText(getTimeStringFromDouble(time));
            if(time == 86400)
                stopService(serviceIntent);
            //setText = getTimeStringFromDouble(time);
        }

    };

    private String getTimeStringFromDouble(double time){
        int resultInt = (int) time;
        int hours = (int) resultInt % 86400 / 3600;
        int minutes = (int) resultInt % 86400 % 3600 / 60;
        int seconds = (int) resultInt % 86400 % 3600 % 60;

        return makeTimeString(hours, minutes, seconds);
    }

    private String makeTimeString(int hour, int min, int sec){
        return String.format("%02d:%02d:%02d", hour, min, sec);
    }
}