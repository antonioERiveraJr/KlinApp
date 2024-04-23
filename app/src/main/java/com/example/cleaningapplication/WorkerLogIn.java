package com.example.cleaningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cleaningapplication.menu.CHome;
import com.example.cleaningapplication.menu.WHome;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkerLogIn extends AppCompatActivity {

    private Button loginButton,clientButton;
    private EditText emailEditText, passwordEditText;
    private TextView register;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog loadBar;

    FirebaseAuth Auth;
    FirebaseUser User ,SellerAcc;
    // ads
    private AdView mAdView;

    private String parentDbName = "Workers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_login_panel);
        // Banner Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        //Initialize firebase
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        SellerAcc = Auth.getCurrentUser();
        //Progress Dialog
        loadBar = new ProgressDialog(this);
        //Button
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        //EditText
        emailEditText = (EditText) findViewById(R.id.input_username);
        passwordEditText = (EditText) findViewById(R.id.input_password);
        //TextViews

        register = (TextView) findViewById(R.id.CreateAccountTextView);
        //Register Panel On click Listener
      register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkerLogIn.this, workerRegistration.class);
                startActivity(intent);
            }
        });

      clientButton = (Button) findViewById(R.id.clientButtons);
        clientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WorkerLogIn.this, ClientLogin.class);
                startActivity(intent);
            }
        });

    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String pass = passwordEditText.getText().toString();
        //check if client
        DatabaseReference checkIfWorker = FirebaseDatabase.getInstance().getReference("Client");
        checkIfWorker.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    loadBar.dismiss();
                    Toast.makeText(WorkerLogIn.this, "Your email is signed up as client", Toast.LENGTH_SHORT).show();

                }else{
                    final DatabaseReference RootRef;
                    RootRef = FirebaseDatabase.getInstance().getReference().child("Workers");

                    if (!email.matches(emailPattern)) {
                        emailEditText.setError("Enter correct E-mail!");
                    } else if (pass.isEmpty() || pass.length() < 6) {
                        passwordEditText.setError("Wrong Password!");
                    } else {
                        loadBar.setTitle("Logging In...");
                        loadBar.setMessage("Please wait, we are currently checking your credentials.");
                        loadBar.setCanceledOnTouchOutside(false);
                        loadBar.show();

                        Auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    User = FirebaseAuth.getInstance().getCurrentUser();
                                    if(User.isEmailVerified()){

                                        DatabaseReference isVerifiedAdmin = FirebaseDatabase.getInstance().getReference("Workers").child(User.getUid()).child("workerNotVerified");
                                        isVerifiedAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(!snapshot.exists()){
                                                    loadBar.dismiss();
                                                    Intent intent = new Intent(WorkerLogIn.this, WHome.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else{
                                                    loadBar.dismiss();
                                                    Toast.makeText(WorkerLogIn.this, "Please wait for admin to verify your account..", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }else{
                                        User.sendEmailVerification();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(WorkerLogIn.this);
                                        builder.setTitle("E-mail Verification")
                                                .setMessage("Check your email to verify your account before Logging in.")
                                                .setCancelable(true)
                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        loadBar.dismiss();
                                                        dialog.cancel();
                                                    }
                                                }).show();
                                    }

                                }else{
                                    loadBar.dismiss();
                                    Toast.makeText(WorkerLogIn.this, "E-mail / Password is incorrect!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}