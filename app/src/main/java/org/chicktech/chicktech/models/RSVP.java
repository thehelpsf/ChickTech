package org.chicktech.chicktech.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by kenanpulak on 10/14/14.
 */
@ParseClassName("RSVP")
public class RSVP extends ParseObject{

    /*private Person person;
    private String status;
    private Date createdAt;*/

    // Parse Getters
    public String getPersonID() {
        return getString("personID");
    }
    public Boolean getStatus() {return getBoolean("status");}

    // Parse Setters
    public void setPersonID(String value) {
        put("personID", value);
    }
    public void setStatus(Boolean value) {put("status", value);}
}