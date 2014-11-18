package org.chicktech.chicktech.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.models.Person;

public class NoPartnerFragment extends Fragment {
    public NoPartnerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_no_partner, container, false);
        TextView tvNoPartner = (TextView) v.findViewById(R.id.tvNoPartner);

        Person user = (Person) ParseUser.getCurrentUser();
        int stringId = user.getRole() == Person.Role.MENTOR ? R.string.no_student_message : R.string.no_mentor_message;
        tvNoPartner.setText(getString(stringId));

        return v;
    }

}
