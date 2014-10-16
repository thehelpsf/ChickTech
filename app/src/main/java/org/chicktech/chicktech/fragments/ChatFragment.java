package org.chicktech.chicktech.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.adapters.ChatArrayAdapter;
import org.chicktech.chicktech.models.ChatMessage;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    Button btnSend;
    EditText etMessage;
    ListView lvMessages;
    ChatArrayAdapter aMessages;
    ArrayList<ChatMessage> messages;

    public static ChatFragment newInstance() {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        //TODO: Add args here
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
        // Required empty public constructor
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
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        btnSend = (Button) v.findViewById(R.id.btnSend);
        etMessage = (EditText) v.findViewById(R.id.etMessage);
        lvMessages = (ListView) v.findViewById(R.id.lvMessages);

        messages = new ArrayList<ChatMessage>();
        ChatMessage testMessage = new ChatMessage();
        testMessage.setFromPersonID("49832");
        testMessage.setToPersonID("40345");
        testMessage.setMessage("hey");
        messages.add(testMessage);
        ChatMessage anotherMessage = new ChatMessage();
        anotherMessage.setFromPersonID("49832");
        anotherMessage.setToPersonID("40345");
        anotherMessage.setMessage("What's up");
        messages.add(anotherMessage);


        aMessages = new ChatArrayAdapter(getActivity(),messages);
        lvMessages = (ListView) v.findViewById(R.id.lvMessages);
        lvMessages.setAdapter(aMessages);

        return v;
    }

}
