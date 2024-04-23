package com.example.cleaningapplication.ADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.cleaningapplication.Model.Client;
import com.example.cleaningapplication.Model.Products;
import com.example.cleaningapplication.ProductDetails;
import com.example.cleaningapplication.R;
import com.example.cleaningapplication.ViewHolder.ClientViewHolder;
import com.example.cleaningapplication.ViewHolder.ProductViewHolder;
import com.example.cleaningapplication.menu.WHome;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class adminClient extends AppCompatActivity {

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference, ProductsRef;
    private ImageButton ArrowBack;
    private String productID = "";
    private String theuserID = "";

    private ImageView arrowBack,clientProfile;
    RecyclerView myitemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_client);

        arrowBack = (ImageButton) findViewById(R.id.arrowback_message);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminClient.this, ADMIN.class);
                startActivity(intent);
            }
        });
        myitemList = (RecyclerView) findViewById(R.id.myitemsrecyclerview);
        myitemList.setHasFixedSize(true);
        myitemList.setLayoutManager(new LinearLayoutManager(adminClient.this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Client");


        FirebaseRecyclerOptions<Client> options = new FirebaseRecyclerOptions.Builder<Client>()
                .setQuery(ProductsRef.orderByChild("clientNotVerified").equalTo("yes"), Client.class).build();

        FirebaseRecyclerAdapter<Client, ClientViewHolder> adapter =
                new FirebaseRecyclerAdapter<Client, ClientViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ClientViewHolder clientViewHolder, int i, @NonNull Client client) {
                        clientViewHolder.ClientName.setText(client.getName());
                        clientProfile = (ImageView) findViewById(R.id.client_picture);
                        Picasso.get().load(client.getProfileImage()).into(clientViewHolder.ClientPicture);

                        clientViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(adminClient.this, clientDetails.class);
                                intent.putExtra("userId", client.getUserId());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.client_admin_layout, parent, false);
                        ClientViewHolder holder = new ClientViewHolder(view);
                        return holder;
                    }
                };
        myitemList.setAdapter(adapter);
        adapter.startListening();

    }
}
