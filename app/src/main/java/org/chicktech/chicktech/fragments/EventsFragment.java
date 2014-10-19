package org.chicktech.chicktech.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.activities.EventDetailActivity;
import org.chicktech.chicktech.adapters.EventArrayAdapter;
import org.chicktech.chicktech.models.Address;
import org.chicktech.chicktech.models.Event;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.utils.CTRestClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EventsFragment extends Fragment {

    ListView lvEvents;
    EventArrayAdapter aEvents;
    ArrayList<Event> events;
    CTRestClient parseClient;
    ProgressBar pb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        Person user = (Person) ParseUser.getCurrentUser();
        if (user != null) {
            //Toast.makeText(getActivity(), "got current user", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "no current user", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        pb.setVisibility(ProgressBar.VISIBLE);
        parseClient.getEventList(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> events, ParseException e) {
            pb.setVisibility(ProgressBar.INVISIBLE);
            if (e == null) {
                aEvents.clear();
                Log.d("events", "Retrieved " + events.size() + " events");
                for (int i = 0; i < events.size(); i++) {
                    aEvents.add((Event)events.get(i));
                }
            } else {
                Log.d("events", "Error: " + e.getMessage());
            }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment

        View view = inflater.inflate(R.layout.fragment_events, container, false);
        // Setup handles to view objects here
        // etFoo = (EditText) view.findViewById(R.id.etFoo);

        pb = (ProgressBar) view.findViewById(R.id.pbLoading);

        createSampleEvents();

        aEvents = new EventArrayAdapter(getActivity(), events);
        lvEvents = (ListView) view.findViewById(R.id.lvEvents);
        lvEvents.setAdapter(aEvents);

        setupListeners();

        parseClient = new CTRestClient();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_events_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.action_new_event:
                Toast.makeText(getActivity(), "launch new event activity", Toast.LENGTH_SHORT).show();
                //createNewEvent();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createNewEvent() {

//        Drone Workshop at Evergreen Museum
//                ChickTech
//        Saturday, August 9, 2014 from 8:00 AM to 7:00 PM (PDT)
//        McMinnville, OR


//        Address tempAddress = new Address("Intel Hawthorn Farm 3 Lobby",
//                "5200 NE Elam Young Pkwy",
//                "",
//                "Hillsboro",
//                "OR",
//                "97214"
//        );

        Address tempAddress = new Address("Portland State University",
            "1825 SW Broadway",
            "Computer Lab EB 325 West",
            "Portland",
            "OR",
            "97201"
            );

        //tempAddress.saveInBackground();

        Date startDate;
        Date endDate;

        try {
            startDate = new SimpleDateFormat("MMMM d, yyyy HH:mm", Locale.US).parse("October 18, 2014 10:00");
            endDate = new SimpleDateFormat("MMMM d, yyyy HH:mm", Locale.US).parse("October 18, 2014 16:00");
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
            return;
        }

        Event tempEvent = new Event("Explore Gaming Workshop",
            "ChickTech and Pixel Arts are teaming up to provide a Gaming Comes to Life workshop on Saturday, October 18 from 10:00 am to 4:00 pm! Explore the important roles needed when creating games including storyteller, designer, programmer, artist, animator, and musician. You will gain hands-on experience by making a playable prototype as an individual or in groups. We will use fun, easy to pick-up game tools like Stencyl, Pixlr, Beepbox, and Twine. You don’t want to miss out!",
            startDate,
            endDate);
        tempEvent.setLocation(tempAddress);
        tempEvent.saveInBackground();

    }

    private void setupListeners() {
        lvEvents.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//Toast.makeText(getActivity(), "See event detail", Toast.LENGTH_SHORT).show();
                Event event = (Event) events.get(position);
                Intent i = new Intent(getActivity(), EventDetailActivity.class);
                i.putExtra("id", event.getObjectId());
                startActivity(i);
            }
        });
    }

    private void createSampleEvents() {
        Event event;
        events = new ArrayList<Event>();
    }
}
