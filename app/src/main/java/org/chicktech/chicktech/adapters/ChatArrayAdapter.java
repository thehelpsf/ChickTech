package org.chicktech.chicktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.ChatMessage;

import java.util.List;

/**
 * Created by kenanpulak on 10/15/14.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    ViewHolder viewHolder;

    // View lookup cache
    private static class ViewHolder {
        TextView tvMessage;
    }

    public ChatArrayAdapter(Context context, List<ChatMessage> messages) {
        super(context, R.layout.item_chat, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_chat, parent, false);
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvMessage.setText(message.getMessage());

        return convertView;
    }
}
