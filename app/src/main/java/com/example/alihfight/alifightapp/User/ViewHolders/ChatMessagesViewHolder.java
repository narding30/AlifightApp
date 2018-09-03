package com.example.alihfight.alifightapp.User.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessagesViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView ProfileImage;
    public TextView TVMessage, TVName;

    public ChatMessagesViewHolder(View itemView) {
        super(itemView);

        ProfileImage = itemView.findViewById(R.id.ProfileImageChat);
        TVMessage = itemView.findViewById(R.id.message_text_layout);
        TVName = itemView.findViewById(R.id.TVUserName);
    }
}
