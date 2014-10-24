package org.chicktech.chicktech.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.fragments.EventDetailsFragment;
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

        EventDetailsFragment fragment = EventDetailsFragment.newInstance(objectID);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContent, fragment);
        //ft.addToBackStack("EventDetails");
        ft.commit();
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
