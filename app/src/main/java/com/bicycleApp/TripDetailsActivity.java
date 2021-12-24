package com.bicycleApp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import Data.MyDatabase;

public class TripDetailsActivity extends AppCompatActivity {

    private Button button;
    private Button deleteButton;
    private TextView textView;
    private CheckBox checkBox;
    private int id;
    private String date, city;
    private boolean notification;
    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        database = new MyDatabase(this, 1);
        button = findViewById(R.id.btn_back);
        button.setOnClickListener((view -> {finish();}));
        deleteButton = findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener((view -> {
            try {
                deleteItem();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        textView = findViewById(R.id.tripdetailstextdate);
        checkBox = findViewById(R.id.checkBox2);

        id = getIntent().getIntExtra("Id",0);
        date = getIntent().getStringExtra("Date");
        city = getIntent().getStringExtra("City");
        notification = getIntent().getBooleanExtra("Notification",true);

        textView.setText(date.substring(0,date.length()-3));
        checkBox.setChecked(notification);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.changeNotification(id, checkBox.isChecked());
            }
        });
    }

    private void deleteItem() throws InterruptedException {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Czy na pewno chcesz usunąć wycieczkę?");
                alertDialogBuilder.setPositiveButton("Tak",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                database.deleteRow("Trip",id);
                                Toast.makeText(TripDetailsActivity.this,"Usunięto wycieczkę",Toast.LENGTH_SHORT).show();
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