package org.chicktech.chicktech.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.wefika.flowlayout.FlowLayout;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.Address;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.utils.CTRestClient;

import java.util.ArrayList;

public class ProfileFragment extends Fragment implements EditProfileFragment.OnSaveListener {
    private Person user;

    private ImageView imgPhoto;
    private TextView tvName;
    private TextView tvDetails;
    private TextView tvEmail;
    private TextView tvPhoneNumber;
    private TextView tvAddress;
    private TextView tvWhy;
    private FlowLayout flWhat;
    private Button btnEdit;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        //TODO: Add args here
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //TODO: Retrieve args here
        }
        user = (Person) ParseUser.getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        imgPhoto = (ImageView) v.findViewById(R.id.imgPhoto);
        tvName = (TextView) v.findViewById(R.id.etName);
        tvDetails = (TextView) v.findViewById(R.id.tvDetails);
        tvEmail = (TextView) v.findViewById(R.id.tvEmail);
        tvPhoneNumber = (TextView) v.findViewById(R.id.tvPhoneNumber);
        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        tvWhy = (TextView) v.findViewById(R.id.tvWhy);
        flWhat = (FlowLayout) v.findViewById(R.id.flWhat);
        btnEdit = (Button) v.findViewById(R.id.btnEdit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        populateProfile();
        return v;
    }

    public void editProfile() {
        EditProfileFragment editProfileFragment = EditProfileFragment.newInstance();
        editProfileFragment.setListener(this);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContent, editProfileFragment);
        ft.addToBackStack("Edit Profile");
        ft.commit();
    }

    @Override
    public void onProfileSave(Person u) {
        user = u;
        populateProfile();
    }

    private void populateProfile() {
        user.getPhotoInBackground(new Person.GetPhotoCallback() {
            @Override
            public void done(Bitmap photo) {
                if (photo != null) {
                    imgPhoto.setImageBitmap(photo);
                } else {
                    imgPhoto.setImageResource(0);
                }
            }
        });

        tvName.setText(user.getPersonName());
        tvDetails.setText(user.getTagline());
        tvEmail.setText(user.getEmail());
        tvPhoneNumber.setText(user.getPhoneNumber());
        user.getAddressInBackground(new Person.GetAddressCallback() {
            @Override
            public void done(Address addr) {
                if (addr == null) {
                    tvAddress.setText("");
                } else {
                    tvAddress.setText(addr.getAddress1()
                            + "\n" + addr.getAddress2()
                            + "\n" + addr.getCity()
                            + ", " + addr.getState()
                            + " " + addr.getZipcode());
                }
            }
        });
        tvWhy.setText(user.getInterestReason());

        flWhat.removeAllViews();
        String interests = user.getInterests();
        if (interests != null && !interests.isEmpty()) {
            String[] interestArray = interests.split("\\s*,\\s*");
            for (int i = 0; i < interestArray.length; i++) {
                addInterestView(interestArray[i]);
            }
        }
    }

    private void addInterestView(String interest) {
        TextView tvInterest = (TextView) getActivity().getLayoutInflater().inflate(R.layout.item_interest, flWhat, false);
        tvInterest.setText(interest);
        flWhat.addView(tvInterest);
    }
}
