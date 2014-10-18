package org.chicktech.chicktech.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.CTEvent;

import java.util.ArrayList;

public class EventDetailActivity extends Activity {

    CTEvent event;
    ArrayList<String> girlsGoing;
    ArrayAdapter<String> aGirlsGoing;
    ListView lvGirlsGoing;
    TextView tvName;
    TextView tvDescription;
    TextView tvDate;
    TextView tvLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        event = (CTEvent) i.getParcelableExtra("event");

        tvName = (TextView) findViewById(R.id.tvEventName);
        tvDescription = (TextView) findViewById(R.id.tvEventDescription);
        lvGirlsGoing = (ListView) findViewById(R.id.lvGirlsGoing);
        tvDate  = (TextView) findViewById(R.id.tvDate);
        tvLocation = (TextView) findViewById(R.id.tvLocation);

        tvName.setText(event.getName());
        tvDescription.setText(Html.fromHtml(event.getDescription()));
        tvDate.setText(event.getStartDate());
        tvLocation.setText(event.getLocation());

        girlsGoing = new ArrayList<String>();
        girlsGoing.add("Bonnie");
        girlsGoing.add("Steph");
        girlsGoing.add("Linda");
        girlsGoing.add("Liz");
        girlsGoing.add("Brenda");
        girlsGoing.add("Julie");
        girlsGoing.add("Cathy");
        girlsGoing.add("Lydia");

        aGirlsGoing = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, girlsGoing);
        lvGirlsGoing.setAdapter(aGirlsGoing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_rsvp:
                Toast.makeText(this, "RSVP", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
