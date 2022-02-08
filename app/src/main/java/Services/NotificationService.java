package Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bicycleApp.R;
import com.bicycleApp.TripDetailsActivity;
import com.bicycleApp.TripDetailsFromNotificationActivity;
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
import java.util.LinkedList;
import java.util.List;

import Data.MyDatabase;
import Model.Trip;

public class NotificationService extends IntentService {

    private MyDatabase database;
    private Cursor cursor;
    private List<Trip> tripList = new LinkedList<>();
    private Date date;
    private Date date2;
    private Date dateFromBase;
    private final String url = "https://api.openweathermap.org/data/2.5/";
    private String apiId;
    private double lat, lon;
    private boolean isOnline = true;
    private DecimalFormat df = new DecimalFormat("#.##");
    private String weatherTemperature = "", weatherCloudines = "", weatherDescription = "";
    private NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
    private NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
    private NotificationCompat.Builder builder;


    public NotificationService(String name) {
        super(name);
    }

    public NotificationService() {
        super("NotificationService");
    }

    Handler handler = new Handler();

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {

                apiId = getResources().getString(R.string.openweather_api_key);




                database = new MyDatabase(getApplicationContext(), 1);
                cursor = database.getAllTrips();
                while(cursor.moveToNext()){
                    Trip trip = new Trip(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(4),cursor.getDouble(5),cursor.getDouble(6));
                    if(cursor.getInt(3) == 0)
                        trip.setNotification(false);
                    tripList.add(trip);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date nowDate = convertToDateViaInstant(LocalDateTime.now());
                String nowDateAfterFormat = sdf.format(nowDate);
                Date date3 = convertToDateViaInstant(LocalDateTime.now().plusDays(7));
                Trip nearestTrip = new Trip();

                for(Trip x : tripList){
                    try {
                        date = sdf.parse(x.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(date.before(date3) && x.getNotification() == true){
                        date3 = date;
                        nearestTrip = x;
                    }
                }
                lat = nearestTrip.getLat();
                lon = nearestTrip.getLon();

                try {
                    dateFromBase = sdf.parse(nearestTrip.getDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    getWeatherDetails(checkData(dateFromBase));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int cocheckuje = checkData(dateFromBase);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel = new NotificationChannel("Mynotification", "Mynotificaiton", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                }
                int flag = 0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    flag = PendingIntent.FLAG_MUTABLE;



                inboxStyle.addLine(nearestTrip.getDate());
                if(isOnline == true){
                    inboxStyle.addLine("Przewidywana pogoda");
                    inboxStyle.addLine(""+weatherTemperature);
                    inboxStyle.addLine(""+weatherCloudines);
                    inboxStyle.addLine(""+weatherDescription);
                }




                builder = new NotificationCompat.Builder(getApplicationContext(), "Mynotification")
                        .setContentTitle("Nadchodząca wycieczka")
                        .setContentText("testestse")
                        .setTicker("krotkie powiadomienie")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.error))
                        .setAutoCancel(true)
                        .setStyle(inboxStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(Notification.DEFAULT_SOUND);

                //NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                //managerCompat.notify(1, builder.build());




            }
        });


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

    public void getWeatherDetails(int days){
        String tempUrl = "";


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
                            JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                            String clouds = jsonObjectClouds.getString("all");
                            weatherTemperature += "Temp: " + df.format(temp) + " °C";
                            weatherCloudines += "Cloudiness: " + clouds + "%";
                            weatherDescription += "" + description;
                            inboxStyle.addLine(weatherCloudines);
                            isOnline = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isOnline = false;
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
                            double temp = jsonTempObject.getDouble("day");
                            String icon = jsonObjectWeather.getString("icon");
                            String description = jsonObjectWeather.getString("description");
                            int clouds = jsonObject.getInt("clouds");
                            weatherTemperature += "Temp: " + df.format(temp) + " °C";
                            weatherCloudines += "Cloudiness: " + clouds + "%";
                            weatherDescription += "" + description;
                            inboxStyle.addLine(weatherCloudines);
                            isOnline = true;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isOnline = false;
                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }
            managerCompat.notify(1, builder.build());
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
