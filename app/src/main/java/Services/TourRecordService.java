package Services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import Utils.Utilities;

import java.util.Calendar;
import java.util.Date;
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
    private long tourId;
    private double startLat, startLon, endLat, endLon, distance, tripTime = 0.0;
    private boolean isFirst = true;
    private Timer timer = new Timer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        database = new MyDatabase(this, 1);
        tourId = intent.getLongExtra("TourId", 0);
        double time = intent.getDoubleExtra(TIME_EXTRA, 0.0);
        tripTime = time;
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
                Log.d("onLocationChanged",location.toString());
                Calendar calendar = Calendar.getInstance();
                Date date = Utilities.convertToDate(calendar);
                try {
                    if(!isFirst){
                        Location l = new Location(LocationManager.KEY_LOCATIONS);
                        l.setLatitude(endLat);
                        l.setLongitude(endLon);
                        distance += (double)(location.distanceTo(l)/1000f);
                    }
                    endLat = location.getLatitude();
                    endLon = location.getLongitude();
                    points.add(new Point(date.toString(), (int)tourId, endLat, endLon));

                    if(isFirst){
                        startLat = location.getLatitude();
                        startLon = location.getLongitude();
                        isFirst = false;
                    }
                }
                catch (Exception e){
                    Log.d("Error Location Service",e.getMessage());
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("onStatusChanged",provider);
                Log.d("Triptime",""+tripTime);
//                LocationListener.super.onStatusChanged(provider, status, extras);
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                Log.d("onProviderEnabled", provider);
//                LocationListener.super.onProviderEnabled(provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Log.d("onProviderDisabled", provider);
//                LocationListener.super.onProviderDisabled(provider);
            }
        });



        //return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        if(distance != 0.0)
            database.updateTrip((int) tourId, startLat, startLon, endLat, endLon, tripTime, distance);
        for(Point p : points){
            database.addPoint(p.getDate(), p.getTourId(), p.getLat(), p.getLon());
        }
        timer.cancel();
        super.onDestroy();
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
            tripTime++;
            intent.putExtra(TIME_EXTRA, time);
            intent.putExtra("Distance", distance);
            intent.putExtra("Lat", endLat);
            intent.putExtra("Lon", endLon);
            sendBroadcast(intent);
        }
    }
}
