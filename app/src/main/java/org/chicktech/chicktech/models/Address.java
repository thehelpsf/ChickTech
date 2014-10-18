package org.chicktech.chicktech.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by kenanpulak on 10/14/14.
 */
@ParseClassName("Address")
public class Address extends ParseObject {

    public String getAddress1() {
        return getString("address1");
    }
    public String getAddress2() {
        return getString("address2");
    }
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
