package org.chicktech.chicktech.utils;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.chicktech.chicktech.models.ChatMessage;
import org.chicktech.chicktech.models.Event;
import org.chicktech.chicktech.models.Person;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kenanpulak on 10/14/14.
 */
public class CTRestClient {

    final public static boolean QUERY_LOCAL = true;
    final public static boolean QUERY_SERVER = false;

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
    public void getEventList(boolean queryLocal, FindCallback<ParseObject> callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        //query.whereEqualTo("playerName", "Dan Stemkoski");
        if (queryLocal) {
            query.fromLocalDatastore();
        }
        query.include("location"); // the key which the associated object was stored
        query.orderByAscending("startDate");
        query.findInBackground(callback);
    }

    public static void getRSVPList(Event event, FindCallback<ParseUser> callback){
        String yeses = event.getRsvpYes();
        if (yeses.length() < 1) {
            return; // no rsvps in this list.
        }

        yeses = yeses.substring(1);
        String []ids = yeses.split(",");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("objectId", Arrays.asList(ids));
        query.findInBackground(callback);
    }

    public void setEventRSVP(int eventid, int personid, Boolean going){

    }

    public static void getEventByID(boolean queryLocal, String objectID, GetCallback<ParseObject> callback){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        if (queryLocal) {
            query.fromLocalDatastore();
        }
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

    public static void getOrganizationInfo(GetCallback<ParseObject> callback) {
        // Right now there's only 1 organization, so just retrieve the first one
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");
        query.getFirstInBackground(callback);
    }

    public static void sendChatMessage(String toPersonID, final String fromPersonID, String message, final GetCallback<ParseObject> callback){

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setToPersonID(toPersonID);
        chatMessage.setFromPersonID(fromPersonID);
        chatMessage.setMessage(message);
        chatMessage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                CTRestClient.getLastChatMessage(fromPersonID,callback);
            }
        });

        // Send push notification to query
        ParsePush push = new ParsePush();
        ParseQuery pQuery = ParseInstallation.getQuery(); // <-- Installation query
        pQuery.whereEqualTo("userID", toPersonID);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("action","org.chicktech.chicktech.CHAT_MESSAGE");
            jsonObject.put("message",message);
            jsonObject.put("toPersonID",toPersonID);
            jsonObject.put("fromPersonID",fromPersonID);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        push.setData(jsonObject);
        push.setQuery(pQuery);
        push.sendInBackground();

    }

    public static void getAllChatMessages (Person person, final FindCallback<ParseObject> callback){

        ParseQuery<ParseObject> fromPersonQuery = ParseQuery.getQuery("ChatMessage");
        fromPersonQuery.whereEqualTo("fromPersonID", person.getObjectId());

        ParseQuery<ParseObject> toPersonQuery = ParseQuery.getQuery("ChatMessage");

        toPersonQuery.whereEqualTo("toPersonID", person.getObjectId());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(fromPersonQuery);
        queries.add(toPersonQuery);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.orderByAscending("createdAt");
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> messages, ParseException e) {
                //Remove the previously cached results
                ParseObject.unpinAllInBackground("chatMessages", new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        // Cache the new results
                        ParseObject.pinAllInBackground("chatMessages", messages);
                    }
                });
            }
        });

    }

    public static void getLastChatMessage (String personID, final GetCallback<ParseObject> callback){

        ParseQuery<ParseObject> fromPersonQuery = ParseQuery.getQuery("ChatMessage");
        fromPersonQuery.whereEqualTo("fromPersonID", personID);

        ParseQuery<ParseObject> toPersonQuery = ParseQuery.getQuery("ChatMessage");

        toPersonQuery.whereEqualTo("toPersonID", personID);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(fromPersonQuery);
        queries.add(toPersonQuery);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.orderByDescending("createdAt");

        mainQuery.getFirstInBackground(callback);
    }
}
