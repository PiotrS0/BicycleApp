package Model;

public class Trip {
    private int id;
    private String date;
    private String title;
    private boolean notification;
    private String lastNotificationDate;
    private double lat;
    private double lon;

    public Trip() {
    }

    public Trip(int id, String date, String title, String lastNotificationDate, double lat, double lon) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.lastNotificationDate = lastNotificationDate;
        this.lat = lat;
        this.lon = lon;
        this.notification = true;
    }

    public Trip(int id, String date, String title, boolean notification, String lastNotificationDate, double lat, double lon) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.notification = notification;
        this.lastNotificationDate = lastNotificationDate;
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

    public String getDateWithoutSeconds(){
        return date.substring(0,date.length()-3);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public String getLastNotificationDate() {
        return lastNotificationDate;
    }

    public void setLastNotificationDate(String lastNotificationDate) {
        this.lastNotificationDate = lastNotificationDate;
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
