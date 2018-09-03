package com.example.alihfight.alifightapp.User.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;

public class ChatHeaderViewHolder extends RecyclerView.ViewHolder {

    public ImageView IVChatImage;
    public TextView TVChatName, TVChatStatus, TVChatTime;
    public View view;

    public ChatHeaderViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        IVChatImage = itemView.findViewById(R.id.IVChatImage);
        TVChatName = itemView.findViewById(R.id.TVChatName);
        TVChatStatus = itemView.findViewById(R.id.TVChatStatus);
        TVChatTime = itemView.findViewById(R.id.TVChatTime);
    }
}
