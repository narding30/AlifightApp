package com.example.alihfight.alifightapp.Coach.Activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.alihfight.alifightapp.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PersonnelHomeActivity extends AppCompatActivity {


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnel_home);
    }
}
