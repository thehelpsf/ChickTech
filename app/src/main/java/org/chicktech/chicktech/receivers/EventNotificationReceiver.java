package org.chicktech.chicktech.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.chicktech.chicktech.activities.ShowPopUpActivity;
import org.chicktech.chicktech.utils.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class EventNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "MyCustomReceiver";


    // This code gets called any time one of our notifications comes in for action:
    //   org.chicktech.chicktech.EVENT_REMINDER
    // Regiesterd in the manifest.


    // JSON to send from Parse
    /*
    {
        "alert":"This is the alert message of the push",
        "title":"This is the title of the push",
        "action":"org.chicktech.chicktech.EVENT_REMINDER",
        "message":"Girls Crazy Day is Coming Oct 30th. Don't forget to RSVP!"
        "event_id":"bF2ZiZzsUV"
    */

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (intent == null)
            {
                Log.d(TAG, "Receiver intent null");
            }
            else
            {
                String action = intent.getAction();
                Log.d(TAG, "got action " + action );
                if (action.equals("org.chicktech.chicktech.EVENT_REMINDER"))
                {
                    if (AppUtils.isThisAppInForeground(context)) {
                        // doing nothing with channel so far.
                        String channel = intent.getExtras().getString("com.parse.Channel");
                        JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

                        // json fields: action, alert, title, event_id
                        // if we have an event_id, start up the event detail activity and pass in event_id
                        if (json.optString("event_id") != null) {
                            //Toast.makeText(context, "Got " + action + " notification", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, ShowPopUpActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("id", json.getString("event_id"));
                            i.putExtra("message", json.getString("alert"));
                            context.getApplicationContext().startActivity(i);
                        }
                    } else {
                        // let the system show the notification in the system notifications tray.
                        //Toast.makeText(context, "See tray for notification", Toast.LENGTH_SHORT).show();
                    }

                }
                else if (action.equals("org.chicktech.chicktech.CHAT_MESSAGE")){
                    String channel = intent.getExtras().getString("com.parse.Channel");
                    JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

                    Intent i = new Intent("android.intent.action.CHAT");
                    i.putExtra("fromPersonID", json.getString("fromPersonID"));
                    i.putExtra("toPersonID", json.getString("toPersonID"));
                    i.putExtra("message", json.getString("message"));

                    context.getApplicationContext().sendBroadcast(i);

                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }
}