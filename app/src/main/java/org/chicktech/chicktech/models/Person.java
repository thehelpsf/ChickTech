package org.chicktech.chicktech.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by kenanpulak on 10/14/14.
 */
@ParseClassName("Person")
public class Person extends ParseObject{

    //private int userID;
    //private Address address;
    //private Person emergencyContact;
    /*private String role;
    private String personName;
    private String handle;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
    private String addressID;
    private String emergencyContactID;
    private Date lastLogin;*/

    // Parse Getters
    public String getRole() {
        return getString("role");
    }
    public String getPersonName() {
        return getString("personName");
    }
    public String getHandle() {
        return getString("handle");
    }
    public String getEmail() {return getString("email");}
    public String getPhoneNumber() {return getString("phoneNumber");}
    public String getProfileImageUrl() {return getString("profileImageUrl");}
    public String getAddressID() {return getString("addressID");}
    public String getEmergencyContactID() {return getString("emergencyContactID");}
    public Date getLastLogin() {
        return getDate("lastLogin");
    }

    // Parse Setters
    public void setRole(String value) {
        put("role", value);
    }
    public void setPersonName(String value) {
        put("personName", value);
    }
    public void setHandle(String value) {
        put("handle", value);
    }
    public void setEmail(String value) {
        put("email", value);
    }
    public void setPhoneNumber(String value) {
        put("phoneNumber", value);
    }
    public void setProfileImageUrl(String value) {
        put("profileImageUrl", value);
    }
    public void setAddressID(String value) {
        put("addressID", value);
    }
    public void setEmergencyContactID(String value) {
        put("emergencyContactID", value);
    }
    public void setLastLogin(Date value) {
        put("lastLogin", value);
    }

}
