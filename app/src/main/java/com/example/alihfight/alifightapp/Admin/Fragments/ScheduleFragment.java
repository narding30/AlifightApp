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

import com.example.alihfight.alifightapp.Admin.Activities.SetScheduleDataActivity;
import com.example.alihfight.alifightapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

}
