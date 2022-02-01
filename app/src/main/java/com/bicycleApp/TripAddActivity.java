package com.bicycleApp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import Data.MyDatabase;

@RequiresApi(api = Build.VERSION_CODES.N)
public class TripAddActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private Calendar calendar1 = Calendar.getInstance();
    private static final String TAG = "MainActivity";
    private MyDatabase database;
    Button locationButton;
    TextView dateTextView, timeTextView;
    private Date data = new Date();
    private Date data2 = new Date();
    private CheckBox checkBox;
    private EditText editText;
    private double lat, lon;
    private final static int MY_REQUEST_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        dateTextView.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });
        timeTextView.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                data2.setYear(year-1900);
                data2.setMonth(month);
                data2.setDate(date);
                String allDate = "" + year;
                allDate += month < 10 ? "-0" + (month+1) : "-" + (month+1);
                allDate += date < 10 ? "-0" + date : "-" + date;
                dateTextView.setText(allDate);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Log.i(TAG, "onTimeSet: " + hour + minute);
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                data2.setHours(hour);
                data2.setMinutes(minute);
                if(minute<10)
                    timeTextView.setText(""+hour+":"+0+minute);
                else
                    timeTextView.setText(""+hour+":"+minute);

            }
        }, HOUR, MINUTE, true);

        timePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openAdd() throws InterruptedException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateAfterFormat = sdf.format(data2);

        Date nowDate = convertToDateViaInstant(LocalDateTime.now());
        String nowDateAfterFormat = sdf.format(nowDate);

        if(data2.before(nowDate))
            Toast.makeText(TripAddActivity.this,"Wprowadź poprawną datę", Toast.LENGTH_SHORT).show();
        else{
            database.addTrip(editText.getText().toString(), dateAfterFormat,checkBox.isChecked(),nowDateAfterFormat,lat,lon);

            Toast.makeText(TripAddActivity.this,"Wycieczka dodana", Toast.LENGTH_SHORT).show();

            Thread.sleep(1000);
            finish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}