package com.example.cleaningapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningapplication.Interface.ItemCllickListener;
import com.example.cleaningapplication.R;

public class ReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView ReportSubjects, ReportSender;
    public ImageView ReportSenderPicture;
    public ItemCllickListener listener;


    public ReportViewHolder(@NonNull View itemView) {
        super(itemView);
        ReportSubjects = (TextView) itemView.findViewById(R.id.reportSubject);
        ReportSender = (TextView) itemView.findViewById(R.id.reportSender);

    }

    @Override
    public void onClick(View view) {

        listener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemCllickListener listener) {
        this.listener = listener;
    }
}