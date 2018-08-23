package com.example.alihfight.alifightapp.User.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alihfight.alifightapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragmentUser extends Fragment {


    public ProfileFragmentUser() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_user, container, false);
    }

}
