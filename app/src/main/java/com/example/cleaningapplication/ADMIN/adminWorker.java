package com.example.cleaningapplication.ADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.cleaningapplication.Model.Client;
import com.example.cleaningapplication.Model.Workers;
import com.example.cleaningapplication.ProductDetails;
import com.example.cleaningapplication.R;
import com.example.cleaningapplication.ViewHolder.ClientViewHolder;
import com.example.cleaningapplication.ViewHolder.WorkerViewHolder;
import com.example.cleaningapplication.menu.WHome;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class adminWorker extends AppCompatActivity {

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
        setContentView(R.layout.activity_admin_worker);

        arrowBack = (ImageButton) findViewById(R.id.arrowback_message);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminWorker.this, ADMIN.class);
                startActivity(intent);
            }
        });
        myitemList = (RecyclerView) findViewById(R.id.myitemsrecyclerview);
        myitemList.setHasFixedSize(true);
        myitemList.setLayoutManager(new LinearLayoutManager(adminWorker.this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Workers");


        FirebaseRecyclerOptions<Workers> options = new FirebaseRecyclerOptions.Builder<Workers>()
                .setQuery(ProductsRef.orderByChild("workerNotVerified").equalTo("yes"), Workers.class).build();

        FirebaseRecyclerAdapter<Workers, WorkerViewHolder> adapter =
                new FirebaseRecyclerAdapter<Workers, WorkerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull WorkerViewHolder workerViewHolder, int i, @NonNull Workers worker) {
                        workerViewHolder.WorkerName.setText(worker.getName());
                        Picasso.get().load(worker.getProfileImage()).into(workerViewHolder.WorkerPicture);

                        workerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(adminWorker.this, workerDetails.class);
                                intent.putExtra("userId", worker.getUserId());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public WorkerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_admin_layout, parent, false);
                        WorkerViewHolder holder = new WorkerViewHolder(view);
                        return holder;
                    }
                };
        myitemList.setAdapter(adapter);
        adapter.startListening();

    }
}
