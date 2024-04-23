package com.example.cleaningapplication.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.cleaningapplication.ADMIN.messageDetails;
import com.example.cleaningapplication.Model.Notifcation;
import com.example.cleaningapplication.Model.Products;
import com.example.cleaningapplication.R;
import com.example.cleaningapplication.ViewHolder.NotificationViewHolder;
import com.example.cleaningapplication.ViewHolder.SellersProductViewHolder;
import com.example.cleaningapplication.ViewRequestDetails;
import com.example.cleaningapplication.ViewYourProductDetails;
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

import java.util.HashMap;

public class WNotification extends AppCompatActivity {
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference, ProductsRef;
    private String message;
    private ImageView arrowBack,fromImage;
    RecyclerView myitemList;
    DatabaseReference getName;
    private ImageButton homeButtons, messageButtons, transactionButtons, notificationButtons, profileButtons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wnotifications);



        homeButtons = (ImageButton) findViewById(R.id.homeButton);
        homeButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WNotification.this, WHome.class);
                startActivity(intent);
            }
        });
        messageButtons = (ImageButton) findViewById(R.id.messageButton);
        messageButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WNotification.this,WMessages.class);
                startActivity(intent);
            }
        });
        transactionButtons = (ImageButton) findViewById(R.id.transactionButton);
        transactionButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WNotification.this, WTransaction.class);

                startActivity(intent);
            }
        });

        profileButtons = (ImageButton) findViewById(R.id.profileButton);
        profileButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WNotification.this, WProfiles.class);
                startActivity(intent);
            }
        });
        myitemList = (RecyclerView) findViewById(R.id.myitemsrecyclerview);
        myitemList.setHasFixedSize(true);
        //myitemList.setLayoutManager(new LinearLayoutManager(WNotification.this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WNotification.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myitemList.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Notification");
        getName = FirebaseDatabase.getInstance().getReference("Client");

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        FirebaseRecyclerOptions<Notifcation> options = new FirebaseRecyclerOptions.Builder<Notifcation>()
                .setQuery(ProductsRef.orderByChild("To").equalTo(User.getUid()), Notifcation.class).build();

        FirebaseRecyclerAdapter<Notifcation, NotificationViewHolder> adapter =
                new FirebaseRecyclerAdapter<Notifcation, NotificationViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int i, @NonNull Notifcation notifcation) {
                        if(notifcation.getClicked().equals("no")){
                            notificationViewHolder.boxColorChanger.setBackgroundResource(R.color.magma_red);
                        }
                        getName.child(notifcation.getFrom()).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String clientsName = snapshot.getValue().toString();

                                message = clientsName + notifcation.getMessage() +"\n REQUEST: " + notifcation.getRequest() + ".\n" + notifcation.getDate();
                                notificationViewHolder.notificationMessage.setText(message);

                                if (clientsName == "Admin") {
                                    notificationViewHolder.profileImage.setImageResource(R.drawable.logo);
                                } else {
                                    Picasso.get().load(notifcation.getFromPicture()).into(notificationViewHolder.profileImage);
                                }
                                notificationViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("Clicked"+User.getUid(), "yes");
                                        hashMap.put("Clicked","yes");
                                        ProductsRef.child(notifcation.getDate()).updateChildren(hashMap);
                                        Intent intent = new Intent(WNotification.this, ViewRequestDetails.class);
                                        intent.putExtra("chosenWorker", notifcation.getFrom());
                                        intent.putExtra("userId", notifcation.getTo());
                                        intent.putExtra("pid", notifcation.getId());
                                        intent.putExtra("Date", notifcation.getDate());
                                        startActivity(intent);
                                    }
                                });
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                    @NonNull
                    @Override
                    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifcation_client_layout, parent, false);
                        NotificationViewHolder holder = new NotificationViewHolder(view);
                        return holder;
                    }
                };
        myitemList.setAdapter(adapter);
        adapter.startListening();

    }
}
