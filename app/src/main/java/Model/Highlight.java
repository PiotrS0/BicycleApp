package Model;

import android.graphics.Bitmap;
import android.media.Image;

import java.util.Date;

public class Highlight {
    private int id;
    private Bitmap image;
    private String description;
    private String date;
    private String lat;
    private String lon;
    private int tourId;

    public Highlight(){}

    public Highlight(Bitmap image, String date) {
        this.image = image;
        this.date = date;
    }

    public Highlight(int id, Bitmap image, String description, String date, String lat, String lon, int tourId) {
        this.id = id;
        this.image = image;
        this.description = description;
        this.date = date;
        this.lat = lat;
        this.lon = lon;
        this.tourId = tourId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }
}
