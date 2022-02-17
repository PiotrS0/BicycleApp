package Utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.time.*;

public class Utilities {

    public static byte[] imageBetweenActivities;
    public static int tripTime;
    public static DecimalFormat df = new DecimalFormat("#.##");

    public static double roundTo2DecimalPlace(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static Date convertToDate(Calendar calendar){
        Date date = new Date();
        date.setTime(calendar.getTimeInMillis());
        return date;
    }

    public static String getTimeStringFromDouble(double time){
        int resultInt = (int) time;
        int hours = (int) resultInt % 86400 / 3600;
        int minutes = (int) resultInt % 86400 % 3600 / 60;
        int seconds = (int) resultInt % 86400 % 3600 % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
}
