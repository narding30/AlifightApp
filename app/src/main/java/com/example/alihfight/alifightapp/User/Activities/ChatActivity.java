package com.example.alihfight.alifightapp.User.Activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.example.alihfight.alifightapp.R;
import com.example.alihfight.alifightapp.User.Datas.ChatMessage;
import com.example.alihfight.alifightapp.User.ViewHolders.ChatMessagesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    ImageView sendButton;
    EditText messageArea;
    private String groupName;
    private RecyclerView recyclerChat;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerChat = findViewById(R.id.recyclerChatMessages);
        sendButton = findViewById(R.id.sendButton);
        messageArea = findViewById(R.id.messageArea);

        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(true);
        recyclerChat.setHasFixedSize(true);

        recyclerChat.setLayoutManager(linearLayoutManager);

        groupName = getIntent().getStringExtra("GroupName");


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendmessage();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String UID =  currentUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(groupName);

        final FirebaseRecyclerAdapter<ChatMessage, ChatMessagesViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<ChatMessage, ChatMessagesViewHolder>(

                        ChatMessage.class,
                        R.layout.message_single_layout,
                        ChatMessagesViewHolder.class,
                        databaseReference.child(groupName).child("messages")

                ) {
                    @Override
                    protected void populateViewHolder(ChatMessagesViewHolder viewHolder, ChatMessage model, int position) {
                        viewHolder.TVMessage.setText(model.getMessageText());
                        viewHolder.TVName.setText(model.getMessageUser());


                        if (model.getFrom().equals(UID)){
                            viewHolder.TVMessage.setBackgroundColor(Color.WHITE);
                            viewHolder.TVMessage.setTextColor(Color.BLACK);
                        }else {
                            viewHolder.TVMessage.setBackgroundResource(R.drawable.message_text_background);
                            viewHolder.TVMessage.setTextColor(Color.WHITE);
                        }
                    }
                };
        recyclerChat.setAdapter(firebaseRecyclerAdapter);
    }

    private void sendmessage() {

        final String message = messageArea.getText().toString();

        if (!TextUtils.isEmpty(message)){

            DateFormat df = new SimpleDateFormat("h:mm a");
            final String date = df.format(Calendar.getInstance().getTime());

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            final String userID =  currentUser.getUid();

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

            databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        String getUserType;

                        getUserType =  dataSnapshot.child("usertype").getValue().toString();

                        if (getUserType.equals("admin")){

                            DatabaseReference user_message_push = FirebaseDatabase.getInstance().getReference(groupName);

                            user_message_push.child(userID).push();

                            String push_id = user_message_push.push().getKey();

                            String current_user_ref = groupName+"/"+ userID;
                            String group_chat_ref = groupName+"/"+ "messages";

                            Map messageMap = new HashMap();
                            messageMap.put("messageText", message);
                            messageMap.put("messageTime", date);
                            messageMap.put("messageUser", "admin");
                            messageMap.put("from", userID);

                            Map messageUserMap = new HashMap();
                            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                            messageUserMap.put(group_chat_ref + "/" + push_id, messageMap);

                            user_message_push.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null){
                                        Log.d("CHAT LOG", databaseError.getMessage().toString());
                                    }
                                }
                            });
                        }else {
                            DatabaseReference user_message_push = FirebaseDatabase.getInstance().getReference(groupName);

                            user_message_push.child(userID).push();

                            String push_id = user_message_push.push().getKey();

                            String current_user_ref = groupName+"/"+ userID;
                            String group_chat_ref = groupName+"/"+ "messages";

                            Map messageMap = new HashMap();
                            messageMap.put("messageText", message);
                            messageMap.put("messageTime", date);
                            messageMap.put("messageUser", dataSnapshot.child("FirstName").getValue().toString());
                            messageMap.put("from", userID);

                            Map messageUserMap = new HashMap();
                            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                            messageUserMap.put(group_chat_ref + "/" + push_id, messageMap);

                            user_message_push.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null){
                                        Log.d("CHAT LOG", databaseError.getMessage().toString());
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            messageArea.setText("");
        }
    }
}
