package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Timer;

public class TourRegistrationActivity extends AppCompatActivity {

    private Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_registration);

    }
}