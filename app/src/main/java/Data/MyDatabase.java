package Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import android.icu.util.Calendar;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MyDatabase extends SQLiteOpenHelper {
    public MyDatabase(@Nullable Context context,  int version) {
        super(context, "test4.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table Trip(Id integer not null primary key autoincrement, Date Date not null, Lat text not null, Lon text not null, Notification numeric not null)");
        db.execSQL("create table Trip(Id integer not null primary key autoincrement, Date Date not null, City text not null, Notification numeric not null)");
        db.execSQL("create table Tour(Id integer not null primary key autoincrement, StartLat text not null, StartLon text not null, EndLat text not null, EndLon text not null"+
                ", Time numeric not null, Distance numeric not null)");
        db.execSQL("create table Point(Id integer not null primary key autoincrement, Date Date not null, Lat text not null, Lon text not null, TourId integer not null references Tour(Id))");
        db.execSQL("create table Highlight(Id integer not null, Image blob not null, Description text, Date Date not null, TripId references Trip(Id), TourId references Tour(Id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addTrip(String city, String date, Boolean notification){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("City", city);
        values.put("Date", date);
        values.put("Notification",notification);
        db.insertOrThrow("Trip",null,values);
    }

    public Cursor getAllTrips(){
        String[] columns = {"Id","Date","City","Notification"};
        SQLiteDatabase db = getReadableDatabase();
        return db.query("Trip",columns,null,null,null,null,null);
    }
}
