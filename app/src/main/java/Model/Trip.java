package Model;

public class Trip {
    private int id;
    private String date;
    private String title;
    private boolean notification;

    public Trip(){}

    public Trip(int id, String date, String title){
        this.id = id;
        this.date = date;
        this.title = title;
        this.notification = true;
    }

    public Trip(int id, String date, String title, boolean notification) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.notification = notification;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDateWithoutSeconds(){
        return date.substring(0,date.length()-3);
    }

    public String getTitle() {
        return title;
    }

    public boolean getNotification() {
        return notification;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setDate(String date){
        this.date = date;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setNotification(boolean notification){
        this.notification = notification;
    }
}
