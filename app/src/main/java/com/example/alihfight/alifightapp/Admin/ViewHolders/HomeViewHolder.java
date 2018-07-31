package com.example.alihfight.alifightapp.Admin.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alihfight.alifightapp.R;

public class HomeViewHolder extends RecyclerView.ViewHolder {

    public TextView tvdatetime;
    public TextView content;
    public ImageView imagecontent;

    public HomeViewHolder(View itemView) {
        super(itemView);

        tvdatetime = itemView.findViewById(R.id.TVDateTime);
        content = itemView.findViewById(R.id.TVContent);
        imagecontent = itemView.findViewById(R.id.IVContentImage);
    }
}
