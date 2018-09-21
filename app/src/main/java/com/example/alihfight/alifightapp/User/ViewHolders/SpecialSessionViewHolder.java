package com.example.alihfight.alifightapp.User.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;

public class SpecialSessionViewHolder extends RecyclerView.ViewHolder {

    public TextView TVSPSessionName, TVSPSessionDay,TVSPSessionTime, TVSPInstructorName,TVSPSTatus,TVSPFullnAme;
    public View view;
    public Button btnDec, btnApp, btnreAppoint;

    public SpecialSessionViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        TVSPInstructorName = itemView.findViewById(R.id.TVSPSessionInstructor);
        TVSPSessionName = itemView.findViewById(R.id.TVSPSessionName);
        TVSPSessionDay = itemView.findViewById(R.id.TVSPSessionDay);
        TVSPSessionTime = itemView.findViewById(R.id.TVSPSessionTime);
        TVSPSTatus = itemView.findViewById(R.id.TVSPStatus);
        TVSPFullnAme = itemView.findViewById(R.id.TVSPFullName);

        btnDec = itemView.findViewById(R.id.btnDecline);
        btnApp = itemView.findViewById(R.id.btnApproved);
        btnreAppoint = itemView.findViewById(R.id.btnReappoint);
    }

}
