package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;

import java.util.LinkedList;
import java.util.List;

import Data.MyDatabase;
import Model.Highlight;

public class HighlightListActivity extends AppCompatActivity {

    private MyDatabase database;
    private List<Highlight> highlightList = new LinkedList<>();
    private GridView gridView;
    private Cursor cursor;
    private HighlightAdapter highlightAdapter;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_list);
        gridView = findViewById(R.id.gridH);
        //button = findViewById(R.id.button3Add);
//        button.setOnClickListener(view -> {
//            Intent intent = new Intent(this, AddHighlightActivity.class);
//            startActivity(intent);
//        });
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.error);

        Highlight highLight = new Highlight(bitmap,"SIEMA");
        highlightList.add(highLight);
        highlightList.add(highLight);
        highlightList.add(highLight);
        highlightList.add(highLight);
        highlightList.add(highLight);
        highlightList.add(highLight);
        highlightList.add(highLight);
        highlightList.add(highLight);
        highlightList.add(highLight);
        highlightList.add(highLight);
        highlightList.add(highLight);
        highlightList.add(highLight);

        highlightAdapter = new HighlightAdapter(this, highlightList);
        gridView.setAdapter(highlightAdapter);
        setTitle("Highlights");
    }
}