package Model;

public class Tour {
    private int id;
    private String title;
    private double startLat;
    private double startLon;
    private double endLat;
    private double endLon;
    private double time;
    private double distance;
    private String date;

    public Tour(){}

    public Tour(String title, double startLat, double startLon, double endLat, double endLon, double time, double distance, String date) {
        this.title = title;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
        this.time = time;
        this.distance = distance;
        this.date = date;
    }

    public Tour(int id, String title, double startLat, double startLon, double endLat, double endLon, double time, double distance, String date) {
        this.id = id;
        this.title = title;
        this.startLat = startLat;
        this.startLon = startLon;
        this.endLat = endLat;
        this.endLon = endLon;
        this.time = time;
        this.distance = distance;
        this.date = date;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getDateWithoutSeconds(){
        return date.substring(0,date.length()-3);
    }
}
