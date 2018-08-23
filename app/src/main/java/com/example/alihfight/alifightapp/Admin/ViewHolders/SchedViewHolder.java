package com.example.alihfight.alifightapp.Admin.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;

public class SchedViewHolder extends RecyclerView.ViewHolder {

    public TextView tvday;
    public TextView tvLocation;
    public TextView tvTime;
    public LinearLayout LLdelete;

    public SchedViewHolder(View itemView) {
        super(itemView);

        tvday = itemView.findViewById(R.id.HolderTvDay);
        tvLocation = itemView.findViewById(R.id.HolderTVInstructor);
        tvTime = itemView.findViewById(R.id.HolderTvTime);
        LLdelete = itemView.findViewById(R.id.LLdelete);
    }
}
