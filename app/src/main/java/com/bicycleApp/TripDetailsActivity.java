package com.bicycleApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import Data.MyDatabase;
import Utils.Utilities;

public class TripDetailsActivity extends AppCompatActivity {

    private TextView textView, weatherTextView;
    private CheckBox checkBox;
    private int id;
    private String date, title;
    private boolean notification;
    private double lat, lon;
    private MyDatabase database;
    DecimalFormat df = new DecimalFormat("#.##");
    private final String url = "https://api.openweathermap.org/data/2.5/";
    private String apiId;
    private Date dateFromBase;
    private ImageView imageView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        apiId = getResources().getString(R.string.openweather_api_key);
        database = new MyDatabase(this, 1);
        toolbar = findViewById(R.id.topAppBarTripDetails);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(item.getTitle().equals("location")){
                    Intent intent = new Intent(getApplicationContext(), MapsShowPointActivity.class);
                    intent.putExtra("Lat", lat);
                    intent.putExtra("Lon", lon);
                    intent.putExtra("Title", "Start point");
                    startActivity(intent);
                }
                if(item.getTitle().equals("delete")){
                    try {
                        deleteItem();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }
        });
        textView = findViewById(R.id.trip_details_title);
        checkBox = findViewById(R.id.checkBox2);
        weatherTextView = findViewById(R.id.textView3);
        id = getIntent().getIntExtra("Id",0);
        date = getIntent().getStringExtra("Date");
        notification = getIntent().getBooleanExtra("Notification",true);
        title = getIntent().getStringExtra("Title");
        lat = getIntent().getDoubleExtra("Lat",0);
        lon = getIntent().getDoubleExtra("Lon",0);
        imageView = findViewById(R.id.imageView2);
        textView.setText(title);
        toolbar.setTitle(date.substring(0,date.length()-3));
        checkBox.setChecked(notification);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.changeTripNotification(id, checkBox.isChecked());
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

    private int checkData(Date dateBase){
        int days = 0;
        Calendar calendar = Calendar.getInstance();
        Date date = Utilities.convertToDate(calendar);
        dateBase.setHours(date.getHours());
        dateBase.setMinutes(date.getMinutes());
        dateBase.setSeconds(date.getSeconds());

        if(date.getDate() == dateBase.getDate() && date.getMonth() == dateBase.getMonth() && date.getYear() == dateBase.getYear())
            return days = -2;

        for(int i = 0;i < 7;i++){
            calendar.add(Calendar.DATE, 1);
            date = Utilities.convertToDate(calendar);
            if(date.getDate() == dateBase.getDate() && date.getMonth() == dateBase.getMonth() && date.getYear() == dateBase.getYear())
                return days = i+1;
        }
        if(date.after(dateBase))
            return days = -1;

        return days;
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

        if(days == 0){
            weatherTextView.setText("Prognoza pogody jest dostępna na 7 dni przed terminem wycieczki.");
            Glide.with(this).load(R.mipmap.empty).into(imageView);
        }

        else{
            if(days==-1){
                tempUrl = url + "weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + apiId;
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
                tempUrl = url + "onecall?lat=" + lat + "&lon=" + lon + "&exclude=current,minutely,hourly,alerts&units=metric&appid=" + apiId;
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