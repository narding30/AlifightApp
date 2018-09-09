package com.example.alihfight.alifightapp.User.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SessionAttendeesViewHolder extends RecyclerView.ViewHolder {

    CircleImageView CIAttendessImage;
    TextView TVAttendeeName,TVAttendeeSessionLeft,TVAttendeeLastSession;

    public SessionAttendeesViewHolder(View itemView) {
        super(itemView);

        CIAttendessImage = itemView.findViewById(R.id.CIAttendeesImage);
        TVAttendeeName = itemView.findViewById(R.id.TVAttendeesName);
        TVAttendeeSessionLeft = itemView.findViewById(R.id.TVAttendeesSessionLeft);
        TVAttendeeLastSession = itemView.findViewById(R.id.TVAttendeesLastSessioned);
    }
}
