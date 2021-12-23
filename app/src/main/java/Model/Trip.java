package Model;

public class Trip {
    private int id;
    private String date;
    private String city;
    private boolean notification;

    public Trip(){}

    public Trip(int id, String date, String city){
        this.id = id;
        this.date = date;
        this.city = city;
        this.notification = true;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setCity(String city){
        this.city = city;
    }

    public void setNotification(boolean notification){
        this.notification = notification;
    }
}
