package org.chicktech.chicktech.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.application.CTApplication;
import org.chicktech.chicktech.models.Event;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.utils.CTRestClient;

/**
 * Created by paul on 10/24/14.
 */
public class EventDetailsFragment extends Fragment {

    Typeface displayFont, displayFont2;
    ImageLoader imageLoader;

    Event event;
    ListView lvGirlsGoing;
    TextView tvName;
    TextView tvDescription;
    TextView tvDate;
    TextView tvLocation;
    TextView tvRsvpStatus;
    TextView tvTime;
    TextView tvDay;
    TextView tvDateNumber;
    TextView tvMonth;
    TextView tvRSVP;
    TextView tvGetThere;
    TextView tvChat;
    ImageView ivImage;
    ImageView ivRsvp;
    ProgressBar pb;
    Person person;
    ScrollView svDetails;
    LinearLayout llMenu;
    String objectID;

    private Animation animFadeIn;


    public EventDetailsFragment () {

    }

    public static EventDetailsFragment newInstance(String eventID) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putString("id", eventID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        objectID = getArguments().getString("id");
        // TODO: react to invalid objectID
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        // Create the TypeFace from the TTF asset
        AssetManager assetMgr = CTApplication.getContext().getAssets();
        displayFont = Typeface.createFromAsset(CTApplication.getContext().getAssets(), "fonts/ostrich-black.ttf");
        displayFont2 = Typeface.createFromAsset(CTApplication.getContext().getAssets(), "fonts/droid.otf");

        setupAnimations();

        imageLoader = ImageLoader.getInstance();
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);

        person = (Person) ParseUser.getCurrentUser();

        // Get access to all the form controls
        tvName = (TextView) view.findViewById(R.id.tvEventName);
        tvDescription = (TextView) view.findViewById(R.id.tvEventDescription);
        lvGirlsGoing = (ListView) view.findViewById(R.id.lvGirlsGoing);
        tvDate  = (TextView) view.findViewById(R.id.tvDate);
        tvLocation = (TextView) view.findViewById(R.id.tvLocation);
        tvRsvpStatus = (TextView) view.findViewById(R.id.tvRSVPStatus);
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        tvDay = (TextView) view.findViewById(R.id.tvDay);
        tvDateNumber = (TextView) view.findViewById(R.id.tvDateNumber);
        tvMonth = (TextView) view.findViewById(R.id.tvMonth);
        tvRSVP = (TextView) view.findViewById(R.id.tvRSVP);
        tvGetThere = (TextView) view.findViewById(R.id.tvGetThere);
        tvChat = (TextView) view.findViewById(R.id.tvChat);
        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        ivRsvp = (ImageView) view.findViewById(R.id.ivRsvp);
        svDetails = (ScrollView) view.findViewById(R.id.svDetails);
        llMenu = (LinearLayout) view.findViewById(R.id.llMenu);
        
        setupClickListeners();

        tvName.setTypeface(displayFont);
        tvDay.setTypeface(displayFont);
        tvDateNumber.setTypeface(displayFont);
        tvMonth.setTypeface(displayFont);
        tvTime.setTypeface(displayFont);
        tvRsvpStatus.setTypeface(displayFont);
        tvRSVP.setTypeface(displayFont);
        tvGetThere.setTypeface(displayFont);
        tvChat.setTypeface(displayFont);

        pb.setVisibility(ProgressBar.VISIBLE);
        CTRestClient.getEventByID(CTRestClient.QUERY_LOCAL, objectID, new GetCallback<ParseObject>() {
            public void done(ParseObject event, ParseException e) {
                pb.setVisibility(ProgressBar.INVISIBLE);
                if (event == null) {
                    Toast.makeText(getLocalContext(), "Did not get event details", Toast.LENGTH_LONG).show();
                } else {
                    //Log.d("event", "Retrieved the object.");
                    fillTheForm(event);
                    svDetails.startAnimation(animFadeIn);
                    slideMenuIn();
                }
            }
        });

