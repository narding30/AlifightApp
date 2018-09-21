package com.example.alihfight.alifightapp.Admin.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

    private DatabaseReference mUsersDatabase;
    View view;
    private TextView display;
    PieChart lcvVisitor;
    PieData data;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_stats, container, false);

        lcvVisitor =  view.findViewById(R.id.chart1);

        display =  view.findViewById(R.id.display);
        final List<PieEntry> entries_reg = new ArrayList<PieEntry>();


        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Statistics");

        mUsersDatabase.orderByChild("SessionName").equalTo("Muay Thai").addListenerForSingleValueEvent(new ValueEventListener() {
            long muaythai;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                muaythai = dataSnapshot.getChildrenCount();

                entries_reg.add(new PieEntry(muaythai, "Muay Thai" ));

                PieDataSet dataSet = new PieDataSet(entries_reg, "");
                dataSet.setSliceSpace(10);
                dataSet.setSelectionShift(10);

                ArrayList<Integer> colors = new ArrayList<Integer>();
                addColors(colors);

                colors.add(ColorTemplate.getHoloBlue());
                dataSet.setColors(colors);

                PieData data = new PieData();
                data.setDataSet(dataSet);

                lcvVisitor.setData(data);
                lcvVisitor.invalidate();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mUsersDatabase.child("SessionName").orderByChild("Fitness").addListenerForSingleValueEvent(new ValueEventListener() {
            long countfitness;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countfitness = dataSnapshot.getChildrenCount();

                entries_reg.add(new PieEntry(countfitness, "Fitness" ));

                PieDataSet dataSet = new PieDataSet(entries_reg, "");
                dataSet.setSliceSpace(10);
                dataSet.setSelectionShift(10);

                ArrayList<Integer> colors = new ArrayList<Integer>();
                addColors(colors);

                colors.add(ColorTemplate.getHoloBlue());
                dataSet.setColors(colors);

                PieData data = new PieData();
                data.setDataSet(dataSet);
                lcvVisitor.setData(data);
                lcvVisitor.invalidate();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mUsersDatabase.child("SessionName").orderByChild("Boxing").addListenerForSingleValueEvent(new ValueEventListener() {
            long countboxing;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countboxing = dataSnapshot.getChildrenCount();

                entries_reg.add(new PieEntry(countboxing, "Boxing" ));

                PieDataSet dataSet = new PieDataSet(entries_reg, "");
                dataSet.setSliceSpace(10);
                dataSet.setSelectionShift(10);

                ArrayList<Integer> colors = new ArrayList<Integer>();
                addColors(colors);

                colors.add(ColorTemplate.getHoloBlue());
                dataSet.setColors(colors);

                PieData data = new PieData();
                data.setDataSet(dataSet);
                lcvVisitor.setData(data);
                lcvVisitor.invalidate();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mUsersDatabase.child("SessionName").orderByChild("Judo").addListenerForSingleValueEvent(new ValueEventListener() {
            long countJudo;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                countJudo = dataSnapshot.getChildrenCount();
                entries_reg.add(new PieEntry(countJudo, "Judo" ));

                PieDataSet dataSet = new PieDataSet(entries_reg, "");
                dataSet.setSliceSpace(10);
                dataSet.setSelectionShift(10);

                ArrayList<Integer> colors = new ArrayList<Integer>();
                addColors(colors);

                colors.add(ColorTemplate.getHoloBlue());
                dataSet.setColors(colors);

                PieData data = new PieData();
                data.setDataSet(dataSet);
                lcvVisitor.setData(data);
                lcvVisitor.invalidate();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return view;
    }

    private void addColors(ArrayList<Integer> list) {
        for (int c : ColorTemplate.VORDIPLOM_COLORS) {
            //list.add(c);
        }
        for (int c : ColorTemplate.JOYFUL_COLORS) {
            //list.add(c);
        }
        for (int c : ColorTemplate.COLORFUL_COLORS) {
            list.add(c);
        }
        for (int c : ColorTemplate.LIBERTY_COLORS) {
            //list.add(c);
        }
        for (int c : ColorTemplate.PASTEL_COLORS) {
            //list.add(c);
        }
    }

}
