package org.chicktech.chicktech.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

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

    // Parse Getters
    public String getTitle() {
        return getString("title");
    }
    public String getDescription() {
        return getString("description");
    }
    public String getImageURL() {
        return getString("imageURL");
    }
    public String getAddressID() {return getString("addressID");}
    public Date getStartDate() {
        return getDate("startDate");
    }
    public Date getEndDate() {
        return getDate("endDate");
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

}
