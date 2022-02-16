package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import Data.MyDatabase;
import Utils.Utilities;

public class HighlightDetailsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MyDatabase database;
    private int id, tourId;
    private String date, title, description;
    private double lat, lon;
    private TextView titleView, descriptionView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight_details);
        database = new MyDatabase(this, 1);
        toolbar = findViewById(R.id.topAppBarhighlightDetails);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        id = getIntent().getIntExtra("Id", 0);
        tourId = getIntent().getIntExtra("TourId", 0);
        date = getIntent().getStringExtra("Date");
        title = getIntent().getStringExtra("Title");
        description = getIntent().getStringExtra("Description");
        lat = getIntent().getDoubleExtra("Lat",0);
        lon = getIntent().getDoubleExtra("Lon", 0);

        toolbar.setTitle(date);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle().equals("location")){
                    Intent intent = new Intent(getApplicationContext(), MapsShowPointActivity.class);
                    intent.putExtra("Lat", lat);
                    intent.putExtra("Lon", lon);
                    intent.putExtra("Title", title);
                    startActivity(intent);
                }
                if(item.getTitle().equals("delete")){
                    deleteHighlight();
                }
                return false;
            }
        });

        titleView = findViewById(R.id.highlight_details_title_view);
        descriptionView = findViewById(R.id.highlight_details_description_view);
        imageView = findViewById(R.id.highlight_details_image_view);
        titleView.setText(title);
        descriptionView.setText(description);
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(Utilities.imageBetweenActivities, 0, Utilities.imageBetweenActivities.length));
    }


    private void deleteHighlight(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Czy na pewno chcesz usunąć to wspomnienie?");
        alertDialogBuilder.setPositiveButton("Tak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        database.deleteRow("Highlight",id);
                        Toast.makeText(HighlightDetailsActivity.this,"Usunięto wspomnienie",Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("Nie",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}