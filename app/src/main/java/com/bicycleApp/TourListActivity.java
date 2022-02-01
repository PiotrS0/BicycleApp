package com.bicycleApp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.LinkedList;
import java.util.List;

import Data.MyDatabase;
import Model.Tour;
import Adapters.*;

public class TourListActivity extends AppCompatActivity {

    private MyDatabase database;
    private List<Tour> toursList = new LinkedList<>();
    private Cursor cursor;
    private ListView list;
    private TourAdapter tourAdapter;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);
        list = (ListView) findViewById(R.id.tours_list);
        database = new MyDatabase(this,1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                placeholder(i);
            }
        });
        toolbar = findViewById(R.id.topAppBarTourList);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        toursList.removeAll(toursList);
        cursor = database.getAllTours();
        while(cursor.moveToNext()){
            Tour tour = new Tour(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7), cursor.getString(8));
            toursList.add(tour);
        }

        tourAdapter = new TourAdapter(this, toursList);
        list.setAdapter(tourAdapter);
    }

    private void placeholder(int position){
        Intent intent = new Intent(this, TourDetailsActivity.class);
        intent.putExtra("Id", toursList.get(position).getId());
        intent.putExtra("Date", toursList.get(position).getDate());
        intent.putExtra("Title", toursList.get(position).getTitle());
        intent.putExtra("Distance", toursList.get(position).getDistance());
        intent.putExtra("Time", toursList.get(position).getTime());
        intent.putExtra("StartLat", toursList.get(position).getStartLat());
        intent.putExtra("StartLon", toursList.get(position).getStartLon());
        intent.putExtra("EndLat", toursList.get(position).getEndLat());
        intent.putExtra("EndLon", toursList.get(position).getEndLon());
        startActivity(intent);
    }
}