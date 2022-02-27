package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.LinkedList;
import java.util.List;

import Adapters.HighlightAdapter;
import Data.MyDatabase;
import Model.Highlight;
import Utils.Utilities;

public class HighlightListActivity extends AppCompatActivity {

    private MyDatabase database;
    private List<Highlight> highlightList = new LinkedList<>();
    private GridView gridView;
    private Cursor cursor;
    private HighlightAdapter highlightAdapter;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_list);
        database = new MyDatabase(this, 1);
        gridView = findViewById(R.id.gridH);
        toolbar = findViewById(R.id.topAppBarHighlightsList);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                placeholder(i);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        highlightList.removeAll(highlightList);
        cursor = database.getAllHighlights();

        while(cursor.moveToNext()){
            Highlight highlight = new Highlight(cursor.getInt(0),cursor.getBlob(1),cursor.getString(2),cursor.getString(3), cursor.getString(4), cursor.getDouble(5),cursor.getDouble(6), cursor.getInt(7));
            highlightList.add(highlight);
        }
        highlightAdapter = new HighlightAdapter(this, highlightList);
        gridView.setAdapter(highlightAdapter);
    }

    private void placeholder(int position){
        Intent intent = new Intent(this, HighlightDetailsActivity.class);
        intent.putExtra("Id", highlightList.get(position).getId());
        Utilities.imageBetweenActivities = highlightList.get(position).getImage();
        intent.putExtra("Title", highlightList.get(position).getTitle());
        intent.putExtra("Description", highlightList.get(position).getDescription());
        intent.putExtra("Date", highlightList.get(position).getDate());
        intent.putExtra("Lat", highlightList.get(position).getLat());
        intent.putExtra("Lon", highlightList.get(position).getLon());
        startActivity(intent);
    }
}