package com.bicycleApp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import Data.MyDatabase;
import Utils.Utilities;


public class TripAddActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private Calendar calendar1 = Calendar.getInstance();
    private MyDatabase database;
    private Button locationButton;
    private TextView dateTextView, timeTextView;
    private Date data = new Date();
    private CheckBox checkBox;
    private EditText editText;
    private double lat, lon;
    private final static int MY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_add);
        toolbar = findViewById(R.id.topAppBarAddTrip);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    openAdd();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        checkBox = findViewById(R.id.checkBox);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });
        Calendar currentDate = Calendar.getInstance();
        Date date = currentDate.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateTextView.setText(dateFormat.format(date));
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeTextView.setText(timeFormat.format(date));
        //timeTextView.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        editText = findViewById(R.id.editTextTitle);
        database = new MyDatabase(this, 1);
        locationButton = findViewById(R.id.bnt_location_add_trip);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapPointPickerActivity.class);
                startActivityForResult(intent, MY_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == MY_REQUEST_CODE) {
                if (data != null){
                    lat = data.getDoubleExtra("latPicked",0);
                    lon = data.getDoubleExtra("lonPicked",0);
                }
            }
        }
    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month+1);
                calendar1.set(Calendar.DATE, date);
                data.setYear(year-1900);
                data.setMonth(month);
                data.setDate(date);
                String allDate = "" + year;
                allDate += month < 10 ? "-0" + (month+1) : "-" + (month+1);
                allDate += date < 10 ? "-0" + date : "-" + date;
                dateTextView.setText(allDate);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                data.setHours(hour);
                data.setMinutes(minute);
                if(minute<10)
                    timeTextView.setText(""+hour+":"+0+minute);
                else
                    timeTextView.setText(""+hour+":"+minute);
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
    }

    public void openAdd() throws InterruptedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateAfterFormat = sdf.format(data);
        Date nowDate = Utilities.convertToDate(java.util.Calendar.getInstance());
        String nowDateAfterFormat = sdf.format(nowDate);
        if(data.before(nowDate))
            Toast.makeText(TripAddActivity.this, getResources().getString(R.string.enterVaildDate), Toast.LENGTH_SHORT).show();
        else{
            database.addTrip(editText.getText().toString(), dateAfterFormat,checkBox.isChecked(),nowDateAfterFormat,lat,lon);
            Toast.makeText(TripAddActivity.this, getResources().getString(R.string.tripAdded), Toast.LENGTH_SHORT).show();
            Thread.sleep(500);
            finish();
        }
    }
}