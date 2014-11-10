package org.chicktech.chicktech.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
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

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

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
    ProgressBar pbRSVPing;
    Person person;
    ScrollView svDetails;
    LinearLayout llMenu;
    Button btnSendRsvpReminder;
    Button btnSendGoReminder;
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
        if (objectID == null || objectID.length() < 1) {
            Toast.makeText(getActivity(), "Invalid event ID", Toast.LENGTH_LONG).show();
        }
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
        pbRSVPing = (ProgressBar) view.findViewById(R.id.pbRSVPing);

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

        btnSendRsvpReminder = (Button) view.findViewById(R.id.btnPushRsvpReminder);
        btnSendGoReminder = (Button) view.findViewById(R.id.btnPushGoReminder);

        if (person.getRole() == Person.Role.ORGANIZER || person.getRole() == Person.Role.MENTOR) {
            btnSendRsvpReminder.setVisibility(View.VISIBLE);
            btnSendGoReminder.setVisibility(View.VISIBLE);
        } else {
            btnSendRsvpReminder.setVisibility(View.GONE);
            btnSendGoReminder.setVisibility(View.GONE);
        }

        setupClickListeners();

        //tvName.setTypeface(displayFont);
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
            public void done(ParseObject eventReceived, ParseException e) {
                pb.setVisibility(ProgressBar.INVISIBLE);
                if (eventReceived == null) {
                    Toast.makeText(getLocalContext(), "Did not get event details", Toast.LENGTH_LONG).show();
                } else {
                    //Log.d("event", "Retrieved the object.");
                    fillTheForm(eventReceived);
                    svDetails.startAnimation(animFadeIn);
                    slideMenuIn(false);
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

        btnSendRsvpReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRsvpPushReminder();
                Toast.makeText(getActivity(), "RSVP Notifications sent to students and mentors", Toast.LENGTH_SHORT).show();
            }
        });

        btnSendGoReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGoPushReminder();
                Toast.makeText(getActivity(), "GO Notifications sent to students and mentors", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendRsvpPushReminder() {
        CTRestClient.sendRsvpReminder(event);
    }

    private void sendGoPushReminder() {
        CTRestClient.sendGoReminder(event);
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


    public void slideMenuIn(boolean doAnimation) {

        if (doAnimation) {
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
        } else {
            llMenu.setVisibility(View.VISIBLE);
        }
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


    // TODO: Refactor RSVP code to use separate set of objects instead of having RSVP lists within Event object.
    private boolean isRsvpIng = false;
    private void updateRSVP() {
        if (isRsvpIng) {
            return;
        }

        isRsvpIng = true;
        pbRSVPing.setVisibility(View.VISIBLE);

        CTRestClient.getEventByID(false, event.getObjectId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                pbRSVPing.setVisibility(View.INVISIBLE);
                event = (Event) object;
                if (e == null) {
                    if (event.isPersonGoing(person)) {
                        event.addRsvpNo(person);
                    } else {
                        event.addRsvpYes(person);
                    }
                    refreshRsvpOnScreen(true);
                }
                isRsvpIng = false;
            }
        });
    }


    private void gotoMaps() {
        if (event == null) {
            Toast.makeText(getLocalContext(), "Don't have event object", Toast.LENGTH_SHORT).show();
            return;
        }

        String zoomLevel = "23";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        String encodedQuery = Uri.encode(event.getNavString());
        String data = "geo:0,0?q=" + encodedQuery;
        if (zoomLevel != null) {
            data = String.format("%s&z=%s", data, zoomLevel);// this does not seem to be working
        }
        intent.setData(Uri.parse(data));
        startActivity(intent);
        getLocalContext().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    public void sendChat() {
        String cannedMessage = "This event would be a great place to learn about it. Click this link to check it out!";
        CTRestClient.sendChatMessage(person.getPartnerId(),person.getObjectId(),cannedMessage,new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject chatMessage, ParseException e) {
                chatMessage.pinInBackground();
                Crouton.showText(getActivity(), "You recommended this event via chat", Style.INFO);
                delayThenSendMessage();
            }
        });
    }


    private void delayThenSendMessage() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String url = new String("chicktech://"+event.getObjectId());

                CTRestClient.sendChatMessage(person.getPartnerId(), person.getObjectId(), url, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject chatMessage, ParseException e) {
                        chatMessage.pinInBackground();
                    }
                });
            }
        }, 2000);
    }
}
