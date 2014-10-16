package org.chicktech.chicktech.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.Event;
import org.chicktech.chicktech.utils.CTRestClient;

import java.util.ArrayList;

public class EventDetailActivity extends Activity {

    Event event;
    ArrayList<String> girlsGoing;
    ArrayAdapter<String> aGirlsGoing;
    ListView lvGirlsGoing;
    TextView tvName;
    TextView tvDescription;
    TextView tvDate;
    TextView tvLocation;
    CTRestClient parseClient;
    ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        pb = (ProgressBar) findViewById(R.id.pbLoading);

        Intent i = getIntent();
        //event = (Event) i.getSerializableExtra("event");
        String objectID = i.getStringExtra("id");


        pb.setVisibility(ProgressBar.VISIBLE);
        CTRestClient.getEventByID(objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject event, ParseException e) {
                pb.setVisibility(ProgressBar.INVISIBLE);
                if (event == null) {
                    Toast.makeText(EventDetailActivity.this, "Did not get event details", Toast.LENGTH_LONG).show();
                } else {
                    //Log.d("event", "Retrieved the object.");
                    fillTheForm(event);
                }
            }
        });


        // Get access to all the form controls
        tvName = (TextView) findViewById(R.id.tvEventName);
        tvDescription = (TextView) findViewById(R.id.tvEventDescription);
        lvGirlsGoing = (ListView) findViewById(R.id.lvGirlsGoing);
        tvDate  = (TextView) findViewById(R.id.tvDate);
        tvLocation = (TextView) findViewById(R.id.tvLocation);

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

    private void fillTheForm(ParseObject object) {
        event = (Event) object;
        if (event != null) {
            tvName.setText(event.getTitle());
            tvDescription.setText(Html.fromHtml(event.getDescription()));
            tvDate.setText(event.getStartDate().toString());
            tvLocation.setText(event.getAddressString());
        }
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
            case R.id.action_goto_map:
                gotoMaps();
                return true;
            case R.id.action_rsvp:
                Toast.makeText(this, "RSVP", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotoMaps() {
        if (event == null) {
            Toast.makeText(this, "Don't have event object", Toast.LENGTH_SHORT).show();
            return;
        }

        String zoomLevel = "18";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String data = "geo:0,0?q=" + event.getAddressString();
        if (zoomLevel != null) {
            data = String.format("%s?z=%s", data, zoomLevel);
        }
        intent.setData(Uri.parse(data));
        startActivity(intent);
    }
}
