package org.chicktech.chicktech.receivers;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParsePushBroadcastReceiver;

import org.chicktech.chicktech.activities.LoginActivity;
import org.chicktech.chicktech.utils.AppUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by paul on 10/21/14.
 *
 * This code gets enacted when a user clicks on one our our notifications
 * in the system notification drawer.
 */
public class CustomReceiver extends ParsePushBroadcastReceiver {

    @Override
    public void onPushOpen(Context context, Intent intent) {

        boolean weAreForeground = AppUtils.isThisAppInForeground(context);

        //Log.e("Push", "Clicked");
        Bundle extras = intent.getExtras();
        String json = extras.getString("com.parse.Data");
        try {
            JSONObject jsonObj = new JSONObject(json);
            String message = jsonObj.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        Intent i = new Intent(context, LoginActivity.class);
        i.putExtras(intent.getExtras());
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}