package org.chicktech.chicktech.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.Address;
import org.chicktech.chicktech.models.Organization;
import org.chicktech.chicktech.utils.CTRestClient;

public class AboutFragment extends Fragment {

    private Organization org;

    private TextView tvDescription;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private TextView tvAddress;

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        return fragment;
    }
    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CTRestClient.getOrganizationInfo(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null) {
                    Log.d("about", "Error getting org: " + e.getMessage());
                } else if (parseObject == null) {
                    Log.d("about", "No organization exists!");
                } else {
                    org = (Organization)parseObject;
                    // Populate views if onCreateView has already been called.
                    if (tvEmail != null) {
                        populateViews();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_about, container, false);

        tvDescription = (TextView)v.findViewById(R.id.tvDescription);
        tvEmail = (TextView)v.findViewById(R.id.tvEmail);
        tvPhoneNumber = (TextView)v.findViewById(R.id.tvPhoneNumber);
        tvAddress = (TextView)v.findViewById(R.id.tvAddress);

        populateViews();

        return v;
    }

    void populateViews() {
        if (org == null) {
            return;
        }

        tvDescription.setText(org.getDescription());
        tvEmail.setText(org.getEmail());
        tvPhoneNumber.setText(org.getPhoneNumber());
        org.getAddressInBackground(new Organization.GetAddressCallback() {
            @Override
            public void done(Address addr) {
                if (addr != null) {
                    tvAddress.setText(addr.toFullString());
                } else {
                    tvAddress.setText("");
                }
            }
        });
    }
}
