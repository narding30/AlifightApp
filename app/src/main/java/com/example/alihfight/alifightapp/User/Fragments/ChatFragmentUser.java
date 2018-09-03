package com.example.alihfight.alifightapp.User.Fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alihfight.alifightapp.R;
import com.example.alihfight.alifightapp.User.Activities.ChatActivity;
import com.example.alihfight.alifightapp.User.Datas.DataChatHeader;
import com.example.alihfight.alifightapp.User.ViewHolders.ChatHeaderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragmentUser extends Fragment {

    View view;
    private RecyclerView Recyclerview;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    String userID;

    public ChatFragmentUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat_user, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        Recyclerview = view.findViewById(R.id.recyclerChatUser);

        linearLayoutManager.setStackFromEnd(true);

        Recyclerview.setLayoutManager(linearLayoutManager);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID =  currentUser.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PaymentDetails");

        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    childRef = FirebaseDatabase.getInstance().getReference("GroupChatHeader");

                    FirebaseRecyclerAdapter<DataChatHeader, ChatHeaderViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataChatHeader, ChatHeaderViewHolder>(

                                    DataChatHeader.class,
                                    R.layout.userchat_listrow,
                                    ChatHeaderViewHolder.class,
                                    childRef.child(dataSnapshot.child("SessionName").getValue().toString())
                            ) {
                                @Override
                                protected void populateViewHolder(final ChatHeaderViewHolder viewHolder, final DataChatHeader model, int position) {
                                    viewHolder.TVChatName.setText(model.getSessionName() +" Group Chat");
                                    viewHolder.TVChatTime.setText(model.getTime());


                                    if (model.getMessageStatus().equals("unread")){
                                        viewHolder.TVChatStatus.setVisibility(View.VISIBLE);

                                        viewHolder.TVChatName.setTypeface(null, Typeface.BOLD);
                                        viewHolder.TVChatStatus.setTypeface(null, Typeface.BOLD);
                                        viewHolder.TVChatTime.setTypeface(null, Typeface.BOLD);

                                    }else {

                                        viewHolder.TVChatStatus.setVisibility(View.GONE);

                                        viewHolder.TVChatName.setTypeface(null, Typeface.NORMAL);
                                        viewHolder.TVChatStatus.setTypeface(null, Typeface.NORMAL);
                                        viewHolder.TVChatTime.setTypeface(null, Typeface.NORMAL);

                                    }

                                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("GroupChatHeader");

                                            databaseReference1.child(model.getSessionName())
                                                    .child(model.getKey())
                                                    .child("MessageStatus")
                                                    .setValue("read");

                                            Intent intent = new Intent(getContext(), ChatActivity.class);
                                            intent.putExtra("Key", model.getKey());
                                            intent.putExtra("GroupName",  viewHolder.TVChatName.getText().toString());
                                            startActivity(intent);
                                        }
                                    });
                                }
                            };
                    Recyclerview.setAdapter(firebaseRecyclerAdapter);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
