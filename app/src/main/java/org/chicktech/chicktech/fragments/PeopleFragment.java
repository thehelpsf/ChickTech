package org.chicktech.chicktech.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.Person;

/**
 * Created by Jing Jin on 10/19/14.
 */
public class PeopleFragment extends Fragment {
    public static final String PERSON_ID_KEY = "personId";

    private FragmentTabHost tabHost;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Context context = getActivity();

        tabHost = new FragmentTabHost(getActivity());
        tabHost.setup(context, getChildFragmentManager(), R.id.flContent);

        Person user = (Person) ParseUser.getCurrentUser();
        Bundle myProfileArgs = new Bundle();
        myProfileArgs.putString(PERSON_ID_KEY, user.getObjectId());
        tabHost.addTab(tabHost.newTabSpec("my profile").setIndicator(context.getString(R.string.my_profile_tab_label)),
                ProfileFragment.class, myProfileArgs);

        Bundle mentorProfileArgs = new Bundle();
        mentorProfileArgs.putString(PERSON_ID_KEY, user.getPartnerId());
        tabHost.addTab(tabHost.newTabSpec("my match").setIndicator(user.getRole() == Person.Role.MENTOR ? context.getString(R.string.student_profile_tab_label)
                        : context.getString(R.string.mentor_profile_tab_label)),
                ProfileFragment.class, mentorProfileArgs);
        return tabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tabHost = null;
    }
}
