package org.chicktech.chicktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.ChatMessage;
import org.chicktech.chicktech.models.Person;

import java.util.List;

/**
 * Created by kenanpulak on 10/15/14.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    ViewHolder viewHolder;
    Person currentUser;

    // View lookup cache
    private static class ViewHolder {
        TextView tvMessage;
    }

    public ChatArrayAdapter(Context context, List<ChatMessage> messages) {
        super(context, R.layout.item_chat, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        currentUser = (Person) ParseUser.getCurrentUser();

        ChatMessage message = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = getInflatedLayoutForType(message.getFromPersonID());
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvMessage.setText(message.getMessage());

        return convertView;
    }

    // Given the item type, responsible for returning the correct inflated XML layout file
    private View getInflatedLayoutForType(String objectID) {

        if (objectID.equals(currentUser.getObjectId())){
            return LayoutInflater.from(getContext()).inflate(R.layout.item_chat, null);
        }
        else{
            return LayoutInflater.from(getContext()).inflate(R.layout.item_chat_other, null);
        }
    }
}
