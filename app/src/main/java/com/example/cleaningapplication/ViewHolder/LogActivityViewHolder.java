package com.example.cleaningapplication.ViewHolder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningapplication.Interface.ItemCllickListener;
import com.example.cleaningapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class LogActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView date,action;
    public ItemCllickListener listener;

    public LogActivityViewHolder(@NonNull View itemView) {
        super(itemView);
        date= (TextView) itemView.findViewById(R.id.date_log);
        action = (TextView) itemView.findViewById(R.id.action_log);


    }

    @Override
    public void onClick(View view) {

        listener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemCllickListener listener) {
        this.listener = listener;
    }
}