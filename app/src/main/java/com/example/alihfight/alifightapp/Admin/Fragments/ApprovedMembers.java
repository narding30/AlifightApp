package com.example.alihfight.alifightapp.Admin.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.alihfight.alifightapp.Admin.Datas.DataUser;
import com.example.alihfight.alifightapp.Admin.ViewHolders.PendingMembersViewHolder;
import com.example.alihfight.alifightapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApprovedMembers extends Fragment {

    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    View view;
    private EditText searchcontent;
    private ImageButton searchBtn;

    public ApprovedMembers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_approved_members, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        Recyclerview = view.findViewById(R.id.recyclerViewPendingApproved);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("PendingMembers");

        searchcontent = view.findViewById(R.id.ETSearchContentApproved);
        searchBtn = view.findViewById(R.id.btnSearchApproved);

        linearLayoutManager.setStackFromEnd(true);

        Recyclerview.setLayoutManager(linearLayoutManager);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getSearchContent = searchcontent.getText().toString();

                firebaseUserSearch(getSearchContent);
            }
        });

        return view;
    }

    private void firebaseUserSearch(String searchText) {

        Query firebaseSearchQuery = childRef.orderByChild("FirstName").startAt(searchText).endAt(searchText+ "\uf8ff");

        FirebaseRecyclerAdapter<DataUser, PendingMembersViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataUser, PendingMembersViewHolder>(

                        DataUser.class,
                        R.layout.pendingmembers_listrow,
                        PendingMembersViewHolder.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(final PendingMembersViewHolder viewHolder, final DataUser model, int position) {


                    }
                };
        Recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

}
