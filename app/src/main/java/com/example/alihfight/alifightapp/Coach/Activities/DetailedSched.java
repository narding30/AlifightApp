package com.example.alihfight.alifightapp.Coach.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;

public class DetailedSched extends AppCompatActivity {

    private TextView TVSesName, TVSesInstruct, TVSesStart, TVSesEnd,TVSesCap,TVSesPerWeek;
    private Button BtnSesAttendees;
    private RecyclerView recyclerViewAttendees;
    private ImageView DetailedImage;
    private LinearLayoutManager linearLayoutManager;
    String getKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_sched);

        TVSesName = findViewById(R.id.TVDetailedSName);
        TVSesInstruct = findViewById(R.id.TVDetailedSInstruct);
        TVSesStart = findViewById(R.id.TVDetailedTimeStart);
        TVSesEnd = findViewById(R.id.TVDetailedTimeEnd);
        TVSesCap = findViewById(R.id.TVDetailedSesCap);
        TVSesPerWeek = findViewById(R.id.TVDetailedSesPerWeek);
        BtnSesAttendees = findViewById(R.id.DetailedBTNAttendees);
        DetailedImage = findViewById(R.id.IVDetailedSession);

        getKey = getIntent().getStringExtra("Key");

        recyclerViewAttendees = findViewById(R.id.DetailedrecyclerViewAttendees);
        linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerViewAttendees.setLayoutManager(linearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
