package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.bicycleApp.databinding.ActivityMainBinding;

import java.util.Timer;

import Services.TourRecordService;

public class RecordActivity extends AppCompatActivity {

    private Button recordButton;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    private boolean timerStarted = false;
    private double time = 0;
    private Intent serviceIntent;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);


///
        ///
        ///
        recordButton = findViewById(R.id.RecordButton);
        textView = findViewById(R.id.textRecord);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startStopTimer();
            }
        });
        serviceIntent = new Intent(getApplicationContext(), TourRecordService.class);
        registerReceiver(updateTime, new IntentFilter(TourRecordService.TIMER_UPDATED));

    }

    private void resetTimer()
    {
        stopTimer();
        time = 0.0;
        textView.setText(getTimeStringFromDouble(time));
        //binding.timeTV.text = getTimeStringFromDouble(time)
    }

    private void startStopTimer()
    {
        if (timerStarted) {
            stopTimer();
        } else {
            startTimer();
        }
    }

    private void startTimer()
    {
        serviceIntent.putExtra(TourRecordService.TIME_EXTRA, time);
        startService(serviceIntent);
        recordButton.setText("stop");
//        binding.startStopButton.text = "Stop"
//        binding.startStopButton.icon = getDrawable(R.drawable.ic_baseline_pause_24)
        timerStarted = true;
    }

    private void stopTimer()
    {
        stopService(serviceIntent);
        recordButton.setText("Start");
//        binding.startStopButton.text = "Start"
//        binding.startStopButton.icon = getDrawable(R.drawable.ic_baseline_play_arrow_24)
        timerStarted = false;
    }

    private BroadcastReceiver updateTime = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            time = intent.getDoubleExtra(TourRecordService.TIME_EXTRA, 0);
            textView.setText(getTimeStringFromDouble(time));
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