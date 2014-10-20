package org.chicktech.chicktech.models;

import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by kenanpulak on 10/14/14.
 */
@ParseClassName("Address")
public class Address extends ParseObject {

    public Address () {

    }

    public Address (String name, String address1, String address2, String city, String state, String zipcode) {
        setName(name);
        setAddress1(address1);
        setAddress2(address2);
        setCity(city);
        setState(state);
        setZipcode(zipcode);
    }

    @Override
    public String toString() {
        return getAddress1() + ", " + getCity() + ", " + getState();
    }
    public String toFullString() {
        String addr = "";
        String s = getAddress1();
        if (s != null && !s.isEmpty()) {
            addr += s;
        }
        s = getAddress2();
        if (s != null && !s.isEmpty()) {
            addr += "\n" + s;
        }
        s = getCity();
        if (s != null && !s.isEmpty()) {
            addr += "\n" + s;
        }
        s = getState();
        if (s != null && !s.isEmpty()) {
            addr += ", " + s;
        }
        s = getZipcode();
        if (s != null && !s.isEmpty()) {
            addr += " " + s;
        }
        return addr.trim();
    }
    public String getName() {
        return getString("name");
    }
    public String getAddress1() {
        return getString("address1");
    }
    public String getAddress2() {
        return getString("address2"); }
    public String getCity() {
        return getString("city");

    }
    public String getState() {
        return getString("state");
    }
    public String getZipcode() {
        return getString("zipcode");
    }

    // Parse Setters
    public void setName(String value) {
        put("name", value);
    }
    public void setAddress1(String value) {
        put("address1", value);
    }
    public void setAddress2(String value) {
        put("address2", value);
    }
    public void setCity(String value) {
        put("city", value);
    }
    public void setState(String value) {
        put("state", value);
    }
    public void setZipcode(String value) {
        put("zipcode", value);
    }

}
