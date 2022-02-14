package Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.bicycleApp.MainActivity;
import com.bicycleApp.RecordActivity;

import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import Data.MyDatabase;
import Model.Point;

public class TourRecordService extends Service {

    public static final String TIMER_UPDATED = "timerUpdated";
    public static final String TIME_EXTRA = "timeExtra";
    private static final int TODO = Service.START_STICKY_COMPATIBILITY;
    private MyDatabase database;
    private List<Point> points = new LinkedList<Point>();


    Handler handler = new Handler();

    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        database = new MyDatabase(this, 1);

        double time = intent.getDoubleExtra(TIME_EXTRA, 0.0);
        timer.scheduleAtFixedRate(new TimeTask(time), 0, 1000);

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 3.0f, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //points.add(new Point())
                        String s = "" + location.getLatitude() + " : " + location.getLongitude();
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFlushComplete(int requestCode) {
                LocationListener.super.onFlushComplete(requestCode);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                LocationListener.super.onStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                LocationListener.super.onProviderDisabled(provider);
            }
        });

        handler.post(new Runnable() {
            @Override
            public void run() {
                //Toast.makeText(getApplicationContext(), "123", Toast.LENGTH_SHORT).show();
            }
        });

        timer.scheduleAtFixedRate(new LocTask(), 0, 5000);


        //return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {

        for(Point p : points){
            database.addPoint(p.getDate(), p.getTourId(), p.getLat(), p.getLon());
        }
        timer.cancel();
        super.onDestroy();
    }

    class LocTask extends TimerTask{

        public LocTask(){

        }

        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(getApplicationContext(), "onStart", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class TimeTask extends TimerTask{
        private double time;

        public TimeTask(double time){
           this.time = time;
        }

        @Override
        public void run(){
            Intent intent = new Intent(TIMER_UPDATED);
            time++;
            intent.putExtra(TIME_EXTRA, time);
            sendBroadcast(intent);
        }
    }
}
