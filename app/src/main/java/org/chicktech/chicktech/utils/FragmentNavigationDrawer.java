package org.chicktech.chicktech.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.adapters.DrawerArrayAdapter;
import org.chicktech.chicktech.models.DrawerMenuItem;

import java.util.ArrayList;

/**
 * Created by kenanpulak on 10/14/14.
 */
public class FragmentNavigationDrawer extends DrawerLayout {
    private ActionBarDrawerToggle drawerToggle;
    private View vDrawerContainer;
    private ListView lvDrawer;
    private DrawerArrayAdapter drawerAdapter;
    private ArrayList<FragmentNavItem> drawerNavItems;
    private int drawerContainerRes;
    private Handler handler;
    private Runnable switchFragmentsTask;
    private int selectedIndex = 0;


    public FragmentNavigationDrawer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FragmentNavigationDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentNavigationDrawer(Context context) {
        super(context);
    }

    // setupDrawerConfiguration((ListView) findViewById(R.id.lvDrawer), R.layout.drawer_list_item, R.id.flContent);
    public void setupDrawerConfiguration(View drawerContainer, ListView drawerListView, int drawerItemRes, int drawerContainerRes) {
        handler = new Handler();
        switchFragmentsTask = new Runnable() {
            @Override
            public void run() {
                showSelectedFragment();
            }
        };

        vDrawerContainer = drawerContainer;

        // Setup navigation items array
        drawerNavItems = new ArrayList<FragmentNavigationDrawer.FragmentNavItem>();
        // Set the adapter for the list view
        drawerAdapter = new DrawerArrayAdapter(getActivity(), new ArrayList<DrawerMenuItem>());
        this.drawerContainerRes = drawerContainerRes;
        // Setup drawer list view and related adapter
        lvDrawer = drawerListView;
        lvDrawer.setAdapter(drawerAdapter);
        // Setup item listener
        lvDrawer.setOnItemClickListener(new FragmentDrawerItemListener());
        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        drawerToggle = setupDrawerToggle();
        setDrawerListener(drawerToggle);
        // Set the overlay color over the main content
        setScrimColor(getResources().getColor(R.color.white_transparent));
        // Setup action buttons
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    // addNavItem("First", "First Fragment", FirstFragment.class)
    public void addNavItem(String navTitle, int navIconResourceId, String windowTitle, Class<? extends Fragment> fragmentClass) {
        drawerAdapter.add(new DrawerMenuItem(navTitle, navIconResourceId));
        drawerNavItems.add(new FragmentNavItem(windowTitle, fragmentClass));
    }

    private void showSelectedFragment () {
        // Create a new fragment and specify the planet to show based on
        // position
        FragmentNavItem navItem = drawerNavItems.get(selectedIndex);
        Fragment fragment = null;
        try {
            fragment = navItem.getFragmentClass().newInstance();
            Bundle args = navItem.getFragmentArgs();
            if (args != null) {
                fragment.setArguments(args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        ft.replace(drawerContainerRes, fragment, navItem.getTitle());
        ft.commit();

        // Set the title
        setTitle(navItem.getTitle());
    }

    /** Swaps fragments in the main content view */
    public void selectDrawerItem(int position) {
        selectedIndex = position;
        // Highlight the selected item, and close the drawer
        lvDrawer.setItemChecked(position, true);
        if (isDrawerOpen()) {
            closeDrawer(vDrawerContainer);
            // Swich fragments after drawer has closed
            handler.postDelayed(switchFragmentsTask, 200);
        } else {
            switchFragmentsTask.run();
        }
    }

    public Boolean isChatSelectedIndex(){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("Chat"); //Fragment Title is the Tag

        if (fragment != null){
            if (fragment.isVisible()){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }


    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    private FragmentActivity getActivity() {
        return (FragmentActivity) getContext();
    }

    private ActionBar getSupportActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    private void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    private class FragmentDrawerItemListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectDrawerItem(position);
        }
    }

    private class FragmentNavItem {
        private Class<? extends Fragment> fragmentClass;
        private String title;
        private Bundle fragmentArgs;

        public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass) {
            this(title, fragmentClass, null);
        }

        public FragmentNavItem(String title, Class<? extends Fragment> fragmentClass, Bundle args) {
            this.fragmentClass = fragmentClass;
            this.fragmentArgs = args;
            this.title = title;
        }

        public Class<? extends Fragment> getFragmentClass() {
            return fragmentClass;
        }

        public String getTitle() {
            return title;
        }

        public Bundle getFragmentArgs() {
            return fragmentArgs;
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(getActivity(), /* host Activity */
                this, /* DrawerLayout object */
                R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                // setTitle(getCurrentTitle());
                // call onPrepareOptionsMenu()
                getActivity().supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // setTitle("Navigate");
                // call onPrepareOptionsMenu()
                getActivity().supportInvalidateOptionsMenu();
            }
        };
    }

    public boolean isDrawerOpen() {
        return isDrawerOpen(vDrawerContainer);
    }


}
