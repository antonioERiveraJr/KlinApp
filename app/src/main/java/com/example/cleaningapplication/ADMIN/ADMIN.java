package com.example.cleaningapplication.ADMIN;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.cleaningapplication.ClientLogin;
import com.example.cleaningapplication.R;

public class ADMIN extends AppCompatActivity {

    private Button adminCLient,adminRequest,adminWorker,adminMessage;
    private ImageButton arrowBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        arrowBack = (ImageButton) findViewById(R.id.arrowback_message);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ADMIN.this, ClientLogin.class);
                startActivity(intent);
            }
        });


        adminCLient = (Button) findViewById(R.id.clientButton);
        adminCLient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ADMIN.this, adminClient.class);
                startActivity(intent);
            }
        });

        adminMessage = (Button) findViewById(R.id.messageButton);
        adminMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ADMIN.this, adminMessage.class);
                startActivity(intent);
            }
        });

        adminWorker = (Button) findViewById(R.id.workerButton);
        adminWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ADMIN.this, adminWorker.class);
                startActivity(intent);
            }
        });

        adminRequest = (Button) findViewById(R.id.requestButton);
        adminRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ADMIN.this, adminRequest.class);
                startActivity(intent);
            }
        });

    }
}