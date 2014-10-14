package org.chicktech.chicktech.fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.chicktech.chicktech.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class EditProfileFragment extends Fragment {

    private Button btnSave;
    private OnSaveListener listener;

    public interface OnSaveListener {
        public void onProfileSave();
    }

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        //TODO: Add args here
        fragment.setArguments(args);
        return fragment;
    }
    public EditProfileFragment() {
        // Required empty public constructor
    }

    public OnSaveListener getListener() {
        return listener;
    }

    public void setListener(OnSaveListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //TODO: Retrieve args here
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        btnSave = (Button) v.findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onProfileSave();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        return v;
    }


}
