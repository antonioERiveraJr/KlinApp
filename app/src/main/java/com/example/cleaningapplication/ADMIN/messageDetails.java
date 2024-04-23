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

import com.example.cleaningapplication.Model.Reports;
import com.example.cleaningapplication.Model.Workers;
import com.example.cleaningapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class messageDetails extends AppCompatActivity {

    ImageButton arrowBack;
    TextView reportMessage, reportUser, reportSubject, reportSender;

    String reportId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);


        arrowBack = (ImageButton) findViewById(R.id.backButton);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(messageDetails.this, ADMIN.class);
                startActivity(intent);
            }
        });


        reportMessage = (TextView) findViewById(R.id.reportMessage);
        reportUser = (TextView) findViewById(R.id.txtReportUser);
        reportSubject = (TextView) findViewById(R.id.reportSubject);
        reportSender = (TextView) findViewById(R.id.reportSender);


        reportId = getIntent().getStringExtra("reportId");


        getReportDetails(reportId);
    }

    private void getReportDetails(String reportId) {

        DatabaseReference report = FirebaseDatabase.getInstance().getReference("Reports").child(reportId);
        report.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Reports reports = snapshot.getValue(Reports.class);
                    reportMessage.setText(reports.getReportMessage());
                    reportUser.setText(reports.getUser());
                    reportSubject.setText(reports.getReportSubject());
                    reportSender.setText(reports.getUserId());


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}