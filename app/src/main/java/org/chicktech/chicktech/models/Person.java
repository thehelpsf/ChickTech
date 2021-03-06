package org.chicktech.chicktech.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.chicktech.chicktech.R;

import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created by kenanpulak on 10/14/14.
 */
@ParseClassName("_User")
public class Person extends ParseUser {
    private static final String ROLE_STUDENT = "student";
    private static final String ROLE_MENTOR = "mentor";
    private static final String ROLE_ORGANIZER = "organizer";

    private ImageView iv;

    public static class GetPhotoCallback {
        public void done(Bitmap photo) {
            // Subclasses to override.
        };
    }

    public static class GetAddressCallback {
        public void done(Address addr) {
            // Subclasses to override.
        }
    }

    public static enum Role {
        STUDENT,
        MENTOR,
        ORGANIZER
    }

    @Override
    public String toString() {
        return getUsername();
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
    private String emergencyContactID;
    private String tagline;
    private String interestReason;
    private String interests;
    private Date lastLogin;
    private Bitmap photo;
    private String partnerId;
    */

    // Parse Getters
    public Role getRole() {
        String s = getString("role");
        if (s.equals(ROLE_ORGANIZER)) {
            return Role.ORGANIZER;
        } else if (s.equals(ROLE_MENTOR)) {
            return Role.MENTOR;
        } else {
            return Role.STUDENT;
        }
    }
    public String getRoleString() {
        String role = getString("role");
        if (role.length() == 0)
            return ROLE_STUDENT;
        return role;
    }
    public String getPersonName() {
        return getString("personName");
    }
    public String getHandle() {
        return getString("handle");
}
    public String getPhoneNumber() {return getString("phoneNumber");}
    public String getFormattedPhoneNumber() {
        String s = getPhoneNumber();
        if (s == null || s.isEmpty() || s.length() != 10) {
            return s;
        }

        return "(" + s.substring(0, 3) + ")" + s.substring(3, 6) + "-" + s.substring(6, 10);
    }
    public String getProfileImageUrl() {return getString("profileImageUrl");}
    public void getAddressInBackground(final GetAddressCallback cb) {
        ParseObject addr = getParseObject("address");
        if (addr == null) {
            cb.done(null);
            return;
        }
        addr.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null) {
                    Log.d("Person", "Error getting address:" + e.getMessage());
                    cb.done(null);
                } else {
                    cb.done((Address)parseObject);
                }
            }
        });
    }
    public String getEmergencyContactID() {return getString("emergencyContactID");}
    public Date getLastLogin() {
        return getDate("lastLogin");
    }
    public String getTagline() {
        return getString("tagline");
    }
    public String getInterestReason() {
        return getString("interestReason");
    }
    public String getInterests() {
        return getString("interests");
    }
    public void getPhotoInBackground(final GetPhotoCallback cb) {
        ParseFile file = (ParseFile)get("photo");
        if (file == null) {
            cb.done(null);
            return;
        }
        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e != null) {
                    Log.d("Person", "Error getting profile photo: " + e.getMessage());
                    cb.done(null);
                    return;
                }
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                cb.done(bmp);
            }
        });
    }
    public void loadPhotoInBackgroundToImageView(ImageView ivTarget) {
        iv = ivTarget;

        ParseFile file = (ParseFile)get("photo");
        if (file == null) {
            iv.setImageResource(R.drawable.ic_girl);
            return;
        }

        file.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {

                if (e != null) {
                    Log.d("Person", "Error getting profile photo: " + e.getMessage());
                    iv.setImageResource(R.drawable.ic_girl);
                    return;
                }

                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                iv.setImageBitmap(bmp);

            }
        });
    }
    public String getPartnerId() {
        return getString("partnerID");
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
    public void setAddress(Address value) {
        if (value == null) {
            remove("address");
        } else {
            put("address", value);
        }
    }
    public void setEmergencyContactID(String value) {
        put("emergencyContactID", value);
    }
    public void setLastLogin(Date value) {
        put("lastLogin", value);
    }
    public void setTagline(String value) {
        put("tagline", value);
    }
    public void setInterestReason(String value) {
        put("interestReason", value);
    }
    public void setInterests(String value) {
        put("interests", value);
    }
    public void setPhoto(Bitmap photo) {
        if (photo == null) {
            this.remove("photo");
            return;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] photoBytes = stream.toByteArray();

        ParseFile file = new ParseFile("profilePhoto.jpg", photoBytes);
        file.saveInBackground();
        put("photo", file);
    }
    public void setPartnerId(String id) {
        put("partnerID", id);
    }
    public void setPartnerID(String value) { put("partnerID", value); }

}
