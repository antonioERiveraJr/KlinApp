package com.example.cleaningapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningapplication.Interface.ItemCllickListener;
import com.example.cleaningapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicantsViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView applicantName;
    public CircleImageView applicantImage;
    public RatingBar applicantRating;
    public ItemCllickListener listener;


    public ApplicantsViewHolder(@NonNull View itemView) {
        super(itemView);
        applicantName = (TextView) itemView.findViewById(R.id.applicantName);
        applicantImage = (CircleImageView) itemView.findViewById(R.id.applicantImage);
        applicantRating = (RatingBar) itemView.findViewById(R.id.applicantRating);

    }

    @Override
    public void onClick(View view) {

        listener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemCllickListener listener) {
        this.listener = listener;
    }
}