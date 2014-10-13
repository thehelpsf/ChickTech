package org.chicktech.fragments;

import java.util.ArrayList;

import org.chicktech.EventDetailActivity;
import org.chicktech.R;
import org.chicktech.adapters.EventArrayAdapter;
import org.chicktech.models.CTEvent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class EventsFragment extends Fragment {
	ListView lvEvents;
	EventArrayAdapter aEvents;
	ArrayList<CTEvent> events;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		
		View view = inflater.inflate(R.layout.fragement_events, container, false);
		// Setup handles to view objects here
		// etFoo = (EditText) view.findViewById(R.id.etFoo);
		
		createSampleEvents();
		
		aEvents = new EventArrayAdapter(getActivity(), events);
		lvEvents = (ListView) view.findViewById(R.id.lvEvents);
		lvEvents.setAdapter(aEvents);
		
		setupListeners();
		
		return view;
	}

	private void setupListeners() {
		lvEvents.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Toast.makeText(getActivity(), "See event detail", Toast.LENGTH_SHORT).show();
				Intent i = new Intent(getActivity(), EventDetailActivity.class);
				startActivity(i);
			}
		});
	}

	private void createSampleEvents() {
		CTEvent event;
		events = new ArrayList<CTEvent>();
		
		event = new CTEvent("ChickTech Hoorah 1", "Come to the big kick-off event of the year! Have fun meeting new people and learning new things. ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit in vulputate velit esse molestie consequat, vel illum dolore eu feugiat nulla facilisis at vero eros et accumsan et iusto odio dignissim qui blandit praesent luptatum zzril delenit.");
		events.add(event);
		event = new CTEvent("ChickTech Hoorah 2", "Come to the big kick-off event of the year! Have fun meeting new people and learning new things.");
		events.add(event);
		event = new CTEvent("ChickTech Hoorah 3", "Come to the big kick-off event of the year! Have fun meeting new people and learning new things.");
		events.add(event);
	}
	
	
}
