package com.example.alihfight.alifightapp.Admin.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;

public class PendingMembersViewHolder extends RecyclerView.ViewHolder {

    public TextView TvTime, TvFullName, TvAddress, TvAge, TvGender, TvOccupation, TvPendingTime;
    public Button BtnApprove;

    public PendingMembersViewHolder(View itemView) {
        super(itemView);

        TvTime = itemView.findViewById(R.id.TVTimePending);
        TvFullName = itemView.findViewById(R.id.TVFullNamePending);
        TvAddress = itemView.findViewById(R.id.TVAddressPending);
        TvAge = itemView.findViewById(R.id.TvAgePending);
        TvGender = itemView.findViewById(R.id.TVGenderPending);
        TvOccupation = itemView.findViewById(R.id.TVOccupationPending);
        TvPendingTime = itemView.findViewById(R.id.TVTimePending);
        BtnApprove = itemView.findViewById(R.id.BTNAcceptPending);
    }
}
