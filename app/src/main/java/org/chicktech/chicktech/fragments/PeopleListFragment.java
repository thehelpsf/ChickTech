package org.chicktech.chicktech.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.chicktech.chicktech.R;

/**
 * Created by paul on 10/24/14.
 */
public class PeopleListFragment extends Fragment {


    public PeopleListFragment () {

    }


    public static PeopleListFragment newInstance () {
        PeopleListFragment fragment = new PeopleListFragment();
//        Bundle args = new Bundle();
//        args.putString("id", eventID);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_people_list, container, false);

        return view;
    }
}
