package com.example.alihfight.alifightapp.Admin.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;

public class ViewHolderSchedFrag extends RecyclerView.ViewHolder {

    public TextView TVSessionName;
    public ImageView IVSession;
    public  View view;

    public ViewHolderSchedFrag(View itemView) {
        super(itemView);
        this.view = itemView;
        TVSessionName = itemView.findViewById(R.id.HolderTvSession);
        IVSession = itemView.findViewById(R.id.SchedSessionImage);
    }
}
