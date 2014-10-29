package org.chicktech.chicktech.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.chicktech.chicktech.R;
import org.chicktech.chicktech.adapters.ChatArrayAdapter;
import org.chicktech.chicktech.models.ChatMessage;
import org.chicktech.chicktech.models.Person;
import org.chicktech.chicktech.utils.CTRestClient;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment{

    Button btnSend;
    EditText etMessage;
    ListView lvMessages;
    ChatArrayAdapter aMessages;
    ArrayList<ChatMessage> messages;
    Person currentUser;
    CTRestClient parseClient;
    private BroadcastReceiver mChatMessageReceiver;



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

    public void setupChatReceiver(){


    }

    @Override
    public void onResume() {
        super.onResume();

        currentUser = (Person) ParseUser.getCurrentUser();


        ParseQuery<ParseObject> query = ParseQuery.getQuery("ChatMessage");
        query.orderByAscending("createdAt");
        query.fromLocalDatastore();

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> chatMessages, ParseException e) {
                aMessages.clear();
                for (int i = 0; i < chatMessages.size(); i++) {
                    aMessages.add((ChatMessage) chatMessages.get(i));
                }
            }
        });

        CTRestClient.getPersonById(currentUser.getPartnerId(),new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                Person partnerUser = (Person) parseUser;
                String chatTitle = " Chat with " + partnerUser.getPersonName();
                getActivity().getActionBar()
                        .setTitle(chatTitle);
            }
        });

        IntentFilter chatIntentFilter = new IntentFilter(
                "com.chicktech.CHAT_MESSAGE");

        mChatMessageReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                ParseQuery<ParseObject> query = ParseQuery.getQuery("ChatMessage");
                query.orderByDescending("createdAt");
                query.fromLocalDatastore();

                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject chatMessage, ParseException e) {
                        chatMessage.saveEventually();
                        aMessages.add((ChatMessage) chatMessage);
                    }
                });
            }
        };

        getActivity().registerReceiver(mChatMessageReceiver, chatIntentFilter);

    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().unregisterReceiver(mChatMessageReceiver);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        btnSend = (Button) v.findViewById(R.id.btnSend);
        etMessage = (EditText) v.findViewById(R.id.etMessage);
        lvMessages = (ListView) v.findViewById(R.id.lvMessages);
        messages = new ArrayList<ChatMessage>();
        parseClient = new CTRestClient();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etMessage.length() > 0){
                    ChatMessage message = new ChatMessage();
                    message.setToPersonID(currentUser.getPartnerID());
                    message.setFromPersonID(currentUser.getObjectId());
                    message.setMessage(etMessage.getText().toString());
                    parseClient.sendChatMessage(message.getToPersonID(), message.getFromPersonID(), message.getMessage(),new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject chatMessage, ParseException e) {
                            chatMessage.pinInBackground();
                            aMessages.add((ChatMessage)chatMessage);
                        }
                    });

                    etMessage.setText("");
                }
            }
        });

        aMessages = new ChatArrayAdapter(getActivity(),messages);
        lvMessages = (ListView) v.findViewById(R.id.lvMessages);
        lvMessages.setAdapter(aMessages);

        return v;
    }
}
