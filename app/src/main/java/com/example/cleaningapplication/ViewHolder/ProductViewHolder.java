package com.example.cleaningapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningapplication.Interface.ItemCllickListener;
import com.example.cleaningapplication.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtProductName, txtSellerNumber, txtProductPrice, txtProductDate, txtListersName,txtWorkerId;
    public ImageView imageView;
    public ItemCllickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_image_layout);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name_layout);
     //   txtSellerNumber = (TextView) itemView.findViewById(R.id.sellers_phonenumber_layout);
       // txtProductPrice = (TextView) itemView.findViewById(R.id.product_price_layout);
      //  txtProductDate = (TextView) itemView.findViewById(R.id.product_posted_date);
      //  txtListersName = (TextView) itemView.findViewById(R.id.listed_name_layout);
        txtWorkerId= (TextView) itemView.findViewById(R.id.product_worker);
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);

    }

    public void setItemClickListener(ItemCllickListener listener){
        this.listener = listener;
    }
}
