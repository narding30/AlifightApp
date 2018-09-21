package com.example.alihfight.alifightapp.Coach.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alihfight.alifightapp.R;
import com.example.alihfight.alifightapp.User.Datas.DataSpecialSession;
import com.example.alihfight.alifightapp.User.ViewHolders.SpecialSessionViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecialSessionFragment extends Fragment {

    RecyclerView recyclerViewSsession;
    private LinearLayoutManager linearLayoutManager;
    View view;

    public SpecialSessionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_special_session, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSsession = view.findViewById(R.id.recyclerSpecialSessionCoach);
        recyclerViewSsession.setLayoutManager(linearLayoutManager);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        DatabaseReference fetchSP = FirebaseDatabase.getInstance().getReference("SpecialSession");

        FirebaseRecyclerAdapter<DataSpecialSession, SpecialSessionViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataSpecialSession, SpecialSessionViewHolder>(

                        DataSpecialSession.class,
                        R.layout.specialsession_listrow,
                        SpecialSessionViewHolder.class,
                        fetchSP
                ) {
                    @Override
                    protected void populateViewHolder(SpecialSessionViewHolder viewHolder, final DataSpecialSession model, int position) {

                        viewHolder.TVSPInstructorName.setText(model.getInstructor());
                        viewHolder.TVSPSessionDay.setText(model.getSessionDay());
                        viewHolder.TVSPSessionName.setText(model.getSessionName());
                        viewHolder.TVSPSessionTime.setText(model.getTime());
                        viewHolder.TVSPSTatus.setText(model.getStatus());
                        viewHolder.TVSPFullnAme.setText(model.getFullName());


                        viewHolder.btnreAppoint.setVisibility(View.GONE);


                        if (model.getStatus().equals("Declined") || model.getStatus().equals("Approved")){
                            viewHolder.btnApp.setEnabled(false);
                            viewHolder.btnDec.setEnabled(false);
                        }else {
                            viewHolder.btnApp.setEnabled(true);
                            viewHolder.btnDec.setEnabled(true);
                        }

                        viewHolder.btnDec.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DatabaseReference MydeclineSession = FirebaseDatabase.getInstance().getReference("SpecialSession");
                                DatabaseReference TheirsSession = FirebaseDatabase.getInstance().getReference("MySpecialSession");

                                MydeclineSession.child(model.getKey())
                                        .child("Status")
                                        .setValue("Declined");

                                TheirsSession.child(model.getUserID())
                                        .child(model.getKey())
                                        .child("Status")
                                        .setValue("Declined");


                            }
                        });

                        viewHolder.btnApp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference MydeclineSession = FirebaseDatabase.getInstance().getReference("SpecialSession");
                                DatabaseReference TheirsSession = FirebaseDatabase.getInstance().getReference("MySpecialSession");

                                MydeclineSession.child(model.getKey())
                                        .child("Status")
                                        .setValue("Approved");

                                TheirsSession.child(model.getUserID())
                                        .child(model.getKey())
                                        .child("Status")
                                        .setValue("Approved");
                            }
                        });



                    }
                };
        recyclerViewSsession.setAdapter(firebaseRecyclerAdapter);
    }
}
