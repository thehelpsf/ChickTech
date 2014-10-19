package org.chicktech.chicktech.utils;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.chicktech.chicktech.models.ChatMessage;
import org.chicktech.chicktech.models.Person;

import java.util.ArrayList;
import java.util.List;

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

    public static void getPersonById(String id, GetCallback<ParseUser> callback) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(id, callback);
    }

    public static void getFullPersonInfo(Person person, GetCallback<Person> callback) {
        // Fetch any relational properties of Person here.
        person.getParseObject("address").fetchIfNeededInBackground(callback);
    }

    public static void sendChatMessage(String toPersonID, String fromPersonID, String message){

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setToPersonID(toPersonID);
        chatMessage.setFromPersonID(fromPersonID);
        chatMessage.setMessage(message);
        chatMessage.saveInBackground();

        // Send push notification to query
        ParsePush push = new ParsePush();
        ParseQuery pQuery = ParseInstallation.getQuery(); // <-- Installation query
        pQuery.whereEqualTo("userID", toPersonID);

        push.sendMessageInBackground(message,pQuery);

    }

    public void getAllChatMessages (Person person, FindCallback<ParseObject> callback){

        ParseQuery<ParseObject> fromPersonQuery = ParseQuery.getQuery("ChatMessage");
        fromPersonQuery.whereEqualTo("fromPersonID", person.getObjectId());

        ParseQuery<ParseObject> toPersonQuery = ParseQuery.getQuery("ChatMessage");

        toPersonQuery.whereEqualTo("toPersonID", person.getObjectId());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(fromPersonQuery);
        queries.add(toPersonQuery);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.orderByAscending("createdAt");
        mainQuery.findInBackground(callback);

    }




}
