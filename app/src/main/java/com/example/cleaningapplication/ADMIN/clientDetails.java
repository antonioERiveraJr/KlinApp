package com.example.cleaningapplication.ADMIN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cleaningapplication.Model.Client;
import com.example.cleaningapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class clientDetails extends AppCompatActivity {

    ImageButton arrowBack;
    Button verify;
    TextView name,birthDate,contactNumber,Address;
    ImageView profile,id;
    String clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_details);


        arrowBack = (ImageButton) findViewById(R.id.backButton);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(clientDetails.this, ADMIN.class);
                startActivity(intent);
            }
        });

        verify = (Button) findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Client").child(clientId).child("clientNotVerified");
                databaseReference.removeValue();

                Intent intent = new Intent(clientDetails.this, ADMIN.class);
                startActivity(intent);
            }
        });

        name = (TextView) findViewById(R.id.client_name);
        birthDate = (TextView) findViewById(R.id.birthDate);
        contactNumber = (TextView) findViewById(R.id.contact);
        Address = (TextView) findViewById(R.id.address);

        profile = (ImageView) findViewById(R.id.client_picture);
        id = (ImageView) findViewById(R.id.Id);

        clientId = getIntent().getStringExtra("userId");


        getClientDetails(clientId);
    }

    private void getClientDetails(String clientId) {

        DatabaseReference client = FirebaseDatabase.getInstance().getReference("Client").child(clientId);
        client.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    Client client = snapshot.getValue(Client.class);
                    name.setText(client.getName());
                    birthDate.setText(client.getbirthDate());
                    contactNumber.setText(client.getPhoneNumber());
                    Address.setText(client.getAddress());




                    Picasso.get().load(client.getProfileImage()).into(profile);
                    Picasso.get().load(client.getId()).into(id);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}