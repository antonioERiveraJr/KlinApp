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
import com.example.cleaningapplication.Model.Workers;
import com.example.cleaningapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class workerDetails extends AppCompatActivity {

    ImageButton arrowBack;
    Button verify;
    TextView name, birthDate, contactNumber, Address;
    ImageView profile, id,sss,nbi;
    String workerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_details);


        sss = (ImageView) findViewById(R.id.sss);
        nbi = (ImageView) findViewById(R.id.nbi);
        arrowBack = (ImageButton) findViewById(R.id.backButton);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(workerDetails.this, ADMIN.class);
                startActivity(intent);
            }
        });

        verify = (Button) findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Workers").child(workerId).child("workerNotVerified");
                databaseReference.removeValue();
                Intent intent = new Intent(workerDetails.this, ADMIN.class);
                startActivity(intent);
            }
        });

        name = (TextView) findViewById(R.id.worker_name);
        birthDate = (TextView) findViewById(R.id.birthDate);
        contactNumber = (TextView) findViewById(R.id.contact);
        Address = (TextView) findViewById(R.id.address);

        profile = (ImageView) findViewById(R.id.worker_picture);
        id = (ImageView) findViewById(R.id.Id);

        workerId = getIntent().getStringExtra("userId");


        getClientDetails(workerId);
    }

    private void getClientDetails(String workerId) {

        DatabaseReference worker = FirebaseDatabase.getInstance().getReference("Workers").child(workerId);
        worker.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Workers worker = snapshot.getValue(Workers.class);
                    name.setText(worker.getName());
                    birthDate.setText(worker.getBirthDate());
                    contactNumber.setText(worker.getPhoneNumber());
                    Address.setText(worker.getAddress());


                    Picasso.get().load(worker.getProfileImage()).into(profile);
                    Picasso.get().load(worker.getId()).into(id);
                    Picasso.get().load(worker.getNbi()).into(nbi);
                    Picasso.get().load(worker.getSss()).into(sss);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}