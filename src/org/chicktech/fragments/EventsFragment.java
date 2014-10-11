package org.chicktech.fragments;

import java.util.ArrayList;

import org.chicktech.R;
import org.chicktech.adapters.EventArrayAdapter;
import org.chicktech.models.CTEvent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

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
				Toast.makeText(getActivity(), "See event detail", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void createSampleEvents() {
		CTEvent event;
		events = new ArrayList<CTEvent>();
		
		event = new CTEvent("ChickTech Hoorah 1", "Come to the big kick-off event of the year! Have fun meeting new people and learning new things.");
		events.add(event);
		event = new CTEvent("ChickTech Hoorah 2", "Come to the big kick-off event of the year! Have fun meeting new people and learning new things.");
		events.add(event);
		event = new CTEvent("ChickTech Hoorah 3", "Come to the big kick-off event of the year! Have fun meeting new people and learning new things.");
		events.add(event);
	}
	
	
}
