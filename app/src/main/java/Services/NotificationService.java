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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bicycleApp.R;
import com.bicycleApp.TripDetailsFromNotificationActivity;
import Utils.Utilities;

import java.text.ParseException;
import java.util.Calendar;
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
    private boolean isOnline = true;

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
            @Override
            public void run() {
                database = new MyDatabase(getApplicationContext(), 1);

                cursor = database.getAllTrips();
                if(cursor.getCount() == 0)
                    return;

                while(cursor.moveToNext()){
                    Trip trip = new Trip(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(4),cursor.getDouble(5));
                    if(cursor.getInt(3) == 0)
                        trip.setNotification(false);
                    if(cursor.getInt(6) == 1)
                        tripList.add(trip);
                }

                if(tripList.size() == 0){
                    stopSelf();
                    return;
                }

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 7);
                Date date2 = Utilities.convertToDate(calendar);
                Trip nearestTrip = new Trip();

                for(Trip x : tripList){
                    try {
                        date = Utilities.sdf.parse(x.getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if(date.before(date2) && x.getNotification() == true){
                        date2 = date;
                        nearestTrip = x;
                    }
                }

                if(nearestTrip.getId() == 0){
                    stopSelf();
                    return;
                }

                Intent intent2 = new Intent(getApplicationContext(), TripDetailsFromNotificationActivity.class);
                intent2.putExtra("Date", nearestTrip.getDate());
                intent2.putExtra("Title", nearestTrip.getTitle());
                intent2.putExtra("Notification", nearestTrip.getNotification());
                intent2.putExtra("Lat", nearestTrip.getStartLat());
                intent2.putExtra("Lon", nearestTrip.getStartLon());

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel = new NotificationChannel("Mynotification", "Mynotificaiton", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager manager = getSystemService(NotificationManager.class);
                    manager.createNotificationChannel(channel);
                }
                int flag = 0;
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                    flag = PendingIntent.FLAG_UPDATE_CURRENT;



                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent2,PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

                inboxStyle.addLine(nearestTrip.getDate().substring(0, nearestTrip.getDate().length()-3));
                if(isOnline == true)
                    inboxStyle.addLine(getResources().getString(R.string.weatherNameForecast));

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());

                builder = new NotificationCompat.Builder(getApplicationContext(), "Mynotification")
                        .setContentTitle(getResources().getString(R.string.upcomingTrip))
                        .setContentText(nearestTrip.getDate().substring(0, nearestTrip.getDate().length()-3))
                        .setTicker(getResources().getString(R.string.upcomingTrip))
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_app))
                        .setAutoCancel(true)
                        .addAction(R.mipmap.icon_app, getResources().getString(R.string.weatherShow), pendingIntent)
                        .setStyle(inboxStyle)
                        .setSmallIcon(R.drawable.ic_baseline_directions_bike_24)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(Notification.DEFAULT_SOUND);

                managerCompat.notify(1, builder.build());
            }
        });
    }


}
