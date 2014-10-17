package org.chicktech.chicktech.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by kenanpulak on 10/14/14.
 */
@ParseClassName("_User")
public class Person extends ParseUser {
    private static final String ROLE_STUDENT = "student";
    private static final String ROLE_MENTOR = "mentor";

    public static enum Role {
        STUDENT,
        MENTOR
    }

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
    public Role getRole() {
        String s = getString("role");
        if (s.equals(ROLE_MENTOR)) {
            return Role.MENTOR;
        } else {
            return Role.STUDENT;
        }
    }
    public String getPersonName() {
        return getString("personName");
    }
    public String getHandle() {
        return getString("handle");
}
    public String getPhoneNumber() {return getString("phoneNumber");}
    public String getProfileImageUrl() {return getString("profileImageUrl");}
    public String getAddressID() {return getString("addressID");}
    public String getEmergencyContactID() {return getString("emergencyContactID");}
    public Date getLastLogin() {
        return getDate("lastLogin");
    }

    // Parse Setters
    public void setRole(Role role) {
        switch(role) {
            case MENTOR:
                put("role", ROLE_MENTOR);
                break;
            case STUDENT:
            default:
                put("role", ROLE_STUDENT);
        }
    }
    public void setPersonName(String value) {
        put("personName", value);
    }
    public void setHandle(String value) {
        put("handle", value);
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
