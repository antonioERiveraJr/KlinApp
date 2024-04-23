package com.example.cleaningapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningapplication.Interface.ItemCllickListener;
import com.example.cleaningapplication.R;


public class SellersProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtSellerNumber, txtProductPrice,
            notificationRequestName,notificationRequestDate,notificationRequestWorker,notificationRequestClient,txtProductDate, txtListersName, txtStatus, txtRemarks;
    public ImageView imageView;
    public ItemCllickListener listener;

    public SellersProductViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = (TextView) itemView.findViewById(R.id.product_name_layout_sellers);
        //txtListersName = (TextView) itemView.findViewById(R.id.listed_name_layout_sellers);
        txtStatus = (TextView) itemView.findViewById(R.id.productStatusTXT_sellers);
        txtRemarks = (TextView) itemView.findViewById(R.id.productRemarksTXT_sellers);

        notificationRequestName = (TextView) itemView.findViewById(R.id.request_name_layout);
        notificationRequestDate = (TextView) itemView.findViewById(R.id.request_posted_date);
        notificationRequestWorker = (TextView) itemView.findViewById(R.id.request_worker_txt);
        notificationRequestClient = (TextView) itemView.findViewById(R.id.request_client_txt);

        //String status = txtStatus.getText().toString();

//        String remarks = txtRemarks.getText().toString();

    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemCllickListener listener){
        this.listener = listener;
    }

}
