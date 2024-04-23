package com.example.cleaningapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cleaningapplication.menu.CHome;
import com.example.cleaningapplication.menu.WHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ClientReport extends AppCompatActivity {

    private ImageButton ArrowBack;

    private EditText sendTo, reportSub, reportMessage;
    private Button sendButton;

    public String saveCurrentDate, saveCurrentTime, dt;
    private DatabaseReference databaseReference;
    private FirebaseUser User;
    private FirebaseAuth Auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        ArrowBack = (ImageButton) findViewById(R.id.arrowback_report);
        ArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(ClientReport.this, CHome.class);
                startActivity(intent);
            }
        });

        sendTo = (EditText) findViewById(R.id.sendreportemailto);
        reportSub = (EditText) findViewById(R.id.reportSubject);
        reportMessage = (EditText) findViewById(R.id.reportMessage);

        sendButton = (Button) findViewById(R.id.sendreportBTN);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReportToDatabase();
            }
        });

    }

    private void sendReportToDatabase() {
        String reportsub = reportSub.getText().toString();
        String reportmes = reportMessage.getText().toString();

        if (reportsub.isEmpty()) {
            reportSub.setError("Input ClientReport Subject!");
            Toast.makeText(this, "Enter ClientReport Subject...", Toast.LENGTH_SHORT).show();
        } else if (reportmes.isEmpty()) {
            reportMessage.setError("Input ClientReport WMessages!");
            Toast.makeText(this,"Enter ClientReport WMessages...", Toast.LENGTH_SHORT).show();
        }else{
            send(reportsub, reportmes);
        }
    }

    private void send(String reportsub, String reportmes) {
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        dt = saveCurrentDate + saveCurrentTime;

        HashMap<String, String> reportMap = new HashMap<>();
        reportMap.put("userId", User.getUid());
        reportMap.put("reportSubject", reportsub);
        reportMap.put("reportMessage", reportmes);
        reportMap.put("time", saveCurrentDate);
        reportMap.put("date", saveCurrentTime);
        reportMap.put("report","yes");
        reportMap.put("User", "Client");
        reportMap.put("reportId", dt);

        databaseReference = FirebaseDatabase.getInstance().getReference("Reports").child(dt);
        databaseReference.setValue(reportMap);

        Intent intent = new Intent(ClientReport.this, CHome.class);
        startActivity(intent);
        Toast.makeText(ClientReport.this, "Your report has been sent!", Toast.LENGTH_SHORT).show();
/*
        Intent intent = new Intent(Intent.ACTION_VIEW
                , Uri.parse("mailto:" + sendTo.getText().toString()));
        intent.putExtra(Intent.EXTRA_SUBJECT, reportSub.getText().toString());
        intent.putExtra(Intent.EXTRA_TEXT, reportMessage.getText().toString());
        startActivity(intent);*/

    }


}