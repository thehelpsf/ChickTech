package org.chicktech.chicktech.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.chicktech.chicktech.activities.ShowPopUpActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class EventNotificationReceiver extends BroadcastReceiver {
    private static final String TAG = "MyCustomReceiver";


    // JSON to send from Parse
    /*
    {
        "action":"org.chicktech.chicktech.EVENT_REMINDER",
            "message":"Girls Crazy Day is Coming Oct 3-th. Don't forget to RSVP!"
    }
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
                    String channel = intent.getExtras().getString("com.parse.Channel");
                    JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
                    Log.d(TAG, "got action " + action + " on channel " + channel + " with:");
                    Iterator itr = json.keys();
                    while (itr.hasNext()) {
                        String key = (String) itr.next();
                        if (key.equals("message"))
                        {
                            //Toast.makeText(context, "Got " + action + " notification", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, ShowPopUpActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                            i.putExtra("message", json.getString("message"));
                            context.getApplicationContext().startActivity(i);
                        }
                        Log.d(TAG, "..." + key + " => " + json.getString(key));
                    }
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }
}