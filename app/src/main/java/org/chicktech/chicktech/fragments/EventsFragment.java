package org.chicktech.chicktech.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.activities.EventDetailActivity;
import org.chicktech.chicktech.adapters.EventArrayAdapter;
import org.chicktech.chicktech.models.Event;
import org.chicktech.chicktech.utils.CTRestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EventsFragment extends Fragment {

    ListView lvEvents;
    EventArrayAdapter aEvents;
    ArrayList<Event> events;
    CTRestClient parseClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment

        View view = inflater.inflate(R.layout.fragment_events, container, false);
        // Setup handles to view objects here
        // etFoo = (EditText) view.findViewById(R.id.etFoo);

        createSampleEvents();

        aEvents = new EventArrayAdapter(getActivity(), events);
        lvEvents = (ListView) view.findViewById(R.id.lvEvents);
        lvEvents.setAdapter(aEvents);

        setupListeners();

        parseClient = new CTRestClient();
        parseClient.getEventList(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> events, ParseException e) {
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

        return view;
    }

    private void setupListeners() {
        lvEvents.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), "See event detail", Toast.LENGTH_SHORT).show();
//                Event event = (Event) events.get(position);
//                Intent i = new Intent(getActivity(), EventDetailActivity.class);
//                i.putExtra("event", event);
//                startActivity(i);
            }
        });
    }

    private void createSampleEvents() {
        Event event;
        events = new ArrayList<Event>();

//        event = new CTEvent("ChickTech Hoorah 1", "<b>Come to the big kick-off event</b> of the year! Have fun meeting new people and learning new things. ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit.");
//        events.add(event);
//        event = new CTEvent("ChickTech Hoorah 2", "Come to the big kick-off event of the year! Have fun meeting new people and learning new things.");
//        events.add(event);
//        event = new CTEvent("ChickTech Hoorah 3", "Come to the big kick-off event of the year! Have fun meeting new people and learning new things.");
//        events.add(event);
//        event = new CTEvent("ChickTech Hoorah 4", "Come to the big kick-off event of the year! Have fun meeting new people and learning new things. ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit.");
//        events.add(event);
//        event = new CTEvent("ChickTech Hoorah 5", "Come to the big kick-off event of the year! Have fun meeting new people and learning new things. ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit.");
//        events.add(event);
    }
}
