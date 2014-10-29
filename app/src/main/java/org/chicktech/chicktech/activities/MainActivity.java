package org.chicktech.chicktech.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.fragments.AboutFragment;
import org.chicktech.chicktech.fragments.ChatFragment;
import org.chicktech.chicktech.fragments.EventsFragment;
import org.chicktech.chicktech.fragments.PeopleFragment;
import org.chicktech.chicktech.models.ChatMessage;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.utils.BitmapUtils;
import org.chicktech.chicktech.utils.CTRestClient;
import org.chicktech.chicktech.utils.CameraLaunchingActivity;
import org.chicktech.chicktech.utils.CameraLaunchingListener;
import org.chicktech.chicktech.utils.FileSystemUtils;
import org.chicktech.chicktech.utils.FragmentNavigationDrawer;

import java.util.List;


public class MainActivity extends ActionBarActivity implements CameraLaunchingActivity {
    private static final String PHOTO_FILENAME = "CTphoto.jpg";
    private FragmentNavigationDrawer dlDrawer;
    private CameraLaunchingListener cameraListener;
    private BroadcastReceiver mChatReceiver;
    private BroadcastReceiver mLinkReceiver;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i = getIntent();
        eventID = i.getStringExtra("id");

        // Find our drawer view
        dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
        // Setup drawer view
        dlDrawer.setupDrawerConfiguration(findViewById(R.id.rlDrawerContainer), (ListView) findViewById(R.id.lvDrawer),
                R.layout.drawer_nav_item, R.id.flContent);
        // Add nav items
        dlDrawer.addNavItem("Events", R.drawable.ic_events_selector, "Events", EventsFragment.class);
        dlDrawer.addNavItem("People", R.drawable.ic_people_selector, "People", PeopleFragment.class);
        dlDrawer.addNavItem("Chat", R.drawable.ic_chat_selector, "Chat", ChatFragment.class);
        dlDrawer.addNavItem("About", R.drawable.ic_about_selector, "About ChickTech", AboutFragment.class);
        // Select default
        if (savedInstanceState == null) {
            dlDrawer.selectDrawerItem(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        IntentFilter chatIntentFilter = new IntentFilter(
                "android.intent.action.CHAT");

        IntentFilter urlFilter = new IntentFilter(
                "com.chicktech.LOAD_URL");

        mChatReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                showChatToast();

                CTRestClient.getLastChatMessage(intent.getStringExtra("toPersonID"), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            ChatMessage chatMessage = (ChatMessage) parseObject;
                            chatMessage.pinInBackground("ChatMessage", new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    Intent intent= new Intent("com.chicktech.CHAT_MESSAGE");
                                    getBaseContext().sendBroadcast(intent);
                                }
                            });
                        }
                    });
                }


        };


        mLinkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String url = intent.getStringExtra("url");
                goToEventDetail(url);
            }
        };


        //registering our receiver
        this.registerReceiver(mChatReceiver, chatIntentFilter);
        this.registerReceiver(mLinkReceiver, urlFilter);

        //fetch all chat messages upon app open
        CTRestClient.getAllChatMessages((Person)ParseUser.getCurrentUser(), new FindCallback<ParseObject>() {
            public void done(List<ParseObject> messages, ParseException e) {
                if (e == null) {
                } else {
                    Log.d("Chat Messages", "Error: " + e.getMessage());
                }
            }
        });

        if (eventID != null) {
            goToEventDetail(eventID);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mChatReceiver != null) {
            this.unregisterReceiver(mChatReceiver);
        }
        if (mLinkReceiver != null) {
            this.unregisterReceiver(mLinkReceiver);
        }
    }


    private void goToEventDetail(String id) {
        Intent i = new Intent(this, EventDetailActivity.class);
        i.putExtra("id", id);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    // Temporary creation of models in parse for testing
    private void showChatToast(){
        if (!dlDrawer.isChatSelectedIndex()) {
            Toast.makeText(this.getApplicationContext(), "New Message", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content
        if (dlDrawer.isDrawerOpen()) {
            // Uncomment to hide menu items
            // menu.findItem(R.id.mi_test).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Uncomment to inflate menu items to Action Bar
        // inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (dlDrawer.getDrawerToggle().onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        dlDrawer.getDrawerToggle().syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        dlDrawer.getDrawerToggle().onConfigurationChanged(newConfig);
    }

    @Override
    public void launchCamera(CameraLaunchingListener listener) {
        if (cameraListener != null) {
            Log.d("MainActivity", "Warn: Already launched camera, ignoring camera launch request");
            return;
        }
        cameraListener = listener;
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.putExtra(MediaStore.EXTRA_OUTPUT, FileSystemUtils.getPhotoFileUri(PHOTO_FILENAME));
        startActivityForResult(i, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (cameraListener == null) {
                Log.d("MainActivity", "Warn: expected cameraListner but it's null");
            } else if (resultCode != RESULT_OK) {
                cameraListener.onCameraFailure(resultCode);
            } else {
                try {
                    Uri takenPhotoUri = FileSystemUtils.getPhotoFileUri(PHOTO_FILENAME);
                    Bitmap takenImage = BitmapUtils.rotateBitmapOrientation(takenPhotoUri.getPath());
                    cameraListener.onCameraSuccess(takenImage);
                } catch (Exception e) {
                    Log.d("MainActivity", "Error rotating bitmap: " + e.getMessage());
                    cameraListener.onCameraFailure(0);
                }
            }
            cameraListener = null;
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
