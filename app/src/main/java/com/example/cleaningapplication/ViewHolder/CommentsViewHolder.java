package com.example.cleaningapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningapplication.Interface.ItemCllickListener;
import com.example.cleaningapplication.R;

public class CommentsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtFrom, txtTo, txtDate, txtMessage;
    public ImageView fromImage;
    public ItemCllickListener listener;

    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        txtFrom = (TextView) itemView.findViewById(R.id.fromName);
        txtMessage = (TextView) itemView.findViewById(R.id.commentsReviews);
        fromImage = (ImageView) itemView.findViewById(R.id.fromProfile);
        txtDate = (TextView) itemView.findViewById(R.id.Date);



    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);

    }

    public void setItemClickListener(ItemCllickListener listener){
        this.listener = listener;
    }
}
