package Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bicycleApp.R;
import com.bicycleApp.TripDetailsFromNotificationActivity;

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
                    Trip trip = new Trip(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(4),cursor.getDouble(5));
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
                int cocheckuje = checkData(dateFromBase);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel = new NotificationChannel("Mynotification", "Mynotificaiton", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                }
                int flag = 0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    flag = PendingIntent.FLAG_MUTABLE;

                Intent intent = new Intent(getApplicationContext(), TripDetailsFromNotificationActivity.class);
                intent.putExtra("Id", nearestTrip.getId());
                intent.putExtra("Date", nearestTrip.getDate());
                //intent.putExtra("Title", tripList.get(position).getTitle());
                intent.putExtra("Notification", nearestTrip.getNotification());
                intent.putExtra("Lat", nearestTrip.getLat());
                intent.putExtra("Lon", nearestTrip.getLon());

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,flag);

                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                inboxStyle.addLine(nearestTrip.getDate());
                if(isOnline == true){
                    inboxStyle.addLine("Przewidywana pogoda");
                }

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());


                builder = new NotificationCompat.Builder(getApplicationContext(), "Mynotification")
                        .setContentTitle("Nadchodząca wycieczka")
                        .setContentText("testestse")
                        .setTicker("krotkie powiadomienie")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.error))
                        .setAutoCancel(true)
                        .addAction(R.mipmap.ic_launcher, "Zobacz prognozę pogody", pendingIntent)
                        .setStyle(inboxStyle)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(Notification.DEFAULT_SOUND);


                managerCompat.notify(1, builder.build());




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




    @RequiresApi(api = Build.VERSION_CODES.O)
    Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}
