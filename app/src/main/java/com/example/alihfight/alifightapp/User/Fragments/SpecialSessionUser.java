package com.example.alihfight.alifightapp.User.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.alihfight.alifightapp.Admin.Activities.AddScheduleActivity;
import com.example.alihfight.alifightapp.R;
import com.example.alihfight.alifightapp.User.Datas.DataSpecialSession;
import com.example.alihfight.alifightapp.User.ViewHolders.SpecialSessionViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecialSessionUser extends Fragment {


    View view;
    FloatingActionButton btnAddSSession;
    RecyclerView recyclerViewSsession;
    private LinearLayoutManager linearLayoutManager;
     String userID;

    public SpecialSessionUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_special_session_user, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewSsession = view.findViewById(R.id.recyclerSpecialSession);
        recyclerViewSsession.setLayoutManager(linearLayoutManager);

        btnAddSSession = view.findViewById(R.id.btnaddSSession);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userID =  currentUser.getUid();

        btnAddSSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PaymentDetails");



                databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){

                            final String sessionname = dataSnapshot.child("SessionName").getValue().toString();
                            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                            LayoutInflater inflater = getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.addspecialsession, null);
                            dialogBuilder.setView(dialogView);

                            final EditText ETDay = dialogView.findViewById(R.id.ETDaySPSession);
                            final EditText ETInstructorName = dialogView.findViewById(R.id.ETInstructorSPSession);
                            final EditText ETTimeStart = dialogView.findViewById(R.id.ETStartTimeSPSession);

                            Button btnAddSched = dialogView.findViewById(R.id.btnSaveSchedSPSession);

                            final AlertDialog dialog = dialogBuilder.create();


                            btnAddSched.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    final String getDay = ETDay.getText().toString();
                                    final String getInstructor = ETInstructorName.getText().toString();
                                    final String getTimeStart = ETTimeStart.getText().toString();

                                    if (!TextUtils.isEmpty(getDay) ||
                                            !TextUtils.isEmpty(getInstructor) ||
                                            !TextUtils.isEmpty(getTimeStart)){

                                        DatabaseReference getmyInfos = FirebaseDatabase.getInstance().getReference("users");


                                        getmyInfos.child(userID).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    String getFullName = dataSnapshot.child("FirstName").getValue().toString() +" " +dataSnapshot.child("LastName").getValue().toString();
                                                    DatabaseReference saveMySPSession = FirebaseDatabase.getInstance().getReference("MySpecialSession");
                                                    DatabaseReference saveSPSession = FirebaseDatabase.getInstance().getReference("SpecialSession");
                                                    String key = saveMySPSession.push().getKey();

                                                    HashMap<String, String> HashString = new HashMap<>();
                                                    HashString.put("SessionDay", getDay);
                                                    HashString.put("Instructor", getInstructor);
                                                    HashString.put("Time", getTimeStart);
                                                    HashString.put("UserID", userID);
                                                    HashString.put("Key", key);
                                                    HashString.put("Status", "Pending");
                                                    HashString.put("FullName", getFullName);
                                                    HashString.put("SessionName", sessionname);


                                                    saveSPSession.child(key)
                                                            .setValue(HashString);

                                                    saveMySPSession.child(userID)
                                                            .child(key)
                                                            .setValue(HashString);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



                                    }
                                }
                            });



                            ETTimeStart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final CharSequence[] items2 = {
                                            "1:00 - 3:00 PM", "3:00 - 5:00 PM","5:00 - 7:00 PM","7:00 - 9:00 PM"
                                    };
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                    builder2.setTitle("Make your selection");
                                    builder2.setItems(items2, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int item) {
                                            // Do something with the selection
                                            ETTimeStart.setText(items2[item]);
                                        }
                                    });
                                    AlertDialog alert2 = builder2.create();
                                    alert2.show();
                                }
                            });


                            ETDay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    final CharSequence[] items2 = {
                                            "Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
                                    };
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                    builder2.setTitle("Make your selection");
                                    builder2.setItems(items2, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int item) {
                                            // Do something with the selection
                                            ETDay.setText(items2[item]);
                                        }
                                    });
                                    AlertDialog alert2 = builder2.create();
                                    alert2.show();
                                }
                            });

                            ETInstructorName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Coaches");

                                    databaseReference.orderByChild("SessionInstructor").equalTo(dataSnapshot.child("SessionName").getValue().toString()).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            final List<String> areas = new ArrayList<String>();

                                            for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                                                String areaName = areaSnapshot.child("FullName").getValue(String.class);
                                                areas.add(areaName);
                                            }
                                            final CharSequence[] itemsz = areas.toArray(new CharSequence[areas.size()]);
                                            AlertDialog.Builder builderz = new AlertDialog.Builder(getActivity());
                                            builderz.setTitle("Select");
                                            builderz.setItems(itemsz, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    ETInstructorName.setText(itemsz[i]);
                                                }
                                            });
                                            AlertDialog alertDialogz = builderz.create();
                                            alertDialogz.show();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });






                            dialog.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        DatabaseReference fetchmySPSessions = FirebaseDatabase.getInstance().getReference("MySpecialSession");

        FirebaseRecyclerAdapter<DataSpecialSession, SpecialSessionViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataSpecialSession, SpecialSessionViewHolder>(

                        DataSpecialSession.class,
                        R.layout.specialsession_listrow,
                        SpecialSessionViewHolder.class,
                        fetchmySPSessions.child(userID)

                ) {
                    @Override
                    protected void populateViewHolder(SpecialSessionViewHolder viewHolder, final DataSpecialSession model, int position) {
                        viewHolder.TVSPInstructorName.setText(model.getInstructor());
                        viewHolder.TVSPSessionDay.setText(model.getSessionDay());
                        viewHolder.TVSPSessionName.setText(model.getSessionName());
                        viewHolder.TVSPSessionTime.setText(model.getTime());
                        viewHolder.TVSPSTatus.setText(model.getStatus());
                        viewHolder.TVSPFullnAme.setText(model.getFullName());

                        viewHolder.btnApp.setVisibility(View.GONE);
                        viewHolder.btnDec.setVisibility(View.GONE);
                        viewHolder.btnreAppoint.setVisibility(View.GONE);

                        if (model.getStatus().equals("Declined")){
                            viewHolder.btnreAppoint.setVisibility(View.VISIBLE);
                        }else {
                            viewHolder.btnreAppoint.setVisibility(View.GONE);
                        }

                        viewHolder.btnreAppoint.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PaymentDetails");



                                databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(final DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()){

                                            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                                            LayoutInflater inflater = getLayoutInflater();
                                            final View dialogView = inflater.inflate(R.layout.addspecialsession, null);
                                            dialogBuilder.setView(dialogView);

                                            final EditText ETDay = dialogView.findViewById(R.id.ETDaySPSession);
                                            final EditText ETInstructorName = dialogView.findViewById(R.id.ETInstructorSPSession);
                                            final EditText ETTimeStart = dialogView.findViewById(R.id.ETStartTimeSPSession);

                                            Button btnAddSched = dialogView.findViewById(R.id.btnSaveSchedSPSession);

                                            final AlertDialog dialog = dialogBuilder.create();


                                            btnAddSched.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    String getDay = ETDay.getText().toString();
                                                    String getInstructor = ETInstructorName.getText().toString();
                                                    String getTimeStart = ETTimeStart.getText().toString();

                                                    if (!TextUtils.isEmpty(getDay) ||
                                                            !TextUtils.isEmpty(getInstructor) ||
                                                            !TextUtils.isEmpty(getTimeStart)){

                                                        DatabaseReference saveMySPSession = FirebaseDatabase.getInstance().getReference("MySpecialSession");
                                                        DatabaseReference saveSPSession = FirebaseDatabase.getInstance().getReference("SpecialSession");

                                                        HashMap<String, Object> HashString = new HashMap<>();
                                                        HashString.put("SessionDay", getDay);
                                                        HashString.put("Instructor", getInstructor);
                                                        HashString.put("Time", getTimeStart);
                                                        HashString.put("UserID", userID);
                                                        HashString.put("Status", "Pending");
                                                        HashString.put("SessionName", dataSnapshot.child("SessionName").getValue().toString());


                                                        saveSPSession.child(model.getKey())
                                                                .updateChildren(HashString);

                                                        saveMySPSession.child(userID)
                                                                .child(model.getKey())
                                                                .updateChildren(HashString);

                                                    }
                                                }
                                            });



                                            ETTimeStart.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    final CharSequence[] items2 = {
                                                            "1:00 - 3:00 PM", "3:00 - 5:00 PM","5:00 - 7:00 PM","7:00 - 9:00 PM"
                                                    };
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                                    builder2.setTitle("Make your selection");
                                                    builder2.setItems(items2, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int item) {
                                                            // Do something with the selection
                                                            ETTimeStart.setText(items2[item]);
                                                        }
                                                    });
                                                    AlertDialog alert2 = builder2.create();
                                                    alert2.show();
                                                }
                                            });


                                            ETDay.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    final CharSequence[] items2 = {
                                                            "Monday", "Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
                                                    };
                                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                                                    builder2.setTitle("Make your selection");
                                                    builder2.setItems(items2, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int item) {
                                                            // Do something with the selection

                                                            ETDay.setText(items2[item]);

                                                        }
                                                    });
                                                    AlertDialog alert2 = builder2.create();
                                                    alert2.show();
                                                }
                                            });

                                            ETInstructorName.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {

                                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Coaches");

                                                    databaseReference.orderByChild("SessionInstructor").equalTo(dataSnapshot.child("SessionName").getValue().toString()).addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            final List<String> areas = new ArrayList<String>();

                                                            for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                                                                String areaName = areaSnapshot.child("FullName").getValue(String.class);
                                                                areas.add(areaName);
                                                            }
                                                            final CharSequence[] itemsz = areas.toArray(new CharSequence[areas.size()]);
                                                            AlertDialog.Builder builderz = new AlertDialog.Builder(getActivity());
                                                            builderz.setTitle("Select");
                                                            builderz.setItems(itemsz, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    ETInstructorName.setText(itemsz[i]);
                                                                }
                                                            });
                                                            AlertDialog alertDialogz = builderz.create();
                                                            alertDialogz.show();
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });

                                            dialog.show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                            }
                        });

                    }
                };
        recyclerViewSsession.setAdapter(firebaseRecyclerAdapter);
    }
}
