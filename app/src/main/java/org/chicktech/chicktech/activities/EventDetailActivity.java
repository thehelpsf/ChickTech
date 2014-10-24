package org.chicktech.chicktech.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;

public class EventDetailActivity extends Activity {

    Typeface displayFont, displayFont2;
    ImageLoader imageLoader;

    Event event;
    ArrayList<String> girlsGoing;
    ArrayAdapter<String> aGirlsGoing;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Create the TypeFace from the TTF asset
        AssetManager assetMgr = CTApplication.getContext().getAssets();
        displayFont = Typeface.createFromAsset(CTApplication.getContext().getAssets(), "fonts/ostrich-black.ttf");
        displayFont2 = Typeface.createFromAsset(CTApplication.getContext().getAssets(), "fonts/droid.otf");

        setupAnimations();

        imageLoader = ImageLoader.getInstance();
        pb = (ProgressBar) findViewById(R.id.pbLoading);

        person = (Person) ParseUser.getCurrentUser();

        Intent i = getIntent();
        objectID = i.getStringExtra("id");

        // Get access to all the form controls
        tvName = (TextView) findViewById(R.id.tvEventName);
        tvDescription = (TextView) findViewById(R.id.tvEventDescription);
        lvGirlsGoing = (ListView) findViewById(R.id.lvGirlsGoing);
        tvDate  = (TextView) findViewById(R.id.tvDate);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvRsvpStatus = (TextView) findViewById(R.id.tvRSVPStatus);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvDay = (TextView) findViewById(R.id.tvDay);
        tvDateNumber = (TextView) findViewById(R.id.tvDateNumber);
        tvMonth = (TextView) findViewById(R.id.tvMonth);
        tvRSVP = (TextView) findViewById(R.id.tvRSVP);
        tvGetThere = (TextView) findViewById(R.id.tvGetThere);
        tvChat = (TextView) findViewById(R.id.tvChat);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivRsvp = (ImageView) findViewById(R.id.ivRsvp);
        svDetails = (ScrollView) findViewById(R.id.svDetails);
        llMenu = (LinearLayout) findViewById(R.id.llMenu);

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
                    Toast.makeText(EventDetailActivity.this, "Did not get event details", Toast.LENGTH_LONG).show();
                } else {
                    //Log.d("event", "Retrieved the object.");
                    fillTheForm(event);
                    svDetails.startAnimation(animFadeIn);
                    slideMenuIn();
                }
            }
        });



        girlsGoing = new ArrayList<String>();
        girlsGoing.add("Bonnie");
        girlsGoing.add("Steph");
        girlsGoing.add("Linda");
        girlsGoing.add("Liz");
        girlsGoing.add("Brenda");
        girlsGoing.add("Julie");
        girlsGoing.add("Cathy");
        girlsGoing.add("Lydia");

        aGirlsGoing = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, girlsGoing);
        lvGirlsGoing.setAdapter(aGirlsGoing);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupAnimations() {
        // Inflate animation from XML
        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
        final Animation animSlideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right_bounce);
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
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
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
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.slide_in_left);
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.event_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_goto_map:
                gotoMaps();
                return true;
            case R.id.action_rsvp:
                Toast.makeText(this, "RSVP", Toast.LENGTH_SHORT).show();
                updateRSVP();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickRSVP(View button) {
        updateRSVP();
    }

    private void updateRSVP() {
        if (event.isPersonGoing(person)) {
            event.addRsvpNo(person);
        } else {
            event.addRsvpYes(person);
        }
        refreshRsvpOnScreen(true);
    }

    public void onClickGetThere (View button) {
        gotoMaps();
    }

    private void gotoMaps() {
        if (event == null) {
            Toast.makeText(this, "Don't have event object", Toast.LENGTH_SHORT).show();
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void onClickChat (View button) {
        Toast.makeText(this, "Send Chat to Mentor about event", Toast.LENGTH_LONG).show();
    }
}
