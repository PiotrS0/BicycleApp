package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.google.android.material.appbar.MaterialToolbar;

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
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_list);
        gridView = findViewById(R.id.gridH);
        toolbar = findViewById(R.id.topAppBarHighlightsList);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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