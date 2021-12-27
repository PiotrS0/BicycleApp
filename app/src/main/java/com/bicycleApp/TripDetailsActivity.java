package com.bicycleApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import Data.MyDatabase;

public class TripDetailsActivity extends AppCompatActivity {

    private Button button;
    private Button deleteButton;
    private TextView textView, weatherTextView;
    private CheckBox checkBox;
    private int id;
    private String date;
    private boolean notification;
    private MyDatabase database;
    DecimalFormat df = new DecimalFormat("#.##");
    private final String url = "https://api.openweathermap.org/data/2.5/";
    private final String appid = "cd7c73584d832f404cedc12f4d738e07";
    private Date dateFromBase;
    private ImageView imageView;

    @RequiresApi(api = Build.VERSION_CODES.O)
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
        weatherTextView = findViewById(R.id.textView3);
        id = getIntent().getIntExtra("Id",0);
        date = getIntent().getStringExtra("Date");
        notification = getIntent().getBooleanExtra("Notification",true);
        imageView = findViewById(R.id.imageView2);
        textView.setText(date.substring(0,date.length()-3));
        checkBox.setChecked(notification);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.changeNotification(id, checkBox.isChecked());
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dateFromBase = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            getWeatherDetails(checkData(dateFromBase));
        } catch (Exception e) {
            weatherTextView.setText(e.getMessage());
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private int checkData(Date dateBase){
        int days = 0;
        LocalDateTime timeNow = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        dateBase.setHours(timeNow.getHour());
        dateBase.setMinutes(timeNow.getMinute());
        dateBase.setSeconds(timeNow.getSecond());
        LocalDateTime time = dateBase.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        if(time.getDayOfMonth() == timeNow.getDayOfMonth() && time.getMonthValue() == timeNow.getMonthValue() && time.getYear() == timeNow.getYear())
            return days = -2;
        if(time.isEqual(timeNow.plusDays(1)))
            return days = 1;
        else if(time.isEqual(timeNow.plusDays(2)))
            return days = 2;
        else if(time.isEqual(timeNow.plusDays(3)))
            return days = 3;
        else if(time.isEqual(timeNow.plusDays(4)))
            return days = 4;
        else if(time.isEqual(timeNow.plusDays(5)))
            return days = 5;
        else if(time.isEqual(timeNow.plusDays(6)))
            return days = 6;
        else if(time.isEqual(timeNow.plusDays(7)))
            return days = 7;
        else if(timeNow.isAfter(time))
            return days = -1;
        else
            return days = 0;
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


    public void getWeatherDetails(int days){
        String tempUrl = "";
        String lat = "53.119559";
        String lon = "23.150423";

        if(days == 0){
            weatherTextView.setText("Prognoza pogody jest dostępna na 7 dni przed terminem wycieczki.");
            Glide.with(this).load(R.drawable.error).into(imageView);
        }

        else{
            if(days==-1){
                tempUrl = url + "weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + appid;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String output = "";
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                            String description = jsonObjectWeather.getString("description");
                            JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                            double temp = jsonObjectMain.getDouble("temp");
                            double feels = jsonObjectMain.getDouble("feels_like");
                            float pressure = jsonObjectMain.getInt("pressure");
                            JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                            String windSpeed = jsonObjectWind.getString("speed");
                            JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                            String clouds = jsonObjectClouds.getString("all");
                            long timeStamp = jsonResponse.getLong("dt");
                            String icon = jsonObjectWeather.getString("icon");
                            java.util.Date time=new java.util.Date((long)timeStamp*1000);
                            weatherTextView.setTextColor(Color.rgb(68,134,199));
                            output += "Aktualna pogoda: "
                                    + "\n Temp: " + df.format(temp) + " °C"
                                    + "\n Feels Like: " + df.format(feels) + " °C"
                                    + "\n Pressure: " + pressure
                                    + "\n Description: " + description
                                    + "\n Wind Speed: " + windSpeed + "m/s (meters per second)"
                                    + "\n Cloudiness: " + clouds + "%"
                                    + "\n TIME: " + time;
                            weatherTextView.setText(output);
                            Glide.with(TripDetailsActivity.this).load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        weatherTextView.setText("Brak połączenia z internetem");
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }

            else{
                int param;
                tempUrl = url + "onecall?lat=" + lat + "&lon=" + lon + "&exclude=current,minutely,hourly,alerts&units=metric&appid=" + appid;
                if(days == -2)
                    param = 0;
                else
                    param = days;
                StringRequest stringRequest = new StringRequest(Request.Method.GET, tempUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String output = "";
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray jsonArray = jsonResponse.getJSONArray("daily");
                            JSONObject jsonObject = jsonArray.getJSONObject(param);
                            JSONArray jsonWeatherArray = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonWeatherArray.getJSONObject(0);
                            JSONObject jsonTempObject = jsonObject.getJSONObject("temp");
                            JSONObject jsonFeelsLikeObject = jsonObject.getJSONObject("feels_like");
                            double temp = jsonTempObject.getDouble("day");
                            double feels = jsonFeelsLikeObject.getDouble("day");
                            int pressure = jsonObject.getInt("pressure");
                            double windSpeed = jsonObject.getDouble("wind_speed");
                            String icon = jsonObjectWeather.getString("icon");
                            String description = jsonObjectWeather.getString("description");
                            int clouds = jsonObject.getInt("clouds");
                            double pop = jsonObject.getDouble("pop");

                            long timeStamp = jsonObject.getLong("dt");
                            java.util.Date time=new java.util.Date((long)timeStamp*1000);

                            weatherTextView.setTextColor(Color.rgb(68,134,199));
                                output += "Przewidywana pogoda: "
                                    + "\n Temp: " + df.format(temp) + " °C"
                                    + "\n Feels Like: " + df.format(feels) + " °C"
                                    + "\n Pressure: " + pressure
                                    + "\n Description: " + description
                                    + "\n Wind Speed: " + windSpeed + "m/s (meters per second)"
                                    + "\n Cloudiness: " + clouds + "%"
                                    + "\n Probability of precipitation: " + pop + "%"
                                    + "\n TIME: " + time;
                            weatherTextView.setText(output);
                            Glide.with(TripDetailsActivity.this).load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        weatherTextView.setText("Błąd w połączeniu");
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }
    }

}