package com.example.alihfight.alifightapp.Coach.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;

public class SessionsViewHolder extends RecyclerView.ViewHolder {

    public TextView TVSesName, TVSesInstruct, TVSesTimeStart, TVSesTimeEnd, TVSesDay;
    public Button btnAttendees,btnattend;
    public View view;


    public SessionsViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        TVSesName = itemView.findViewById(R.id.TVSessionNameFS);
        TVSesInstruct = itemView.findViewById(R.id.TVInstructorNameFS);
        TVSesTimeStart = itemView.findViewById(R.id.TVSessionStartFS);
        TVSesTimeEnd = itemView.findViewById(R.id.TVSessionEndFS);
        TVSesDay = itemView.findViewById(R.id.TVSessionDayFS);
        btnAttendees = itemView.findViewById(R.id.BTNAttendees);
        btnattend = itemView.findViewById(R.id.btnAttend);
    }
}
