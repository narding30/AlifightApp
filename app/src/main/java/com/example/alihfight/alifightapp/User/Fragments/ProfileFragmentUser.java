package com.example.alihfight.alifightapp.User.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragmentUser extends Fragment {

    View view;
    CircleImageView IVProfImage;
    TextView nametext,agetext,sextext,addresstext,contactnumbertext,occupationtext,sessiontext,sessionslefttext;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    public String userID;


    public ProfileFragmentUser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_profile_user, container, false);

        IVProfImage = view.findViewById(R.id.IVProfileImage);
        nametext = view.findViewById(R.id.TVNameProfile);
        agetext =view.findViewById(R.id.TVAgeProfile);
        sextext = view.findViewById(R.id.TVSexProfile);
        addresstext = view.findViewById(R.id.TVAddressProfile);
        contactnumbertext = view.findViewById(R.id.TVContactNumProfile);
        occupationtext = view.findViewById(R.id.TVOccupationProfile);
        sessiontext = view.findViewById(R.id.TVSessionProfile);
        sessionslefttext = view.findViewById(R.id.TVSessionsLeftProfile);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        userID =  currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        databaseReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nametext.setText(dataSnapshot.child("FirstName").getValue().toString() + " " +dataSnapshot.child("LastName").getValue().toString());
                agetext.setText(dataSnapshot.child("Age").getValue().toString());
                sextext.setText(dataSnapshot.child("Gender").getValue().toString());
                addresstext.setText(dataSnapshot.child("Address").getValue().toString());
                occupationtext.setText(dataSnapshot.child("Occupation").getValue().toString());
                contactnumbertext.setText(dataSnapshot.child("Contact").getValue().toString());



                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("PaymentDetails").child(userID);


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        sessiontext.setText(dataSnapshot.child("SessionName").getValue().toString());
                        sessionslefttext.setText(dataSnapshot.child("Package").getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
