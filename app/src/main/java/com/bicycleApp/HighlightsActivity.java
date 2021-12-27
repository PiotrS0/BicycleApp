package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toolbar;

import java.util.LinkedList;
import java.util.List;

import Data.MyDatabase;
import Model.HighLight;

public class HighlightsActivity extends AppCompatActivity {

    private MyDatabase database;
    private List<HighLight> highLightList = new LinkedList<>();
    private GridView gridView;
    private Cursor cursor;
    private HighlightAdapter highlightAdapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlights);
        gridView = findViewById(R.id.gridH);
        //button = findViewById(R.id.button3Add);
//        button.setOnClickListener(view -> {
//            Intent intent = new Intent(this, AddHighlightActivity.class);
//            startActivity(intent);
//        });
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.error);

        HighLight highLight = new HighLight(bitmap,"SIEMA");
        highLightList.add(highLight);
        highLightList.add(highLight);
        highLightList.add(highLight);
        highLightList.add(highLight);
        highLightList.add(highLight);
        highLightList.add(highLight);
        highLightList.add(highLight);
        highLightList.add(highLight);
        highLightList.add(highLight);
        highLightList.add(highLight);
        highLightList.add(highLight);
        highLightList.add(highLight);

        highlightAdapter = new HighlightAdapter(this,highLightList);
        gridView.setAdapter(highlightAdapter);
        setTitle("Highlights");
    }
}