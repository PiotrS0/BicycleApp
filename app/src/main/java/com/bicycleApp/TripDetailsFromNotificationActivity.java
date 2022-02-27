package com.bicycleApp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;
import java.util.Date;

import Utils.Utilities;

public class TripDetailsFromNotificationActivity extends AppCompatActivity {

    private TextView titleText, weatherTextView;
    private String date, title;
    private double lat, lon;
    private final String url = "https://api.openweathermap.org/data/2.5/";
    private String apiId, weatherCurrentName, weatherForecastName, weatherTemp, weatherFeels, weatherPressure, weatherDescription, weatherWind, weatherCloud, weatherPrecipation;;
    private Date dateFromBase;
    private ImageView imageView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_from_notification);
        apiId = getResources().getString(R.string.openweather_api_key);
        toolbar = findViewById(R.id.topAppBarTripDetailsFromNotification);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        weatherCurrentName = getResources().getString(R.string.weatherNameCurrent);
        weatherForecastName = getResources().getString(R.string.weatherNameForecast);
        weatherTemp = getResources().getString(R.string.weatherTemp);
        weatherFeels = getResources().getString(R.string.weatherFeels);
        weatherPressure = getResources().getString(R.string.weatherPressure);
        weatherDescription = getResources().getString(R.string.weatherDescription);
        weatherWind = getResources().getString(R.string.weatherWind);
        weatherCloud = getResources().getString(R.string.weatherCloud);
        weatherPrecipation = getResources().getString(R.string.weatherPrecipation);
        titleText = findViewById(R.id.text_trip_details_from_notification_title);
        weatherTextView = findViewById(R.id.trip_details_from_notification_text_weather);
        date = getIntent().getStringExtra("Date");
        title = getIntent().getStringExtra("Title");
        lat = getIntent().getDoubleExtra("Lat",0);
        lon = getIntent().getDoubleExtra("Lon",0);
        imageView = findViewById(R.id.trip_details_from_notification_imageview);
        toolbar.setTitle(date.substring(0,date.length()-3));
        titleText.setText(title);

        try {
            dateFromBase = Utilities.sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(lat == 0.0 && lon == 0.0)
            weatherTextView.setText(getResources().getString(R.string.weatherNoLocation));
        else{
            try {
                getWeatherDetails(checkData(dateFromBase));
            } catch (Exception e) {
                weatherTextView.setText(e.getMessage());
                e.printStackTrace();
            }
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
                            String icon = jsonObjectWeather.getString("icon");
                            weatherTextView.setTextColor(Color.rgb(68,134,199));
                            output += weatherCurrentName +
                                    "\n" + weatherTemp + ": " +  Utilities.df.format(temp) + " °C" +
                                    "\n" + weatherFeels + ": " + Utilities.df.format(feels) + " °C" +
                                    "\n" + weatherPressure + ": " + pressure + " hPa" +
                                    "\n" + weatherDescription + ": " + description +
                                    "\n" + weatherWind + ": " + windSpeed + " m/s" +
                                    "\n" + weatherCloud + ": " + clouds + " %";
                            weatherTextView.setText(output);
                            Glide.with(TripDetailsFromNotificationActivity.this).load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);
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
                            Glide.with(TripDetailsFromNotificationActivity.this).load("https://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);
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