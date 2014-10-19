package org.chicktech.chicktech.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.fragments.ChatFragment;
import org.chicktech.chicktech.fragments.EventsFragment;
import org.chicktech.chicktech.fragments.ProfileFragment;
import org.chicktech.chicktech.utils.BitmapUtils;
import org.chicktech.chicktech.utils.CameraLaunchingActivity;
import org.chicktech.chicktech.utils.CameraLaunchingListener;
import org.chicktech.chicktech.utils.FileSystemUtils;
import org.chicktech.chicktech.utils.FragmentNavigationDrawer;


public class MainActivity extends ActionBarActivity implements CameraLaunchingActivity {
    private static final String PHOTO_FILENAME = "CTphoto.jpg";

    private FragmentNavigationDrawer dlDrawer;

    private CameraLaunchingListener cameraListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find our drawer view
        dlDrawer = (FragmentNavigationDrawer) findViewById(R.id.drawer_layout);
        // Setup drawer view
        dlDrawer.setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer),
                R.layout.drawer_nav_item, R.id.flContent);
        // Add nav items
        dlDrawer.addNavItem("Events", "Events", EventsFragment.class);
        dlDrawer.addNavItem("Profile", "Profile", ProfileFragment.class);
        dlDrawer.addNavItem("Chat", "Chat", ChatFragment.class);
        // Select default
        if (savedInstanceState == null) {
            dlDrawer.selectDrawerItem(0);
        }

        // Temporary creation of models in parse for testing
        sendModelsToParse();

    }

    // Temporary creation of models in parse for testing
    private void sendModelsToParse() {

//        Address tempAddress = new Address("Portland State University",
//                "1825 SW Broadway",
//                "Computer Lab EB 325 West",
//                "Portland",
//                "OR",
//                "97201"
//                );
//        tempAddress.saveInBackground();

        //ChatMessage tempChatMessage = new ChatMessage();
        //Event tempEvent = new Event("Event One", "This is the first event of the year.", new Date(), new Date());
        //Person tempPerson = new Person();

        //RSVP tempRSVP = new RSVP();

        //tempChatMessage.saveInBackground();
        //tempEvent.saveInBackground();
        //tempPerson.saveInBackground();
        //tempRSVP.saveInBackground();
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
