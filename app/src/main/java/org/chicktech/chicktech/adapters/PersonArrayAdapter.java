package org.chicktech.chicktech.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.Person;

import java.util.List;

/**
 * Created by paul on 10/26/14.
 */
public class PersonArrayAdapter extends ArrayAdapter<ParseUser> {
    private ViewHolder viewHolder;

    // View lookup cache
    private static class ViewHolder {
        TextView tvName;
        ImageView imgPhoto;
    }

    public PersonArrayAdapter(Context context, List<ParseUser> people) {
        super(context, R.layout.item_person, people);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Person user = (Person)getItem(position);
        if (user == null) {
            return convertView;
        }

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_person, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(user.getPersonName());

        // TODO: FIX, This image always shows up in the last slot. The views are
        // changing while the images is being loaded? Load the image well before this
        // code is called. Tags?
//        user.getPhotoInBackground(new Person.GetPhotoCallback() {
//            @Override
//            public void done(Bitmap photo) {
//                if (photo != null) {
//                    viewHolder.imgPhoto.setImageBitmap(photo);
//                } else {
//                    viewHolder.imgPhoto.setImageResource(0);
//                }
//            }
//        });

        return convertView;
    }
}
