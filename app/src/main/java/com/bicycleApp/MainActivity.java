package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import Data.MyDatabase;

public class MainActivity extends AppCompatActivity {

    private Button buttonAdd;
    private Button buttonExit;
    private Button buttonList;
    private Button buttonInfo;
    private SensorManager sensorManager;
    private java.util.List<Sensor> sensorList;
    private Sensor sensorLight;
    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonAdd = (Button) findViewById(R.id.addbutton);
        buttonAdd.setOnClickListener((view -> {openAdd();}));

        buttonExit = (Button) findViewById(R.id.exitbutton);
        buttonExit.setOnClickListener((view -> {openExit();}));

        buttonList = findViewById(R.id.listbutton);
        buttonList.setOnClickListener((view -> {openList();}));

        buttonInfo = (Button) findViewById(R.id.infobutton);
        buttonInfo.setOnClickListener((view -> {openInfo();}));

        database = new MyDatabase(this, 1);
        Log.d("MAIN","BAZA1");
    }


    public void openAdd(){


        Intent intent = new Intent (this, AddTripActivity.class);
        startActivity(intent);

        Location location;

    }

    public void openList(){
        Cursor cursor = database.getAllTrips();
        while(cursor.moveToNext()){

        }
//        Intent intent = new Intent(this, List.class);
//        startActivity(intent);

    }

    public void openInfo(){
//        Intent intent = new Intent(this, Info.class);
//        startActivity(intent);
    }

    public void openExit(){
        finish();
    }
}