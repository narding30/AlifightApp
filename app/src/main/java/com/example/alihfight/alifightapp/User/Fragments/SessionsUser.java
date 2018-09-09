package com.example.alihfight.alifightapp.User.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.alihfight.alifightapp.Coach.Activities.DetailedSched;
import com.example.alihfight.alifightapp.Coach.Datas.DataSession;
import com.example.alihfight.alifightapp.Coach.ViewHolders.SessionsViewHolder;
import com.example.alihfight.alifightapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SessionsUser extends Fragment {

    View view;
    private RecyclerView recyclerSessions;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseUser firebaseUser;
    String userID;
    private Button btnfullsched;

    public SessionsUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sessions_user, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerSessions = view.findViewById(R.id.recyclerUsersSessions);
        recyclerSessions.setLayoutManager(linearLayoutManager);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("SessionSchedule").child("Sunday");
                break;


            case Calendar.MONDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("SessionSchedule").child("Monday");
                break;


            case Calendar.TUESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("SessionSchedule").child("Tuesday");
                break;


            case Calendar.WEDNESDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("SessionSchedule").child("Wednesday");
                break;


            case Calendar.THURSDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("SessionSchedule").child("Thursday");
                break;


            case Calendar.FRIDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("SessionSchedule").child("Friday");
                break;


            case Calendar.SATURDAY:
                mDatabaseRef = FirebaseDatabase.getInstance().getReference();
                childRef = mDatabaseRef.child("SessionSchedule").child("Saturday");
                break;


        }

        btnfullsched = view.findViewById(R.id.BTNviewfullschedUser);
        btnfullsched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();

        databaseReference1.child("PaymentDetails").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    String getFullN = dataSnapshot.child("SessionName").getValue().toString();

                    FirebaseRecyclerAdapter<DataSession, SessionsViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataSession, SessionsViewHolder>(

                                    DataSession.class,
                                    R.layout.sessions_listrow,
                                    SessionsViewHolder.class,
                                    childRef.orderByChild("SessionName").equalTo(getFullN)

                            ) {

                                @Override
                                protected void populateViewHolder(SessionsViewHolder viewHolder, final DataSession model, int position) {

                                    viewHolder.TVSesName.setText(model.getSessionName());
                                    viewHolder.TVSesDay.setText(model.getSessionDay());
                                    viewHolder.TVSesInstruct.setText(model.getInstructor());
                                    viewHolder.TVSesTimeEnd.setText(model.getTimeEnd());
                                    viewHolder.TVSesTimeStart.setText(model.getTimeStart());
                                    viewHolder.btnAttendees.setText(model.getAttendees()+"/"+model.getSessionCapacity());

                                    final String getPackageCount = dataSnapshot.child("Package").getValue().toString();


                                    viewHolder.btnattend.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            DatabaseReference retrieveUserInfo = FirebaseDatabase.getInstance().getReference("users");

                                            retrieveUserInfo.child(userID).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()){
                                                        DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference("Attendees");
                                                        String key = databaseReference2.child(model.getSessionName()).child(model.getKey()).push().getKey();


                                                        HashMap<String, String> HashString = new HashMap<String,String>();
                                                        HashString.put("Fullname", dataSnapshot.child("FirstName").getValue().toString()+" "+dataSnapshot.child("LastName").getValue().toString());
                                                        HashString.put("PackageCount", getPackageCount);
                                                        HashString.put("UserID", userID);
                                                        HashString.put("Key", key);



                                                         databaseReference2.child(model.getSessionName())
                                                                          .child(model.getKey())
                                                                          .child(key)
                                                                          .setValue(HashString);

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    });

                                    viewHolder.view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            Intent intent = new Intent(getContext(), DetailedSched.class);
                                            intent.putExtra("Key", model.getKey());
                                            startActivity(intent);
                                        }
                                    });

                                }
                            };
                    recyclerSessions.setAdapter(firebaseRecyclerAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
