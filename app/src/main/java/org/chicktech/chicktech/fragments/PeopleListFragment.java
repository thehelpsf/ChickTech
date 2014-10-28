package org.chicktech.chicktech.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.adapters.PersonArrayAdapter;
import org.chicktech.chicktech.models.Event;
import org.chicktech.chicktech.utils.CTRestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paul on 10/24/14.
 */
public class PeopleListFragment extends Fragment {
    private String eventID;
    private Event mEvent;
    private ArrayList<ParseUser> people;
    private PersonArrayAdapter aPeople;
    private TextView tvNoRsvpsYet;
    private ListView lvPeople;
    ProgressBar pb;

    public PeopleListFragment () {

    }


    public static PeopleListFragment newInstance () {
        PeopleListFragment fragment = new PeopleListFragment();
//        Bundle args = new Bundle();
//        args.putString("id", eventID);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventID = getArguments().getString("id");

        if (eventID == null || eventID.length() < 1) {
            Toast.makeText(getActivity(), "Invalid event ID", Toast.LENGTH_LONG).show();
            return;
        }

        people = new ArrayList<ParseUser>();
        aPeople = new PersonArrayAdapter(getActivity(), people);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people_list, container, false);
        //Toast.makeText(getActivity(), "ID = " + eventID, Toast.LENGTH_SHORT).show();

        lvPeople = (ListView) view.findViewById(R.id.lvPeople);
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);
        tvNoRsvpsYet = (TextView) view.findViewById(R.id.tvNoRsvpsYet);

        lvPeople.setAdapter(aPeople);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        CTRestClient.getEventByID(CTRestClient.QUERY_SERVER, eventID, new GetCallback<ParseObject>() {
            public void done(ParseObject event, ParseException e) {
                if (event == null) {
                    Toast.makeText(getActivity(), "Did not get event details", Toast.LENGTH_LONG).show();
                } else {
                    //Log.d("event", "Retrieved the object.");
                    mEvent = (Event)event;
                    getRSVPs(mEvent);
                }
            }
        });
    }


    private void getRSVPs(Event event) {
        pb.setVisibility(ProgressBar.VISIBLE);
        CTRestClient.getRSVPList(event, new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null) {
                    aPeople.clear();
                    if (parseUsers.size() > 0) {
                        tvNoRsvpsYet.setVisibility(View.INVISIBLE);
                        aPeople.addAll(parseUsers);
                    } else {
                        tvNoRsvpsYet.setVisibility(View.VISIBLE);
                        //Toast.makeText(getActivity(), "no RSVPs for this event yet", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error Getting RSVP List", Toast.LENGTH_SHORT).show();
                }
                pb.setVisibility(ProgressBar.INVISIBLE);
            }
        });
    }



}
