package Model;

public class Point {
    private int id;
    private String date;
    private int tourId;
    private double lat;
    private double lon;

    public Point() {
    }

    public Point(String date, int tourId, double lat, double lon) {
        this.date = date;
        this.tourId = tourId;
        this.lat = lat;
        this.lon = lon;
    }

    public Point(int id, String date, int tourId, double lat, double lon) {
        this.id = id;
        this.date = date;
        this.tourId = tourId;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
