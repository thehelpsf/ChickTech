package org.chicktech.chicktech.adapters;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.application.CTApplication;
import org.chicktech.chicktech.models.Event;
import org.chicktech.chicktech.models.Person;

import java.util.List;

/**
 * Created by kenanpulak on 10/14/14.
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {
    Typeface displayFont, displayFont2;
    ImageLoader imageLoader;
    ViewHolder viewHolder;

    // View lookup cache
    private static class ViewHolder {
        TextView tvName;
        TextView tvDescription;
        TextView tvDate;
        TextView tvLocation;
        TextView tvRsvpStatus;
        TextView tvDay;
        TextView tvDateNumber;
        TextView tvMonth;
        TextView tvTime;
        ImageView ivImage;
        RelativeLayout rlBadge;
        RelativeLayout rlDay;
        RelativeLayout rlBody;
        RelativeLayout rlEventName;
        Person person;
    }

    public EventArrayAdapter(Context context, List<Event> events) {

        super(context, R.layout.item_event, events);

        // Create the TypeFace from the TTF asset
        AssetManager assetMgr = CTApplication.getContext().getAssets();
        displayFont = Typeface.createFromAsset(CTApplication.getContext().getAssets(), "fonts/ostrich-black.ttf");
        displayFont2 = Typeface.createFromAsset(CTApplication.getContext().getAssets(), "fonts/droid.otf");

        imageLoader = ImageLoader.getInstance();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvEventName);
            viewHolder.tvDescription = (TextView) convertView.findViewById(R.id.tvEventDescription);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            viewHolder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
            viewHolder.tvDateNumber = (TextView) convertView.findViewById(R.id.tvDateNumber);
            viewHolder.tvMonth = (TextView) convertView.findViewById(R.id.tvMonth);
            viewHolder.tvLocation = (TextView) convertView.findViewById(R.id.tvLocation);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.tvRsvpStatus = (TextView) convertView.findViewById(R.id.tvRSVPStatus);
            viewHolder.ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
            viewHolder.person = (Person) ParseUser.getCurrentUser();

            //viewHolder.tvName.setTypeface(displayFont);
            viewHolder.tvDay.setTypeface(displayFont);
            viewHolder.tvDateNumber.setTypeface(displayFont);
            viewHolder.tvMonth.setTypeface(displayFont);
            viewHolder.tvRsvpStatus.setTypeface(displayFont);

            viewHolder.rlBadge = (RelativeLayout) convertView.findViewById(R.id.rlBadge);
            viewHolder.rlDay = (RelativeLayout) convertView.findViewById(R.id.rlDay);
            viewHolder.rlEventName = (RelativeLayout) convertView.findViewById(R.id.rlEventName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.rlBadge.getLayoutParams();
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) viewHolder.rlDay.getLayoutParams();
        RelativeLayout.LayoutParams paramsName = (RelativeLayout.LayoutParams) viewHolder.rlEventName.getLayoutParams();

        if (position % 2 == 0) {
            // even
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params2.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

            viewHolder.tvName.setGravity(Gravity.CENTER_HORIZONTAL);

            paramsName.removeRule(RelativeLayout.LEFT_OF);
            paramsName.addRule(RelativeLayout.RIGHT_OF, R.id.rlDay);
            paramsName.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramsName.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        } else {
            params.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params2.removeRule(RelativeLayout.ALIGN_PARENT_LEFT);
            params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

            viewHolder.tvName.setGravity(Gravity.CENTER_HORIZONTAL);

            paramsName.removeRule(RelativeLayout.RIGHT_OF);
            paramsName.addRule(RelativeLayout.LEFT_OF, R.id.rlDay);
            paramsName.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsName.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        }

        viewHolder.rlBadge.setLayoutParams(params); //causes layout update
        viewHolder.rlDay.setLayoutParams(params2);


        String imageUrl = event.getImageURL();
        if (imageUrl != null && imageUrl != "") {
            imageLoader.displayImage(imageUrl, viewHolder.ivImage);
        } else {
            viewHolder.ivImage.setImageResource(android.R.color.transparent);
        }


        viewHolder.tvName.setText(event.getTitle());
        viewHolder.tvDescription.setText(Html.fromHtml(event.getDescription()));
        viewHolder.tvDate.setText(event.getStartDate().toString());
        viewHolder.tvLocation.setText(event.getAddressString());
        viewHolder.tvRsvpStatus.setText(event.getRsvpStatusLabelShort(viewHolder.person));
        viewHolder.tvDay.setText(event.getDayOfWeek());
        viewHolder.tvDateNumber.setText(event.getDateNumber());
        viewHolder.tvMonth.setText(event.getMonth());
        viewHolder.tvTime.setText(event.getTimeString());

        return convertView;
    }
}
