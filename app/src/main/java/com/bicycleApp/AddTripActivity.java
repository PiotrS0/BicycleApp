package com.bicycleApp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import Data.MyDatabase;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AddTripActivity extends AppCompatActivity {

    private Button buttonAdd;
    private Button buttonCancel;
    private Calendar calendar1 = Calendar.getInstance();
    private static final String TAG = "MainActivity";
    private MyDatabase database;
    Button dateButton, timeButton;
    TextView dateTextView, timeTextView;
    private Date data = new Date();
    private Date data2 = new Date();
    private CheckBox checkBox;
    private EditText editText;
    public AddTripActivity() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        buttonAdd = findViewById(R.id.add_button);
        buttonAdd.setOnClickListener((view -> {
            try {
                openAdd();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
        buttonCancel = findViewById(R.id.cancel_button);
        buttonCancel.setOnClickListener((view -> {finish();}));
        checkBox = findViewById(R.id.checkBox);
        dateButton = findViewById(R.id.dateButton);
        timeButton = findViewById(R.id.timeButton);
        dateTextView = findViewById(R.id.dateTextView);
        timeTextView = findViewById(R.id.timeTextView);
        dateTextView.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeTextView.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        editText = findViewById(R.id.editTextCity);
        database = new MyDatabase(this, 1);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                handleDateButton();
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                handleTimeButton();
            }
        });
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
                dateTextView.setText(""+year +"-"+ (month+1) +"-"+ date);
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

        if(data2.before(nowDate))
            Toast.makeText(AddTripActivity.this,"Wprowadź poprawną datę", Toast.LENGTH_SHORT).show();
        else{
            database.addTrip(editText.getText().toString(), dateAfterFormat,checkBox.isChecked());

            Toast.makeText(AddTripActivity.this,"Wycieczka dodana", Toast.LENGTH_SHORT).show();

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