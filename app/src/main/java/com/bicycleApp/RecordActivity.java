package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.util.Timer;

public class RecordActivity extends AppCompatActivity {

    private Button recordButton;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        chronometer = findViewById(R.id.chronometerR);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
//                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
//                    chronometer.setBase(SystemClock.elapsedRealtime());
//                    Toast.makeText(RecordActivity.this, "Bing!", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        recordButton = findViewById(R.id.button2);
        recordButton.setOnClickListener((view -> {
            startChronometer(view);
        }));
    }

    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;



    }
}