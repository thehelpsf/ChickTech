package org.chicktech.chicktech.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
    private Runnable disableListAnimationsTask;
    private Handler handler;



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

        currentUser = (Person) ParseUser.getCurrentUser();

        handler = new Handler();
        disableListAnimationsTask = new Runnable() {
            @Override
            public void run() {
                // Disable animation in next run loop so animations that are queued in this
                // run loop can still run.
                aMessages.enableAnimations = false;
            }
        };

    }

    public void setupChatReceiver(){


    }

    @Override
    public void onResume() {
        super.onResume();

        if (currentUser.getPartnerId() == null) {
            return;
        }

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
                        // Animate when receiving new messages
                        aMessages.enableAnimations = true;
                        aMessages.add((ChatMessage) chatMessage);
                        handler.postDelayed(disableListAnimationsTask, 0);
                    }
                });
            }
        };

        getActivity().registerReceiver(mChatMessageReceiver, chatIntentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (currentUser.getPartnerId() == null) {
            return;
        }

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
        aMessages = new ChatArrayAdapter(getActivity(),messages);
        lvMessages = (ListView) v.findViewById(R.id.lvMessages);
        lvMessages.setAdapter(aMessages);


        final String partnerId = currentUser.getPartnerID();
        if (partnerId == null) {
            // When no mentor/mentee has been assigned yet, disable send button and show no partner message.
            btnSend.setEnabled(false);
            etMessage.setEnabled(false);

            TextView tvNoPartner = (TextView) v.findViewById(R.id.tvNoPartner);
            tvNoPartner.setText(getString(currentUser.getRole() == Person.Role.MENTOR ? R.string.no_student_message : R.string.no_mentor_message));
            tvNoPartner.setVisibility(View.VISIBLE);
        } else {
            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etMessage.length() > 0) {
                        ChatMessage message = new ChatMessage();
                        message.setToPersonID(partnerId);
                        message.setFromPersonID(currentUser.getObjectId());
                        message.setMessage(etMessage.getText().toString());
                        parseClient.sendChatMessage(message.getToPersonID(), message.getFromPersonID(), message.getMessage(), new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject chatMessage, ParseException e) {
                                chatMessage.pinInBackground();
                                // Animate when sending messages
                                aMessages.enableAnimations = true;
                                aMessages.add((ChatMessage) chatMessage);
                                handler.postDelayed(disableListAnimationsTask, 0);
                            }
                        });

                        etMessage.setText("");
                    }
                }
            });
        }

        return v;
    }
}
