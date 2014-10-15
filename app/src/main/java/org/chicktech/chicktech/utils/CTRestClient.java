package org.chicktech.chicktech.utils;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
        query.orderByAscending("startDate");
        query.findInBackground(callback);
    }

    public void getRSVPList(int eventid){

    }

    public void setEventRSVP(int eventid, int personid, Boolean going){

    }

    public void getPersonByID(String objectID){

    }

    public void getPersonByPhoneNumber(String phoneNumber){

    }

    public void sendChatMessage(int toPersonID, int fromPersonID, String message){

    }




}
