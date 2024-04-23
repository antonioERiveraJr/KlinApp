package com.example.cleaningapplication.ADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.cleaningapplication.Model.Products;
import com.example.cleaningapplication.ProductDetails;
import com.example.cleaningapplication.R;
import com.example.cleaningapplication.ViewHolder.ProductViewHolder;
import com.example.cleaningapplication.ViewHolder.SellersProductViewHolder;
import com.example.cleaningapplication.ViewYourProductDetails;
import com.example.cleaningapplication.menu.CTransaction;
import com.example.cleaningapplication.menu.WHome;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class adminRequest extends AppCompatActivity {
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference, ProductsRef;
    private ImageButton ArrowBack;
    private String productID = "";
    private String theuserID = "";

    private ImageView arrowBack;
    RecyclerView myitemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request);

        arrowBack = (ImageButton) findViewById(R.id.arrowback_message);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminRequest.this, ADMIN.class);
                startActivity(intent);
            }
        });
        myitemList = (RecyclerView) findViewById(R.id.myitemsrecyclerview);
        myitemList.setHasFixedSize(true);
        myitemList.setLayoutManager(new LinearLayoutManager(adminRequest.this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Requests");


        FirebaseRecyclerOptions<Products> options = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef.orderByChild("check if request is not verified").equalTo("yes"), Products.class).build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull Products products) {
                        productViewHolder.txtProductName.setText(products.getProduct_location());
                     //   productViewHolder.txtProductPrice.setText("₱ " + products.getProduct_price());
                     //   productViewHolder.txtSellerNumber.setText(products.getSellers_contact());
                    //    productViewHolder.txtProductDate.setText(products.getDate());
                     //   productViewHolder.txtListersName.setText("Listed By: " + products.getLister_shopName());
                        Picasso.get().load(products.getImage()).into(productViewHolder.imageView);

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(adminRequest.this, requestDetails.class);
                                intent.putExtra("userId", products.getUserId());
                                intent.putExtra("pid", products.getPid());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };
        myitemList.setAdapter(adapter);
        adapter.startListening();

    }
}
