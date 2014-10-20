package org.chicktech.chicktech.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by kenanpulak on 10/14/14.
 */
@ParseClassName("ChatMessage")
public class ChatMessage extends ParseObject implements Serializable{

    // might need
    //private int messageID;
    //private Date createdAt;

    //private int fromPersonID;
    //private int toPersonID;
    //private String message;

    // ID's are strings because in parse objectID is a string
    public String getFromPersonID() {
        return getString("fromPersonID");
    }
    public String getToPersonID() {
        return getString("toPersonID");
    }
    public String getMessage() {
        return getString("message");
    }

    // Parse Setters
    public void setFromPersonID(String value) {
        put("fromPersonID", value);
    }
    public void setToPersonID(String value) {
        put("toPersonID", value);
    }
    public void setMessage(String value) {
        put("message", value);
    }

}
