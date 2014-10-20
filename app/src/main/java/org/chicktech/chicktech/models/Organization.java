package org.chicktech.chicktech.models;

import android.util.Log;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;

/**
 * Created by Jing Jin on 10/19/14.
 */
@ParseClassName("Organization")
public class Organization extends ParseObject {
    public static class GetAddressCallback {
        public void done(Address addr) {
            // Subclasses to override.
        }
    }

    public Organization () {
    }

    public String getDescription() {
        return getString("description");
    }
    public String getEmail() {
        return getString("email");
    }
    public String getPhoneNumber() {
        return getString("phoneNumber");
    }
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


    public void setDescription(String val) {
        put("description", val);
    }
    public void setEmail(String val) {
        put("email", val);
    }
    public void setPhoneNumber(String val) {
        put("phoneNumber", val);
    }
    public void setAddress(Address value) {
        if (value == null) {
            remove("address");
        } else {
            put("address", value);
        }
    }

}
