package com.example.alihfight.alifightapp.Admin.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alihfight.alifightapp.Admin.Activities.AddScheduleActivity;
import com.example.alihfight.alifightapp.Admin.Activities.SetScheduleDataActivity;
import com.example.alihfight.alifightapp.Admin.Datas.DataSchedule;
import com.example.alihfight.alifightapp.Admin.ViewHolders.SchedViewHolder;
import com.example.alihfight.alifightapp.Admin.ViewHolders.ViewHolderSchedFrag;
import com.example.alihfight.alifightapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScheduleFragment extends Fragment {

    View view;
    FloatingActionButton floatingActionButton;
    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase;

    public ScheduleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_schedule, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext());
        Recyclerview = view.findViewById(R.id.recyclerSched);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        childRef = mDatabaseRef.child("SessionDetails");

        Recyclerview.setLayoutManager(linearLayoutManager);

        floatingActionButton = view.findViewById(R.id.btnaddsched);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SetScheduleDataActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataSchedule, ViewHolderSchedFrag> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataSchedule, ViewHolderSchedFrag>(
                        DataSchedule.class,
                        R.layout.schedulefrag_listrow,
                        ViewHolderSchedFrag.class,
                        childRef
                ) {
                    @Override
                    protected void populateViewHolder(final ViewHolderSchedFrag viewHolder, final DataSchedule model, int position) {
                        viewHolder.TVSessionName.setText(model.getSessionName());

                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("SessionImage").child(model.getSessionName());

                        mUserDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    final String image = dataSnapshot.child("image").getValue().toString();

                                    if (!image.equals("default")){
                                        Picasso.with(getContext())
                                                .load(image)
                                                .fit().centerCrop()
                                                .networkPolicy(NetworkPolicy.OFFLINE)
                                                .placeholder(R.drawable.zz)
                                                .into(viewHolder.IVSession, new Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError() {
                                                        Picasso.with(getContext())
                                                                .load(image)
                                                                .placeholder(R.drawable.zz)
                                                                .into(viewHolder.IVSession);
                                                    }
                                                });
                                    }
                                }
                                }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent myIntent = new Intent(getContext(), AddScheduleActivity.class);
                                myIntent.putExtra("SessionName", model.getSessionName());
                                myIntent.putExtra("SessionCapacity", model.getSessionCapacity());
                                myIntent.putExtra("SessionsPerWeek", model.getSessionsPerWeek());
                                startActivity(myIntent);
                            }
                        });
                    }
                };
        Recyclerview.setAdapter(firebaseRecyclerAdapter);
    }
}
