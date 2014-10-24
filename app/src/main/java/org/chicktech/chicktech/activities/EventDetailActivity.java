package org.chicktech.chicktech.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.fragments.EventDetailsFragment;
import org.chicktech.chicktech.fragments.PeopleListFragment;
import org.chicktech.chicktech.listeners.FragmentTabListener;
import org.chicktech.chicktech.models.Person;

public class EventDetailActivity extends ActionBarActivity {

    Person person;
    String objectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_event_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        person = (Person) ParseUser.getCurrentUser();

        Intent i = getIntent();
        objectID = i.getStringExtra("id");

        setupTabs();

//        EventDetailsFragment fragment = EventDetailsFragment.newInstance(objectID);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.flContent, fragment);
//        //ft.addToBackStack("EventDetails");
//        ft.commit();
    }



    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Bundle args = new Bundle();
        args.putString("id", objectID);

        ActionBar.Tab tab1 = actionBar
                .newTab()
                .setText("Detail")
                //.setIcon(R.drawable.ic_rsvp)
                .setTabListener(
                        new FragmentTabListener<EventDetailsFragment>(R.id.flContent, this, "Detail",
                                EventDetailsFragment.class, args));

        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        ActionBar.Tab tab2 = actionBar
                .newTab()
                .setText("People")
                //.setIcon(R.drawable.ic_location)
                .setTabListener(
                        new FragmentTabListener<PeopleListFragment>(R.id.flContent, this, "RSVPs",
                                PeopleListFragment.class, args));

        actionBar.addTab(tab2);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.event_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
