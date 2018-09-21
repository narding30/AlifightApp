package com.example.alihfight.alifightapp.User.Fragments;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.alihfight.alifightapp.Admin.Datas.DataHome;
import com.example.alihfight.alifightapp.Admin.ViewHolders.HomeViewHolder;
import com.example.alihfight.alifightapp.R;
import com.example.alihfight.alifightapp.User.Activities.UserHomeActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragmentUser extends Fragment {


    View view;
    private RecyclerView Recyclerview;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference childRef;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mUserDatabase;
    private FirebaseUser firebaseUser;
    String userID;

    public NewsFragmentUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news_user, container, false);

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        Recyclerview = view.findViewById(R.id.recyclerNewsUser);

        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        Recyclerview.setLayoutManager(linearLayoutManager);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        childRef = mDatabaseRef.child("UsersNewsFeed");


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();

        DatabaseReference fetchNotif = FirebaseDatabase.getInstance().getReference("NotifUser");

        fetchNotif.child(userID).orderByChild("NotifStatus").equalTo("unread").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long countchildren = dataSnapshot.getChildrenCount();

                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(getActivity());
                    Intent intent = new Intent(getActivity(), UserHomeActivity.class);
                    intent.putExtra("notification", "unread");
                    PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

                    mBuilder.setContentIntent(pendingIntent);

                    mBuilder.setSmallIcon(R.drawable.background);
                    mBuilder.setContentTitle("You have received a notification");
                    mBuilder.setContentText("You have "+(int) countchildren+" unread notification");
                    mBuilder.setPriority(Notification.PRIORITY_MAX);

                    long[] vibrate = {0, 100, 200, 300};
                    Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    mBuilder.setSound(alarmSound);
                    mBuilder.setVibrate(vibrate);
                    NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.notify(001, mBuilder.build());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FirebaseRecyclerAdapter<DataHome, HomeViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<DataHome, HomeViewHolder>(

                        DataHome.class,
                        R.layout.homelistrow,
                        HomeViewHolder.class,
                        childRef

                ) {
                    @Override
                    protected void populateViewHolder(final HomeViewHolder viewHolder, final DataHome model, int position) {

                        viewHolder.content.setText(model.getContent());

                        final Handler handler = new Handler();
                        final int delay = 1000; //milliseconds

                        handler.postDelayed(new Runnable(){
                            public void run(){
                                //do something
                                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                DateFormat df = new SimpleDateFormat("yyyy/MM/dd h:mm a");
                                String date = df.format(Calendar.getInstance().getTime());
                                String actualTime = model.getDateTime();
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
                                        viewHolder.tvdatetime.setText(elapsedMinutes + " Min(s)");

                                    }else if(elapsedHours >= 1 || elapsedHours < 24){
                                        viewHolder.tvdatetime.setText(elapsedHours+ " Hr(s)");
                                    }else {
                                        viewHolder.tvdatetime.setText(model.getMonth()+" "+ model.getDay() +" at "+ model.getTime());
                                    }


                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                handler.postDelayed(this, delay);
                            }
                        }, delay);



                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("news_image").child(model.getKey());

                        mUserDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    viewHolder.imagecontent.setVisibility(View.VISIBLE);
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
                                }else{

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
