package Model;

public class Trip {
    private int id;
    private String date;
    private String title;
    private boolean notification;
    private double distance;
    private double time;
    private double startLat;
    private double startLon;
    private double endLat;
    private double endLon;
    private boolean isPlanned;

    public Trip() {
    }

    public Trip(int id, String date, String title, double startLat, double startLon) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.notification = true;
        this.startLat = startLat;
        this.startLon = startLon;
        this.isPlanned = true;
    }

    public Trip(int id, String date, String title, double distance, double time, double startLat, double startLon, double endLat, double endLon) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.distance = distance;
        this.time = time;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public void setStartLon(double startLon) {
        this.startLon = startLon;
    }

    public double getEndLat() {
        return endLat;
    }

    public String getDateWithoutSeconds(){
        return date.substring(0,date.length()-3);
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }

    public boolean getPlanned() {
        return isPlanned;
    }

    public void setPlanned(boolean planned) {
        isPlanned = planned;
    }

    public String getDateWithoutTime(){
        return date.substring(0,date.length()-9);
    }
}
