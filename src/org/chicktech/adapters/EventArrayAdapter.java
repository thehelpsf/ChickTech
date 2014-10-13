package org.chicktech.adapters;

import java.util.List;

import org.chicktech.R;
import org.chicktech.models.CTEvent;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EventArrayAdapter extends ArrayAdapter<CTEvent> {
	ViewHolder viewHolder;
	View view;

	// View lookup cache
	private static class ViewHolder {
		TextView tvName;
		TextView tvDescription;
	}

	public EventArrayAdapter(Context context, List<CTEvent> events) {
		super(context, R.layout.item_event, events);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CTEvent event = getItem(position);
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
			viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvEventName);
			viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvEventDescription);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tvName.setText(event.getName());
		viewHolder.tvDescription.setText(Html.fromHtml(event.getDescription()));
		
		return convertView;
	}

}
