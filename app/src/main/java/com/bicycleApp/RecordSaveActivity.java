package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;

import Data.MyDatabase;
import Utils.Utilities;

public class RecordSaveActivity extends AppCompatActivity {

    private long id;
    private MyDatabase database;
    private MaterialToolbar toolbar;
    private double time, distance;
    private TextView distanceText, timeText, speedText;
    private EditText titleText;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_save);
        id = this.getIntent().getLongExtra("TourId", 0);
        time = this.getIntent().getDoubleExtra("Time", 0);
        distance = this.getIntent().getDoubleExtra("Distance", 0);
        database = new MyDatabase(this, 1);
        distanceText = findViewById(R.id.text_record_save_distance);
        timeText = findViewById(R.id.text_record_save_time);
        speedText = findViewById(R.id.text_record_save_avg_speed);
        titleText = findViewById(R.id.text_record_save_title);

        toolbar = findViewById(R.id.topAppBarRecordSave);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    deleteItem();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                saveItem();
                return false;
            }
        });
        distanceText.setText(""+ Utilities.roundTo2DecimalPlace(distance) + " km");
    }

    private void saveItem(){
        title = titleText.getText().toString();
        if(title.equals(""))
            title = "Tour";
        database.saveTour((int)id, title);
        finish();
    }

    private void deleteItem() throws InterruptedException {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Czy na pewno chcesz usunąć wycieczkę?");
        alertDialogBuilder.setPositiveButton("Tak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        database.deleteRow("Tour", (int) id);
                        database.deletePoints((int) id);
                        Toast.makeText(RecordSaveActivity.this,"Usunięto wycieczkę",Toast.LENGTH_SHORT).show();
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