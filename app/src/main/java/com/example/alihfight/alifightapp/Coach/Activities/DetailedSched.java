package com.example.alihfight.alifightapp.Coach.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alihfight.alifightapp.Coach.ViewHolders.SessionsViewHolder;
import com.example.alihfight.alifightapp.R;
import com.example.alihfight.alifightapp.User.Datas.DataAttendees;
import com.example.alihfight.alifightapp.User.ViewHolders.SessionAttendeesViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class DetailedSched extends AppCompatActivity {

    private TextView TVSesName, TVSesInstruct, TVSesStart, TVSesEnd,TVSesCap,TVSesPerWeek;
    private Button BtnSesAttendees;
    private RecyclerView recyclerViewAttendees;
    private ImageView DetailedImage;
    private LinearLayoutManager linearLayoutManager;
    String getKey;
    String getSessionName;
    String getDay;
    private DatabaseReference databaseReference;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_sched);

        TVSesName = findViewById(R.id.TVDetailedSName);
        TVSesInstruct = findViewById(R.id.TVDetailedSInstruct);
        TVSesStart = findViewById(R.id.TVDetailedTimeStart);
        TVSesEnd = findViewById(R.id.TVDetailedTimeEnd);
        TVSesCap = findViewById(R.id.TVDetailedSesCap);
        TVSesPerWeek = findViewById(R.id.TVDetailedSesPerWeek);

        BtnSesAttendees = findViewById(R.id.DetailedBTNAttendees);
        DetailedImage = findViewById(R.id.IVDetailedSession);

        getKey = getIntent().getStringExtra("Key");
        getSessionName = getIntent().getStringExtra("SessionName");
        getDay = getIntent().getStringExtra("Day");


        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerViewAttendees = findViewById(R.id.DetailedrecyclerViewAttendees);
        recyclerViewAttendees.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

         databaseReference = FirebaseDatabase.getInstance().getReference("Attendees");


        databaseReference.child(getSessionName).child(getKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Attendees");

                    FirebaseRecyclerAdapter<DataAttendees, SessionAttendeesViewHolder> firebaseRecyclerAdapter =
                            new FirebaseRecyclerAdapter<DataAttendees, SessionAttendeesViewHolder>(

                                    DataAttendees.class,
                                    R.layout.session_attendees_listrow,
                                    SessionAttendeesViewHolder.class,
                                    databaseReference1.child(getSessionName).child(getKey)

                            ) {
                                @Override
                                protected void populateViewHolder(SessionAttendeesViewHolder viewHolder, final DataAttendees model, int position) {

                                    viewHolder.TVAttendeeName.setText(model.getFullname());
                                    viewHolder.TVAttendeeSessionLeft.setText("Sessions Left: "+model.getPackageCount());


                                    DatabaseReference fetchFromDB = FirebaseDatabase.getInstance().getReference("SessionSchedule");


                                    fetchFromDB.child(getDay).child(getKey).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {

                                                TVSesName.setText(dataSnapshot.child("SessionName").getValue().toString());
                                                TVSesCap.setText(dataSnapshot.child("SessionCapacity").getValue().toString());
                                                TVSesInstruct.setText(dataSnapshot.child("Instructor").getValue().toString());
                                                TVSesPerWeek.setText(dataSnapshot.child("SessionsPerWeek").getValue().toString());
                                                TVSesEnd.setText(dataSnapshot.child("TimeEnd").getValue().toString());
                                                TVSesStart.setText(dataSnapshot.child("TimeStart").getValue().toString());


                                                DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference("Attendees");


                                                String getSesName = TVSesName.getText().toString();
                                                final String getSesCap = TVSesCap.getText().toString();

                                                databaseReference4.child(getSesName)
                                                        .child(getKey).addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()){
                                                            int size = (int) dataSnapshot.getChildrenCount();
                                                            BtnSesAttendees.setText(size+"/"+getSesCap);
                                                        }else{
                                                            BtnSesAttendees.setText("0/" + getSesCap);
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });


                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            };
                    recyclerViewAttendees.setAdapter(firebaseRecyclerAdapter);
                }else {
                    DatabaseReference fetchFromDB = FirebaseDatabase.getInstance().getReference("SessionSchedule");


                    fetchFromDB.child(getDay).child(getKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){

                                TVSesName.setText(dataSnapshot.child("SessionName").getValue().toString());
                                TVSesCap.setText(dataSnapshot.child("SessionCapacity").getValue().toString());
                                TVSesInstruct.setText(dataSnapshot.child("Instructor").getValue().toString());
                                TVSesPerWeek.setText(dataSnapshot.child("SessionsPerWeek").getValue().toString());
                                TVSesEnd.setText(dataSnapshot.child("TimeEnd").getValue().toString());
                                TVSesStart.setText(dataSnapshot.child("TimeStart").getValue().toString());


                                BtnSesAttendees.setText("0/"+TVSesCap.getText().toString());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
