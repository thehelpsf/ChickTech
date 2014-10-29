package org.chicktech.chicktech.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.ChatMessage;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.utils.CTRestClient;
import org.chicktech.chicktech.views.RoundedImageView;

import java.util.List;

/**
 * Created by kenanpulak on 10/15/14.
 */
public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    ViewHolder viewHolder;
    Person currentUser;
    boolean fetchedUserPhoto = false;
    boolean fetchedPartnerPhoto = false;
    Bitmap userPhoto;
    Bitmap partnerPhoto;

    // View lookup cache
    private static class ViewHolder {
        TextView tvMessage;
        RoundedImageView roundedImageView;
    }

    public ChatArrayAdapter(Context context, List<ChatMessage> messages) {
        super(context, 0, messages);

        currentUser = (Person) ParseUser.getCurrentUser();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        currentUser = (Person) ParseUser.getCurrentUser();

        ChatMessage message = getItem(position);

        if (convertView != getInflatedLayoutForType(message.getFromPersonID())) {
            viewHolder = new ViewHolder();
            convertView = getInflatedLayoutForType(message.getFromPersonID());
            viewHolder.tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
            viewHolder.roundedImageView = (RoundedImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView textView = (TextView) view;

                    String url = textView.getText().toString();
                    if (url.contains("chicktech://")){

                        String segments[] = url.split("//");
                        String objectID = segments[segments.length - 1];
                        Intent intent= new Intent("com.chicktech.LOAD_URL");
                        intent.putExtra("url",objectID);
                        getContext().sendBroadcast(intent);
                    }

                }
            });
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (message.getFromPersonID().equals(currentUser.getObjectId())){
            populateUserPhotoAsync(viewHolder.roundedImageView);
        }
        else{
            populatePartnerPhotoAsync(viewHolder.roundedImageView);
        }

        viewHolder.tvMessage.setText(message.getMessage());

        return convertView;
    }

    // Caches or makes an async call as needed
    private void populateUserPhotoAsync(final RoundedImageView v) {
        if (fetchedUserPhoto) {
            populatePhoto(userPhoto, v);
            return;
        }

        currentUser.getPhotoInBackground(new Person.GetPhotoCallback() {
            @Override
            public void done(Bitmap photo) {
                fetchedUserPhoto = true;
                userPhoto = photo;
                populatePhoto(userPhoto, v);
            }
        });
    }

    // Caches or makes an async call as needed
    private void populatePartnerPhotoAsync(final RoundedImageView v) {
        if (fetchedPartnerPhoto) {
            populatePhoto(partnerPhoto, v);
            return;
        }

        CTRestClient.getPersonById(currentUser.getPartnerId(), new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser person, ParseException e) {
                Person partner = (Person) person;
                partner.getPhotoInBackground(new Person.GetPhotoCallback() {
                    @Override
                    public void done(Bitmap photo) {
                        fetchedPartnerPhoto = true;
                        partnerPhoto = photo;
                        populatePhoto(partnerPhoto, v);
                    }
                });
            }
        });
    }

    private void populatePhoto(Bitmap photo, RoundedImageView v) {
        if (photo != null) {
            v.setImageBitmap(photo);
        } else {
            v.setImageResource(0);
        }
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
