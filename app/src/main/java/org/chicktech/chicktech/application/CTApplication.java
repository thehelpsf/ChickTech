package org.chicktech.chicktech.application;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.chicktech.chicktech.models.Address;
import org.chicktech.chicktech.models.ChatMessage;
import org.chicktech.chicktech.models.Event;
import org.chicktech.chicktech.models.Organization;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.models.RSVP;

/**
 * Created by kenanpulak on 10/14/14.
 */
public class CTApplication extends Application{
    public static final String APP_TAG = "ChickTechApp";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        CTApplication.context = this;

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);


        ParseObject.registerSubclass(Address.class);
        ParseObject.registerSubclass(ChatMessage.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Person.class);
        ParseObject.registerSubclass(RSVP.class);
        ParseObject.registerSubclass(Organization.class);
        Parse.initialize(this, "h24sgXF8i6c5bRFHteYrN7s6gh7fdqzXIwa8ocWw", "FpddZGkm1EEJT6aF2CXP2O89ihLWwlw5eg7kimUf");
        if (ParseUser.getCurrentUser() != null){
            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
            installation.put("userID", ParseUser.getCurrentUser().getObjectId());
            installation.saveInBackground();
        }

//        ParsePush.subscribeInBackground("test", new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Log.d("debug", "successfully subscribed to the broadcast channel.");
//                    //Toast.makeText(MainActivity.this, "successfully subscribed to the broadcast channel.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.d("debug", "successfully subscribed to the broadcast channel.");
//                    //Toast.makeText(MainActivity.this, "did not subscribe to broadcast channel", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

    }

    public static Context getContext() {
        return context;
    }
}
