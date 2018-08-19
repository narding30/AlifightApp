package com.example.alihfight.alifightapp.Admin.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApprovedMembersViewHolder extends RecyclerView.ViewHolder {

    public TextView name, date, memtype, sessions;
    public CircleImageView profImage;

    public ApprovedMembersViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.ApprovedName);
        date = itemView.findViewById(R.id.ApprovedMembershipDate);
        memtype = itemView.findViewById(R.id.ApprovedMemberType);
        sessions = itemView.findViewById(R.id.ApprovedSessionsLeft);
    }
    public void vImage(String imageUrl, Context applicationContext) {
        Picasso.with(applicationContext).load(imageUrl).placeholder(R.drawable.team).into(profImage);
    }
}
