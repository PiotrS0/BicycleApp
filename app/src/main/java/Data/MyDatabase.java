package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Date;

import Model.Trip;

public class MyDatabase extends SQLiteOpenHelper {
    public MyDatabase(@Nullable Context context,  int version) {
        super(context, "test10.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Trip(Id integer not null primary key autoincrement, Date Date not null, Title text, Notification numeric not null, LastNotificationDate Date not null, Lat numeric not null, Lon numeric not null)");
        db.execSQL("create table Tour(Id integer not null primary key autoincrement, Title text, StartLat text not null, StartLon text not null, EndLat text not null, EndLon text not null"+
                ", Time numeric not null, Distance numeric not null, Date Date not null)");
        db.execSQL("create table Point(Id integer not null primary key autoincrement, Date Date not null, Lat text not null, Lon text not null, TourId integer not null references Tour(Id))");
        db.execSQL("create table Highlight(Id integer not null primary key autoincrement, Image blob not null, Title text, Description text, Date Date not null, Lat numeric not null, Lon numeric not null, TourId references Tour(Id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addTrip(String title, String date, Boolean notification, String lastNotificationDate, Double lat, Double lon){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Date", date);
        values.put("Title", title);
        values.put("Notification",notification);
        values.put("LastNotificationDate", lastNotificationDate);
        values.put("Lat",lat);
        values.put("Lon",lon);
        db.insertOrThrow("Trip",null,values);
    }

    public void addTour(String title, double startLat, double startLon, double endLat, double endLon, double time, double distance, String date){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title",title);
        values.put("StartLat",startLat);
        values.put("StartLon",startLon);
        values.put("EndLat",endLat);
        values.put("EndLon",endLon);
        values.put("Time",time);
        values.put("Distance",distance);
        values.put("Date", date);
        db.insertOrThrow("Tour",null, values);
    }

    public void addPoint(String date, int tourId, double lat, double lon){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Date",date);
        values.put("Lat",lat);
        values.put("Lon",lon);
        values.put("TourId",tourId);
        db.insertOrThrow("Point",null,values);
    }

    public void addHighlight(byte[] image, String title, String description,String date, double lat, double lon, int tourId){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Image", image);
        values.put("Title",title);
        values.put("Description", description);
        values.put("Date",date);
        values.put("Lat",lat);
        values.put("Lon",lon);
        values.put("TourId",tourId);
        db.insertOrThrow("Highlight",null,values);
    }

    public Cursor getAllTrips(){
        String[] columns = {"Id","Date","Title","Notification","LastNotificationDate","Lat","Lon"};
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Trip",columns,null,null,null,null,null);
    }

    public Cursor getAllTours(){
        String[] columns = {"Id","Title","StartLat","StartLon","EndLat","EndLon","Time","Distance","Date"};
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Tour",columns,null,null,null,null,null);
    }

    public Cursor getAllHighlights(){
        String[] columns = {"Id","Image","Title","Description","Date","Lat","Lon","TourId"};
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Highlight",columns,null,null,null,null,null);
    }

    public Cursor getTourPoints(int tourId){
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Point",null,"TourId = " + tourId,null,null,null,null);
    }

    public void clearTable(String tableName){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + tableName);
    }

    public void deleteRow(String table, int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(table,"Id = "+ id,null);
    }

    public void changeTripNotification(int id, boolean notification){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Notification", notification);
        db.update("Trip",cv,"Id = " + id,null);
    }
//    public Cursor getNearestTrip(Date date){
//        SQLiteDatabase db = getReadableDatabase();
//        return db.query("Trip",null, "Date < " + date, null, null, null, null);
//    }


}
