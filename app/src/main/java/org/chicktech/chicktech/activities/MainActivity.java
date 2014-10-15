package org.chicktech.chicktech.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.fragments.ChatFragment;
import org.chicktech.chicktech.fragments.EventsFragment;
import org.chicktech.chicktech.fragments.ProfileFragment;
import org.chicktech.chicktech.models.Address;
import org.chicktech.chicktech.models.ChatMessage;
import org.chicktech.chicktech.models.Event;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.models.RSVP;
import org.chicktech.chicktech.utils.FragmentNavigationDrawer;

import java.util.Date;


public class MainActivity extends ActionBarActivity {

    private FragmentNavigationDrawer dlDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find our drawer view
        dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
        // Setup drawer view
        dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer),
                R.layout.drawer_nav_item, R.id.flContent);
        // Add nav items
        dlDrawer.addNavItem("Events", "Events", EventsFragment.class);
        dlDrawer.addNavItem("Profile", "Profile", ProfileFragment.class);
        dlDrawer.addNavItem("Chat", "Chat", ChatFragment.class);
        // Select default
        if (savedInstanceState == null) {
            dlDrawer.selectDrawerItem(0);
        }

        // Temporary creation of models in parse for testing
        sendModelsToParse();

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, "successfully subscribed to the broadcast channel.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "did not subscribe to broadcast channel", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    // Temporary creation of models in parse for testing
    private void sendModelsToParse(){
        Address tempAddress = new Address();
        ChatMessage tempChatMessage = new ChatMessage();
        //Event tempEvent = new Event("Event One", "This is the frist event of the year.", new Date(), new Date());
        Person tempPerson = new Person();
        RSVP tempRSVP = new RSVP();
        tempAddress.saveInBackground();
        tempChatMessage.saveInBackground();
        //tempEvent.saveInBackground();
        tempPerson.saveInBackground();
        tempRSVP.saveInBackground();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        if (dlDrawer.isDrawerOpen()) {
            // Uncomment to hide menu items
            // menu.findItem(R.id.mi_test).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Uncomment to inflate menu items to Action Bar
        // inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dlDrawer.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
    }
}
