package org.chicktech.adapters;

import java.util.List;

import org.chicktech.R;
import org.chicktech.models.CTEvent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventArrayAdapter extends ArrayAdapter<CTEvent> {
	TextView tvName;
	TextView tvDescription;

	public EventArrayAdapter(Context context, List<CTEvent> events) {
		super(context, R.layout.item_event, events);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CTEvent event = getItem(position);
		
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
		} 
		
		tvName = (TextView) convertView.findViewById(R.id.tvEventName);
		tvDescription = (TextView) convertView.findViewById(R.id.tvEventDescription);
		
		tvName.setText(event.getName());
		tvDescription.setText(event.getDescription());
		
		return convertView;
	}

}
