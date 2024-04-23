package com.example.cleaningapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningapplication.Interface.ItemCllickListener;
import com.example.cleaningapplication.R;

public class WorkerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView WorkerName;
    public ImageView WorkerPicture;
    public ItemCllickListener listener;


    public WorkerViewHolder(@NonNull View itemView) {
        super(itemView);
        WorkerName = (TextView) itemView.findViewById(R.id.worker_name);
        WorkerPicture = (ImageView) itemView.findViewById(R.id.worker_picture);

    }

    @Override
    public void onClick(View view) {

        listener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemCllickListener listener){
        this.listener = listener;
    }
}

