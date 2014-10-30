package org.chicktech.chicktech.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kenanpulak on 10/14/14.
 */
@ParseClassName("Event")
public class Event extends ParseObject {

    //private int eventID;
    /*private String title;
    private String description;
    private String imageURL;
    private String addressID;
    private Address location;
    private Date startDate;
    private Date endDate;
    */

    public Event() {
    }

    public Event(String name, String description, Date startDate, Date endDate) {
        setTitle(name);
        setDescription(description);
        setStartDate(startDate);
        setEndDate(endDate);
    }

    public String getAddressString() {
        Address location = getLocation();
        if (location != null) {
            return location.toString();
        }
        return "unknown";
    }

    public String getNavString() {
        Address location = getLocation();
        if (location != null) {
            return location.toNavString();
        }
        return "unknown";
    }

    public String getDayOfWeek() {
        Date d = getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        String dayOfTheWeek = sdf.format(d).toUpperCase(Locale.US);
        return dayOfTheWeek;
    }

    public String getMonth() {
        Date d = getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
        String month = sdf.format(d).toUpperCase(Locale.US);
        return month;
    }

    public String getDateNumber() {
        Date d = getStartDate();
        SimpleDateFormat sdf = new SimpleDateFormat("d");
        String date = sdf.format(d).toUpperCase(Locale.US);
        return date;
    }

    public String getTimeString() {
        Date sd = getStartDate();
        Date ed = getEndDate();
        SimpleDateFormat sdfStart = new SimpleDateFormat("ha");
        SimpleDateFormat sdfEnd = new SimpleDateFormat("ha");
        String time = sdfStart.format(sd).toUpperCase(Locale.US) + "-"
                + sdfStart.format(ed).toUpperCase(Locale.US);
        return time;
    }

    public String getStartDateString() {
        return getMonth() + " " + getDateNumber();
    }

    // Parse Getters
    public String getTitle() {
        return getString("title");
    }
    public String getDescription() {
        return getString("description");
    }
    public String getImageURL() {
        return getString("imageUrl");
    }
    public String getAddressID() {
        return getString("addressID");
    }
    public Date getStartDate() {
        return getDate("startDate");
    }
    public Date getEndDate() {
        return getDate("endDate");
    }
    public Address getLocation() {
        return (Address) getParseObject("location");
    }
    public String getRsvpYes() {
        String str = getString("rsvpYes");
        if (str == null) {
            return "";
        }
        return str;
    }
    public String getRsvpNo() {
        String str = getString("rsvpNo");
        if (str == null) {
            return "";
        }
        return str;
    }

    public boolean isPersonGoing(Person person) {
        String folks = getRsvpYes();
        if (folks == null) {
            return false;
        }
        return folks.contains(person.getObjectId());
    }

    public boolean isPersonNotGoing(Person person) {
        String folks = getRsvpNo();
        if (folks == null) {
            return false;
        }
        return folks.contains(person.getObjectId());
    }

    public String getRsvpStatusString (Person person) {
        String string = "";
        if (isPersonGoing(person)) {
            return "You plan to attend!";
        }
        if (isPersonNotGoing(person)) {
            return "You plan to miss the event";
        }
        return "You have not RSVP'D yet";
    }

    public String getRsvpStatusStringShort (Person person) {
        String string = "";
        if (isPersonGoing(person)) {
            return "CANCEL RSVP";
        }
        if (isPersonNotGoing(person)) {
            return "RSVP\nYES";
        }
        return "RSVP\nNOW";
    }

    public String getRsvpStatusLabelShort (Person person) {
        String string = "";
        if (isPersonGoing(person)) {
            return "GOING";
        }
        if (isPersonNotGoing(person)) {
            return "NOPE";
        }
        return "RSVP";
    }

    // Parse Setters
    public void setTitle(String value) {
        put("title", value);
    }
    public void setDescription(String value) {
        put("description", value);
    }
    public void setImageURL(String value) {
        put("imageURL", value);
    }
    public void setAddressID(String value) {
        put("addressID", value);
    }
    public void setStartDate(Date value) {
        put("startDate", value);
    }
    public void setEndDate(Date value) {
        put("endDate", value);
    }
    public void setLocation(Address value) {
        put("location", value);
    }
    public void setRsvpYes(String value) {
        put("rsvpYes", value);
    }
    public void setRsvpNo(String value) {
        put("rsvpNo", value);
    }

    public void addRsvpYes(Person person) {
//        if (!isPersonGoing(person)) {
            setRsvpYes(getRsvpYes() + "," + person.getObjectId());
            removeRsvpNo(person);
            saveInBackground();
//        }
    }

    public void addRsvpNo(Person person) {
//        if (!isPersonNotGoing(person)) {
            setRsvpNo(getRsvpNo() + "," + person.getObjectId());
            removeRsvpYes(person);
            saveInBackground();
//        }
    }

    private void removeRsvpNo(Person person) {
        if (isPersonNotGoing(person)) {
            String rsvp = getRsvpNo();
            rsvp = rsvp.replace(person.getObjectId(), "");
            rsvp = rsvp.replace(",,", ",");
            setRsvpNo(rsvp);
        }
    }

    private void removeRsvpYes(Person person) {
        if (isPersonGoing(person)) {
            String rsvp = getRsvpYes();
            rsvp = rsvp.replace(person.getObjectId(), "");
            rsvp = rsvp.replace(",,", ",");
            setRsvpYes(rsvp);
        }
    }

}
