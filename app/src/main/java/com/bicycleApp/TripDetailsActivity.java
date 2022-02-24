package com.bicycleApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
    private boolean notification;
    private double lat, lon;
    private MyDatabase database;
    private final String url = "https://api.openweathermap.org/data/2.5/";
    private String apiId, date, title, weatherCurrentName, weatherForecastName, weatherTemp, weatherFeels, weatherPressure, weatherDescription, weatherWind, weatherCloud, weatherPrecipation;
    private Date dateFromBase;
    private ImageView imageView;
    private MaterialToolbar toolbar;
    private Button startRecordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        apiId = getResources().getString(R.string.openweather_api_key);
        weatherCurrentName = getResources().getString(R.string.weatherNameCurrent);
        weatherForecastName = getResources().getString(R.string.weatherNameForecast);
        weatherTemp = getResources().getString(R.string.weatherTemp);
        weatherFeels = getResources().getString(R.string.weatherFeels);
        weatherPressure = getResources().getString(R.string.weatherPressure);
        weatherDescription = getResources().getString(R.string.weatherDescription);
        weatherWind = getResources().getString(R.string.weatherWind);
        weatherCloud = getResources().getString(R.string.weatherCloud);
        weatherPrecipation = getResources().getString(R.string.weatherPrecipation);
        database = new MyDatabase(this, 1);
        toolbar = findViewById(R.id.topAppBarTripDetails);
        startRecordButton = findViewById(R.id.trip_details_start_record_button);
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
        checkBox = findViewById(R.id.trip_details_checkbox);
        weatherTextView = findViewById(R.id.trip_details_text_weather);
        id = getIntent().getIntExtra("Id",0);
        date = getIntent().getStringExtra("Date");
        notification = getIntent().getBooleanExtra("Notification",true);
        title = getIntent().getStringExtra("Title");
        lat = getIntent().getDoubleExtra("Lat",0);
        lon = getIntent().getDoubleExtra("Lon",0);
        imageView = findViewById(R.id.trip_details_imageview);
        textView.setText(title);
        toolbar.setTitle(date.substring(0,date.length()-3));
        checkBox.setChecked(notification);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.changeTripNotification(id, checkBox.isChecked());
            }
        });

        try {
            dateFromBase = Utilities.sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            if(lat == 0.0 && lon == 0.0)
                weatherTextView.setText(getResources().getString(R.string.weatherNoLocation));
            else
                getWeatherDetails(checkData(dateFromBase));
        } catch (Exception e) {
            weatherTextView.setText(e.getMessage());
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 3);
        Date dateCheck = Utilities.convertToDate(calendar);
        if(dateFromBase.before(dateCheck))
            startRecordButton.setVisibility(View.VISIBLE);

        startRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRecord();
            }
        });
    }

    private void startRecord(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(getResources().getString(R.string.sureRecordFromDetails));
        alert.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent newIntent = new Intent(getApplicationContext(), RecordActivity.class);
                newIntent.putExtra("SharedId", id);
                newIntent.putExtra("Title", title);
                database.updateTripIsPlanned(id, false);
                //database.deleteRow("Trip",id);
                finish();
                startActivity(newIntent);
            }
        });
        alert.setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alert.setNegativeButton(getResources().getString(R.string.onlyDelete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.deleteRow("Trip",id);
                Toast.makeText(TripDetailsActivity.this,getResources().getString(R.string.tripDeleted),Toast.LENGTH_SHORT).show();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private int checkData(Date dateBase){
        int days = 0;
        Calendar calendar = Calendar.getInstance();
        Date date = Utilities.convertToDate(calendar);
        if(date.after(dateBase))
            return days = -1;
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
        return days;
    }

    private void deleteItem() throws InterruptedException {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.sureDeleteTrip));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        database.deleteRow("Trip",id);
                        Toast.makeText(TripDetailsActivity.this,getResources().getString(R.string.tripDeleted),Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });

        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.no),new DialogInterface.OnClickListener() {
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
            weatherTextView.setText(getResources().getString(R.string.weatherAvaliableSevenDays));
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
                            output += weatherCurrentName +
                                "\n" + weatherTemp + ": " +  Utilities.df.format(temp) + " °C" +
                                "\n" + weatherFeels + ": " + Utilities.df.format(feels) + " °C" +
                                "\n" + weatherPressure + ": " + pressure + " hPa" +
                                "\n" + weatherDescription + ": " + description +
                                "\n" + weatherWind + ": " + windSpeed + " m/s" +
                                "\n" + weatherCloud + ": " + clouds + " %";
                            weatherTextView.setText(output);
                            Glide.with(TripDetailsActivity.this).load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        weatherTextView.setText(getResources().getString(R.string.noInternet));
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
                            weatherTextView.setTextColor(Color.rgb(68,134,199));
                            output += weatherForecastName +
                                    "\n" + weatherTemp + ": " +  Utilities.df.format(temp) + " °C" +
                                    "\n" + weatherFeels + ": " + Utilities.df.format(feels) + " °C" +
                                    "\n" + weatherPressure + ": " + pressure + " hPa" +
                                    "\n" + weatherDescription + ": " + description +
                                    "\n" + weatherWind + ": " + windSpeed + " m/s" +
                                    "\n" + weatherCloud + ": " + clouds + " %" +
                                    "\n" + weatherPrecipation + ": " + pop + " %";
                            weatherTextView.setText(output);
                            Glide.with(TripDetailsActivity.this).load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        weatherTextView.setText(getResources().getString(R.string.connectionError));
                        Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
        }
    }
}