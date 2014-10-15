package org.chicktech.chicktech.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.CTEvent;
import org.chicktech.chicktech.models.Event;

import java.util.List;

/**
 * Created by kenanpulak on 10/14/14.
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {
    ViewHolder viewHolder;
    View view;

    // View lookup cache
    private static class ViewHolder {
        TextView tvName;
        TextView tvDescription;
        TextView tvDate;
        TextView tvLocation;
    }

    public EventArrayAdapter(Context context, List<Event> events) {
        super(context, R.layout.item_event, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvEventName);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvEventDescription);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(event.getTitle());
        viewHolder.tvDescription.setText(Html.fromHtml(event.getDescription()));
        viewHolder.tvDate.setText(event.getStartDate().toString());
        //viewHolder.tvLocation.setText(event.get);

        return convertView;
    }
}
