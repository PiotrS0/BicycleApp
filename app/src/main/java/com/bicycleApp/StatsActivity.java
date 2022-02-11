package com.bicycleApp;

import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import Data.MyDatabase;
import Model.Tour;

public class StatsActivity extends AppCompatActivity {

    private MyDatabase database;
    private MaterialToolbar toolbar;
    private Spinner spinner;
    private Button rangeButton;
    private Date startDate, endDate, tempDateFrom, tempDateTo;
    private Cursor cursor;
    private List<Tour> toursList = new ArrayList<Tour>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        database = new MyDatabase(this, 1);
        cursor = database.getAllTours();
        while(cursor.moveToNext()){
            Tour tour = new Tour(cursor.getInt(0),cursor.getString(1),cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getDouble(7), cursor.getString(8));
            toursList.add(tour);
        }
        toolbar = findViewById(R.id.topAppBarStats);
        spinner = findViewById(R.id.statsSpinner);
        rangeButton = findViewById(R.id.btn_stats_range);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        rangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDateRange();
            }
        });
        LocalDateTime currentDate = LocalDateTime.now();
        tempDateTo = convertToDateViaInstant(currentDate);
        spinner.getSelectedItemId();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0)
                    tempDateFrom = convertToDateViaInstant(currentDate.minusDays(3));
                if(i == 1)
                    tempDateFrom = convertToDateViaInstant(currentDate.minusDays(7));
                if(i == 2)
                    tempDateFrom = convertToDateViaInstant(currentDate.minusDays(14));
                if(i == 3)
                    tempDateFrom = convertToDateViaInstant(currentDate.minusDays(30));
                if(i == 4)
                    tempDateFrom = convertToDateViaInstant(currentDate.minusMonths(3));
                if(i == 5)
                    tempDateFrom = convertToDateViaInstant(currentDate.minusMonths(6));
                if(i == 6)
                    tempDateFrom = convertToDateViaInstant(currentDate.minusYears(1));

                Log.d("Karolina", ""+i);
                displayParameters(tempDateFrom, tempDateTo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void handleDateRange() {
        MaterialDatePicker.Builder builderRange = MaterialDatePicker.Builder.dateRangePicker();
        builderRange.setCalendarConstraints(limitRange().build());
        MaterialDatePicker<Pair<Long, Long>> pickerRange = builderRange.build();
        pickerRange.show(getSupportFragmentManager(), pickerRange.toString());
        pickerRange.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                Pair<Long, Long> pair = selection;
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                calendar.setTimeInMillis(pair.first);
                startDate = calendar.getTime();
                calendar.setTimeInMillis(pair.second);
                endDate = calendar.getTime();
                displayParameters(startDate, endDate);
            }
        });
    }

    private CalendarConstraints.Builder limitRange(){
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();

        Calendar calendarStart = GregorianCalendar.getInstance();
        Calendar calendarEnd = GregorianCalendar.getInstance();

        calendarStart.set(2020, 0, 1);
        calendarEnd.set(2040, 11, 31);

        long minDate = calendarStart.getTimeInMillis();
        long maxDate = calendarEnd.getTimeInMillis();

        constraintsBuilderRange.setStart(minDate);
        constraintsBuilderRange.setEnd(maxDate);

        return constraintsBuilderRange;
    }

    private void displayParameters(Date dateFrom, Date dateTo){
        List<Tour> list = new ArrayList<Tour>();
        for(Tour tour : toursList){
            try {
                Date d = sdf.parse(tour.getDate());
                if(d.after(dateFrom) && d.before(dateTo))
                    list.add(tour);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for(Tour t : list){

        }





    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}