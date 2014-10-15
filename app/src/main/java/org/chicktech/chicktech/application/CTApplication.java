package org.chicktech.chicktech.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import org.chicktech.chicktech.activities.MainActivity;
import org.chicktech.chicktech.models.Address;
import org.chicktech.chicktech.models.ChatMessage;
import org.chicktech.chicktech.models.Event;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.models.RSVP;

/**
 * Created by kenanpulak on 10/14/14.
 */
public class CTApplication extends Application{

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        CTApplication.context = this;

        ParseObject.registerSubclass(Address.class);
        ParseObject.registerSubclass(ChatMessage.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Person.class);
        ParseObject.registerSubclass(RSVP.class);
        Parse.initialize(this, "h24sgXF8i6c5bRFHteYrN7s6gh7fdqzXIwa8ocWw", "FpddZGkm1EEJT6aF2CXP2O89ihLWwlw5eg7kimUf");

        // Specify an Activity to handle all pushes by default.
        PushService.setDefaultPushCallback(this, MainActivity.class);
    }
}
