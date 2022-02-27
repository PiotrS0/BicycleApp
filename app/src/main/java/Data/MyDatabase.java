package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import Utils.Utilities;

import java.util.Calendar;

public class MyDatabase extends SQLiteOpenHelper {
    public MyDatabase(@Nullable Context context,  int version) {
        super(context, "test18.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Trip(Id integer not null primary key autoincrement, Date Date not null, Title text, Notification numeric, Distance numeric, Time numeric, StartLat numeric, StartLon numeric, EndLat numeric, EndLon numeric, IsPlanned numeric)");
        db.execSQL("create table Point(Id integer not null primary key autoincrement, Date Date not null, Lat numeric not null, Lon numeric not null, TourId integer not null references Tour(Id))");
        db.execSQL("create table Highlight(Id integer not null primary key autoincrement, Image blob not null, Title text, Description text, Date Date not null, Lat numeric not null, Lon numeric not null, TourId references Tour(Id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addTripPlanned(String title, String date, boolean notification, Double startLat, Double startLon, boolean isPlanned){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Date", date);
        values.put("Title", title);
        values.put("Notification",notification);
        values.put("StartLat",startLat);
        values.put("StartLon",startLon);
        values.put("IsPlanned", isPlanned);
        db.insertOrThrow("Trip",null,values);
    }

    public long addTourStart(){
        SQLiteDatabase db = getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        ContentValues values = new ContentValues();
        values.put("Title", "NEW TOUR");
        values.put("Date", Utilities.sdf.format(calendar.getTime()));
        values.put("Notification", false);
        values.put("IsPlanned", false);
        return db.insertOrThrow("Trip", null, values);
    }

    public void updateTourStart(int id, String title){
        SQLiteDatabase db = getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        values.put("Date", Utilities.sdf.format(calendar.getTime()));
        values.put("Title", title);
        values.put("IsPlanned", false);
        db.update("Trip", values, "Id = "+ id, null);
    }

    public void updateTrip(int id, double startLat, double startLon, double endLat, double endLon, double time, double distance){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("StartLat",startLat);
        values.put("StartLon",startLon);
        values.put("EndLat",endLat);
        values.put("EndLon",endLon);
        values.put("Time", time);
        values.put("Distance", distance);
        db.update("Trip", values,"Id = " + id,null);
    }

    public void saveTrip(int id, String title){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Title", title);
        db.update("Trip", values, "Id = " + id, null);
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
        String[] columns = {"Id","Date","Title","Notification","StartLat","StartLon","IsPlanned"};
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Trip",columns,null,null,null,null,null);
    }

    public Cursor getAllTours(){
        String[] columns = {"Id","Date","Title","Distance","Time","StartLat","StartLon","EndLat","EndLon","IsPlanned"};
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Trip",columns,null,null,null,null,null);
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

    public void deletePoints(int tourId){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Point", "TourId = "+ tourId, null);
    }

    public void updateTripIsPlanned(int id, boolean isPlanned){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("IsPlanned", isPlanned);
        db.update("Trip", cv, "Id = " + id, null);
    }
}