        return view;
    }


    private void setupClickListeners() {
        tvRSVP.setClickable(true);
        tvRSVP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRSVP();
            }
        });

        tvGetThere.setClickable(true);
        tvGetThere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMaps();
            }
        });

        tvChat.setClickable(true);
        tvChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendChat();
            }
        });
    }


    private Activity getLocalContext () {
        return getActivity();
    }


    private void setupAnimations() {
        // Inflate animation from XML
        animFadeIn = AnimationUtils.loadAnimation(getLocalContext(), R.anim.fade_in);
        animFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                svDetails.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    private void fillTheForm(ParseObject object) {
        event = (Event) object;
        if (event != null) {
            tvName.setText(event.getTitle());
            tvDescription.setText(Html.fromHtml(event.getDescription()));
            tvDate.setText(event.getStartDateString());
            tvLocation.setText(event.getAddressString());
            tvDay.setText(event.getDayOfWeek());
            tvDateNumber.setText(event.getDateNumber());
            tvMonth.setText(event.getMonth());
            tvTime.setText(event.getTimeString());

            String imageUrl = event.getImageURL();
            if (imageUrl != null && imageUrl != "") {
                imageLoader.displayImage(imageUrl, ivImage);
            } else {
                ivImage.setImageResource(android.R.color.transparent);
            }

            refreshRsvpOnScreen(false);
        }
    }


    private void refreshRsvpOnScreen(boolean performAnimation) {

        tvRSVP.setText(event.getRsvpStatusStringShort(person));

        // Inflate animation from XML
        final Animation animSlideIn = AnimationUtils.loadAnimation(getLocalContext(), R.anim.slide_in_right_bounce);
        animSlideIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        // Inflate animation from XML
        Animation slideOut = AnimationUtils.loadAnimation(getLocalContext(), R.anim.slide_out_right);
        // Setup listeners (optional)
        slideOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Fires when animation starts
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tvRsvpStatus.setText(event.getRsvpStatusString(person));
                //Toast.makeText(EventDetailActivity.this, "done", Toast.LENGTH_SHORT).show();
                tvRsvpStatus.startAnimation(animSlideIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        if (performAnimation) {
            tvRsvpStatus.startAnimation(slideOut);
        } else {
            tvRsvpStatus.setText(event.getRsvpStatusString(person));
        }
    }


    public void slideMenuIn() {
        // Inflate animation from XML
        Animation anim = AnimationUtils.loadAnimation(getLocalContext(), R.anim.slide_in_left);
        // Setup listeners (optional)
        anim.setDuration(1000L);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                llMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        llMenu.startAnimation(anim);
    }


    private void onBackPressed() {
        // TODO: ask parent activity to process back button?
        Toast.makeText(getLocalContext(), "back", Toast.LENGTH_SHORT).show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_goto_map:
                gotoMaps();
                return true;
            case R.id.action_rsvp:
                updateRSVP();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateRSVP() {
        if (event.isPersonGoing(person)) {
            event.addRsvpNo(person);
        } else {
            event.addRsvpYes(person);
        }
        refreshRsvpOnScreen(true);
    }


    private void gotoMaps() {
        if (event == null) {
            Toast.makeText(getLocalContext(), "Don't have event object", Toast.LENGTH_SHORT).show();
            return;
        }

        String zoomLevel = "18";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String data = "geo:0,0?q=" + event.getAddressString();
        if (zoomLevel != null) {
            data = String.format("%s?z=%s", data, zoomLevel);
        }
        intent.setData(Uri.parse(data));
        startActivity(intent);
        getLocalContext().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    public void sendChat() {
        Toast.makeText(getLocalContext(), "Send Chat to Mentor about event", Toast.LENGTH_LONG).show();
        PeopleListFragment fragment = PeopleListFragment.newInstance();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        ft.replace(R.id.flContent, fragment);
        ft.addToBackStack("People");
        ft.commit();

    }
}
