package org.chicktech.chicktech.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.wefika.flowlayout.FlowLayout;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.activities.NavDrawerActivity;
import org.chicktech.chicktech.models.Address;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.utils.CTRestClient;

public class ProfileFragment extends Fragment implements EditProfileFragment.EditProfileManager {
    private Person user;
    private boolean isSelf = false;
    boolean isLoading = true;

    private ProgressBar pbLoading;
    private ScrollView svProfile;
    private ImageView imgPhoto;
    private ImageView imgFlower;
    private TextView tvName;
    private TextView tvDetails;
    private LinearLayout llEmail;
    private TextView tvEmail;
    private LinearLayout llPhone;
    private TextView tvPhoneNumber;
    private LinearLayout llAddress;
    private TextView tvAddress;
    private TextView tvWhy;
    private FlowLayout flWhat;
    private Button btnAction;

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
        Person currentUser = (Person) ParseUser.getCurrentUser();
        String passedUid = null;
        if (getArguments() != null) {
            passedUid = getArguments().getString(PeopleFragment.PERSON_ID_KEY);
        }

        if (passedUid == null) {
            Log.d("Profile", "No person ID passed when initializing profile view!");
        }else if (currentUser.getObjectId().equals(passedUid)) {
            isSelf = true;
            user = (Person) ParseUser.getCurrentUser();
        } else {
            CTRestClient.getPersonById(passedUid, new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e != null) {
                        Log.d("Profile", "Error getting user by id: " + e.getMessage());
                    } else {
                        user = (Person)parseUser;

                        // If onCreateView has already been called, but couldn't populate profile
                        // bc there was no user, populate profile now.
                        if (imgPhoto != null) {
                            populateProfile();
                        }
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        pbLoading = (ProgressBar) v.findViewById(R.id.pbLoading);
        svProfile = (ScrollView) v.findViewById(R.id.svProfile);
        imgPhoto = (ImageView) v.findViewById(R.id.imgPhoto);
        imgFlower = (ImageView) v.findViewById(R.id.imgFlower);
        tvName = (TextView) v.findViewById(R.id.etName);
        tvDetails = (TextView) v.findViewById(R.id.tvDetails);
        tvEmail = (TextView) v.findViewById(R.id.tvEmail);
        tvPhoneNumber = (TextView) v.findViewById(R.id.tvPhoneNumber);
        tvAddress = (TextView) v.findViewById(R.id.tvAddress);
        tvWhy = (TextView) v.findViewById(R.id.tvWhy);
        flWhat = (FlowLayout) v.findViewById(R.id.flWhat);
        btnAction = (Button) v.findViewById(R.id.btnAction);
        if (isSelf) {
            btnAction.setBackgroundResource(R.drawable.btn_edit);
            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editProfile();
                }
            });
        } else {
            btnAction.setBackgroundResource(R.drawable.btn_chat);
            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((NavDrawerActivity)getActivity()).selectTopLevelNav(NavDrawerActivity.TopLevelNavs.CHAT);
                }
            });
        }

        llAddress = (LinearLayout) v.findViewById(R.id.llAddress);
        llPhone = (LinearLayout) v.findViewById(R.id.llPhone);
        llEmail = (LinearLayout) v.findViewById(R.id.llEmail);

        populateProfile();
        return v;
    }

    public void editProfile() {
        EditProfileFragment editProfileFragment = EditProfileFragment.newInstance(this);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContent, editProfileFragment);
        ft.addToBackStack("Edit Profile");
        ft.commit();
    }

    @Override
    public Person getPersonToEdit() {
        return user;
    }

    @Override
    public void onProfileSave(Person u) {
        user = u;
        populateProfile();
    }

    private void populateProfile() {
        startLoading();

        if (user == null) {
            // Don't "finishLoading" in this case, b/c this can be called while
            // we're still waiting on user data from the network.
            return;
        }

        user.getPhotoInBackground(new Person.GetPhotoCallback() {
            @Override
            public void done(Bitmap photo) {
                if (photo != null) {
                    imgPhoto.setImageBitmap(photo);
                    imgFlower.setImageResource(R.drawable.flower_full);
                } else {
                    imgFlower.setImageResource(R.drawable.flower_empty);
                    imgPhoto.setImageResource(0);
                }
            }
        });

        tvName.setText(user.getPersonName());
        tvDetails.setText(user.getTagline());

        String s = user.getEmail();
        if (s == null || s.isEmpty()) {
            llEmail.setVisibility(View.GONE);
        } else {
            tvEmail.setText(s);
        }

        s = user.getFormattedPhoneNumber();
        if (s == null || s.isEmpty()) {
            llPhone.setVisibility(View.GONE);
        } else {
            tvPhoneNumber.setText(s);
        }

        tvWhy.setText(user.getInterestReason());

        flWhat.removeAllViews();
        String interests = user.getInterests();
        if (interests != null && !interests.isEmpty()) {
            String[] interestArray = interests.split("\\s*,\\s*");
            for (int i = 0; i < interestArray.length; i++) {
                addInterestView(interestArray[i]);
            }
        }

        user.getAddressInBackground(new Person.GetAddressCallback() {
            @Override
            public void done(Address addr) {
                if (addr == null) {
                    tvAddress.setText("");
                } else {
                    llAddress.setVisibility(View.VISIBLE);
                    tvAddress.setText(addr.toFullString());
                }

                // This is where the profile populating actually ends since everything else is synchronous
                finishLoading();

            }
        });
    }

    private void addInterestView(String interest) {
        TextView tvInterest = (TextView) getActivity().getLayoutInflater().inflate(R.layout.item_interest, flWhat, false);
        tvInterest.setText(interest);
        flWhat.addView(tvInterest);
    }

    private void startLoading() {
        isLoading = true;
        pbLoading.setVisibility(View.VISIBLE);
        svProfile.setVisibility(View.INVISIBLE);
    }

    private void finishLoading() {
        isLoading = false;
        svProfile.setVisibility(View.VISIBLE);
        pbLoading.setVisibility(View.GONE);
    }
}
