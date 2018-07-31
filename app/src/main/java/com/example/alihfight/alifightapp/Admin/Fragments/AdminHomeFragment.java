package com.example.alihfight.alifightapp.Admin.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alihfight.alifightapp.Admin.Activities.PostContentActivity;
import com.example.alihfight.alifightapp.Admin.Datas.DataHome;
import com.example.alihfight.alifightapp.Admin.ViewHolders.HomeViewHolder;
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
public class AdminHomeFragment extends Fragment {

    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase;
    View view;
    TextView tvcreatecontent;

    public AdminHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        tvcreatecontent = view.findViewById(R.id.TVGoCreateContent);

        linearLayoutManager = new LinearLayoutManager(getContext());
        Recyclerview = view.findViewById(R.id.recyclerViewHome);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("VesselSchedule").child("Sunday").child("Departed");


        Recyclerview.setLayoutManager(linearLayoutManager);

        tvcreatecontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), PostContentActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        FirebaseRecyclerAdapter<DataHome, HomeViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataHome, HomeViewHolder>(

                        DataHome.class,
                        R.layout.homelistrow,
                        HomeViewHolder.class,
                        childRef

                ) {
                    @Override
                    protected void populateViewHolder(final HomeViewHolder viewHolder, DataHome model, int position) {
                        viewHolder.tvdatetime.setText(model.getDateTime());
                        viewHolder.content.setText(model.getContent());




                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("VesselImage").child(model.getKey());

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
                                                .into(viewHolder.imagecontent , new Callback() {
                                                    @Override
                                                    public void onSuccess() {

                                                    }

                                                    @Override
                                                    public void onError() {
                                                        Picasso.with(getContext())
                                                                .load(image)
                                                                .placeholder(R.drawable.zz)
                                                                .into(viewHolder.imagecontent);
                                                    }
                                                });
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                    }
                };
        Recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

}
