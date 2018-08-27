package com.example.alihfight.alifightapp.Admin.Fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.alihfight.alifightapp.Admin.Datas.DataUser;
import com.example.alihfight.alifightapp.Admin.ViewHolders.PendingMembersViewHolder;
import com.example.alihfight.alifightapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingMembers extends Fragment {


    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    View view;
    private EditText searchcontent;
    private ImageButton searchBtn;

    public PendingMembers() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pending_members, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        Recyclerview = view.findViewById(R.id.recyclerViewPending);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("PendingMembers");

        searchcontent = view.findViewById(R.id.ETSearchContent);
        searchBtn = view.findViewById(R.id.btnSearch);

        linearLayoutManager.setStackFromEnd(true);

        Recyclerview.setLayoutManager(linearLayoutManager);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getSearchContent = searchcontent.getText().toString();

                firebaseUserSearch(getSearchContent);
            }
        });

        String searchtext = searchcontent.getText().toString();
        if (TextUtils.isEmpty(searchtext)){
            novalue();
        }

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

                        viewHolder.TvFullName.setText(model.getLastName()+ ", " + model.getFirstName()+ " " + model.getMiddleName());
                        viewHolder.TvAddress.setText(model.getAddress());
                        viewHolder.TvGender.setText(model.getGender());
                        viewHolder.TvAge.setText(model.getAge());
                        viewHolder.TvOccupation.setText(model.getOccupation());

                        viewHolder.BtnApprove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                        .getReference("users");

                                databaseReference
                                        .child(model.getId())
                                        .child("Status")
                                        .setValue("Approved");

                                DatabaseReference From = FirebaseDatabase.getInstance()
                                        .getReference("PendingMembers")
                                        .child(model.getId());


                                DatabaseReference To = FirebaseDatabase.getInstance()
                                        .getReference("ApprovedMembers")
                                        .child(model.getId());

                                moveFirebaseRecord1(From ,To);
                            }
                        });


                        final Handler handler = new Handler();
                        final int delay = 1000; //milliseconds

                        handler.postDelayed(new Runnable(){
                            public void run(){
                                //do something
                                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                DateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                String date = df.format(Calendar.getInstance().getTime());
                                String actualTime = model.getTime();
                                Date time1;
                                Date time2;

                                try {

                                    time2 = format.parse(date);
                                    time1 = format.parse(actualTime);

                                    long diff = time2.getTime() - time1.getTime()  ;
                                    long secondsInMilli = 1000;
                                    long minutesInMilli = secondsInMilli * 60;
                                    long hoursInMilli = minutesInMilli * 60;
                                    long elapsedHours = diff / hoursInMilli;
                                    diff = diff % hoursInMilli;
                                    long elapsedMinutes = diff / minutesInMilli;

                                    if (elapsedMinutes < 60 && elapsedHours == 0) {
                                        viewHolder.TvPendingTime.setText(elapsedMinutes + " Min(s)");
                                    }else if(elapsedHours >= 1 || elapsedHours < 24){
                                        viewHolder.TvPendingTime.setText(elapsedHours+ " Hr(s)");
                                    }else {
                                        viewHolder.TvPendingTime.setText(model.getTime());
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                handler.postDelayed(this, delay);
                            }
                        }, delay);

                    }
                };
        Recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    public void moveFirebaseRecord1(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            Toast.makeText(getContext(), "Copy failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            fromPath.removeValue();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Toast.makeText(getContext(), "Copy failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<DataUser, PendingMembersViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataUser, PendingMembersViewHolder>(

                        DataUser.class,
                        R.layout.pendingmembers_listrow,
                        PendingMembersViewHolder.class,
                        childRef
                ) {
                    @Override
                    protected void populateViewHolder(final PendingMembersViewHolder viewHolder, final DataUser model, int position) {

                        viewHolder.TvFullName.setText(model.getLastName()+ ", " + model.getFirstName()+ " " + model.getMiddleName());
                        viewHolder.TvAddress.setText(model.getAddress());
                        viewHolder.TvGender.setText(model.getGender());
                        viewHolder.TvAge.setText(model.getAge());
                        viewHolder.TvOccupation.setText(model.getOccupation());

                        viewHolder.BtnApprove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                        .getReference("users");

                                databaseReference
                                        .child(model.getId())
                                        .child("Status")
                                        .setValue("Approved");

                                DatabaseReference From = FirebaseDatabase.getInstance()
                                        .getReference("PendingMembers")
                                        .child(model.getId());


                                DatabaseReference To = FirebaseDatabase.getInstance()
                                        .getReference("ApprovedMembers")
                                        .child(model.getId());

                                moveFirebaseRecord1(From ,To);

                            }
                        });

                        final Handler handler = new Handler();
                        final int delay = 1000; //milliseconds

                        handler.postDelayed(new Runnable(){
                            public void run(){
                                //do something
                                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                DateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                String date = df.format(Calendar.getInstance().getTime());
                                String actualTime = model.getTime();
                                Date time1;
                                Date time2;

                                try {

                                    time2 = format.parse(date);
                                    time1 = format.parse(actualTime);

                                    long diff = time2.getTime() - time1.getTime()  ;
                                    long secondsInMilli = 1000;
                                    long minutesInMilli = secondsInMilli * 60;
                                    long hoursInMilli = minutesInMilli * 60;
                                    long elapsedHours = diff / hoursInMilli;
                                    diff = diff % hoursInMilli;
                                    long elapsedMinutes = diff / minutesInMilli;


                                    if (elapsedMinutes < 60 && elapsedHours == 0) {
                                        viewHolder.TvPendingTime.setText(elapsedMinutes + " Min(s)");

                                    }else if(elapsedHours >= 1 || elapsedHours < 24){
                                        viewHolder.TvPendingTime.setText(elapsedHours+ " Hr(s)");
                                    }else {
                                        viewHolder.TvPendingTime.setText(model.getTime());
                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                handler.postDelayed(this, delay);
                            }
                        }, delay);

                    }
                };
        Recyclerview.setAdapter(firebaseRecyclerAdapter);
    }

    private void novalue(){
        final FirebaseRecyclerAdapter<DataUser, PendingMembersViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataUser, PendingMembersViewHolder>(

                        DataUser.class,
                        R.layout.pendingmembers_listrow,
                        PendingMembersViewHolder.class,
                        childRef
                ) {
                    @Override
                    protected void populateViewHolder(final PendingMembersViewHolder viewHolder, final DataUser model, int position) {

                        viewHolder.TvFullName.setText(model.getLastName()+ ", " + model.getFirstName()+ " " + model.getMiddleName());
                        viewHolder.TvAddress.setText(model.getAddress());
                        viewHolder.TvGender.setText(model.getGender());
                        viewHolder.TvAge.setText(model.getAge());
                        viewHolder.TvOccupation.setText(model.getOccupation());

                        viewHolder.BtnApprove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                                        .getReference("users");

                                databaseReference
                                        .child(model.getId())
                                        .child("Status")
                                        .setValue("Approved");

                                DatabaseReference From = FirebaseDatabase.getInstance()
                                        .getReference("PendingMembers")
                                        .child(model.getId());


                                DatabaseReference To = FirebaseDatabase.getInstance()
                                        .getReference("ApprovedMembers")
                                        .child(model.getId());

                                moveFirebaseRecord1(From ,To);
                            }
                        });


                        final Handler handler = new Handler();
                        final int delay = 1000; //milliseconds

                        handler.postDelayed(new Runnable(){
                            public void run(){
                                //do something
                                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                DateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                String date = df.format(Calendar.getInstance().getTime());
                                String actualTime = model.getTime();
                                Date time1;
                                Date time2;

                                try {

                                    time2 = format.parse(date);
                                    time1 = format.parse(actualTime);

                                    long diff = time2.getTime() - time1.getTime()  ;
                                    long secondsInMilli = 1000;
                                    long minutesInMilli = secondsInMilli * 60;
                                    long hoursInMilli = minutesInMilli * 60;
                                    long elapsedHours = diff / hoursInMilli;
                                    diff = diff % hoursInMilli;
                                    long elapsedMinutes = diff / minutesInMilli;


                                    if (elapsedMinutes < 60 && elapsedHours == 0) {
                                        viewHolder.TvPendingTime.setText(elapsedMinutes + " Min(s)");

                                    }else if(elapsedHours >= 1 || elapsedHours < 24){
                                        viewHolder.TvPendingTime.setText(elapsedHours+ " Hr(s)");
                                    }else {
                                        viewHolder.TvPendingTime.setText(model.getTime());
                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                handler.postDelayed(this, delay);
                            }
                        }, delay);

                    }
                };
        Recyclerview.setAdapter(firebaseRecyclerAdapter);
    }
}
