package example.com.pos;

/**
 * Created by root on 9/9/17.
 */

public class EventDataModel {
    private String ID;
    private String title;
    private String location;
    private String timeAndDate;

    public EventDataModel(String ID, String title, String location, String timeAndDate) {
        this.ID = ID;
        this.title = title;
        this.location = location;
        this.timeAndDate = timeAndDate;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimeAndDate() {
        return timeAndDate;
    }

    public void setTimeAndDate(String timeAndDate) {
        this.timeAndDate = timeAndDate;
    }
}
