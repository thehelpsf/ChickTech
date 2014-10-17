package org.chicktech.chicktech.utils;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.chicktech.chicktech.models.Person;

/**
 * Created by kenanpulak on 10/14/14.
 */
public class CTRestClient {

    /*


   * getEventList()
   * getRSVPList(int eventid)
   * setEventRSVP(int eventid, int personid, Bool going)
   * getPerson(objectID)
   * getPerson(phone number)
   * setPerson() - update profile
   * setChatMessage(int toPersonId, int fromPersonId, string message)
   * Notification Handler for incoming notifications

     */
    public void getEventList(FindCallback<ParseObject> callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        //query.whereEqualTo("playerName", "Dan Stemkoski");
        query.include("location"); // the key which the associated object was stored
        query.orderByAscending("startDate");
        query.findInBackground(callback);
    }

    public void getRSVPList(int eventid){

    }

    public void setEventRSVP(int eventid, int personid, Boolean going){

    }

    public static void getEventByID(String objectID, GetCallback<ParseObject> callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.whereEqualTo("objectId", objectID);
        query.include("location"); // the key which the associated object was stored
        query.getFirstInBackground(callback);
    }

    public static void getPersonByPhoneNumber(String phoneNumber, GetCallback<ParseUser> callback) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("phoneNumber", phoneNumber);
        query.getFirstInBackground(callback);
    }

    public static void getFullPersonInfo(Person person, GetCallback<Person> callback) {
        // Fetch any relational properties of Person here.
        person.getParseObject("address").fetchIfNeededInBackground(callback);
    }

    public void sendChatMessage(int toPersonID, int fromPersonID, String message){

    }




}
