package com.example.cleaningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cleaningapplication.ADMIN.ADMIN;
import com.example.cleaningapplication.menu.CHome;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ClientLogin extends AppCompatActivity {

    private Button loginButton, switchWorker, adminButton;
    private EditText emailEditText, passwordEditText;
    private TextView register, forgotPassword, switchtoUsers;
    private DatabaseReference clients = FirebaseDatabase.getInstance().getReference("Client");
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog loadBar;
    FirebaseAuth Auth;
    FirebaseUser User, SellerAcc;
    // ads
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login_panel);

        switchWorker = (Button) findViewById(R.id.workerButtons);
        switchWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientLogin.this, WorkerLogIn.class);
                startActivity(intent);
            }
        });

        //Initialize firebase
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        SellerAcc = Auth.getCurrentUser();
        //Progress Dialog
        loadBar = new ProgressDialog(this);
        //Button
        loginButton = (Button) findViewById(R.id.sellerloginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(ClientLogin.this,samplepicture.class);
               // startActivity(intent);

                loginUser();
            }
        });
        //EditText
        emailEditText = (EditText) findViewById(R.id.seller_email);
        passwordEditText = (EditText) findViewById(R.id.seller_password);
        //TextViews
        register = (TextView) findViewById(R.id.sellerCreateAccount);
        //  switchtoUsers = (TextView) findViewById(R.id.switchUsers);
        //  forgotPassword = (TextView) findViewById(R.id.sellerforgotPassword);


        register.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                Intent intent = new Intent(ClientLogin.this, clientRegistration.class);
                startActivity(intent);
            }


        });

    }

    private void loginUser() {
        String email = emailEditText.getText().toString();
        String pass = passwordEditText.getText().toString();


        //check if worker
        DatabaseReference checkIfWorker = FirebaseDatabase.getInstance().getReference("Workers");
        checkIfWorker.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    loadBar.dismiss();
                    Toast.makeText(ClientLogin.this, "Your email is signed up as worker", Toast.LENGTH_SHORT).show();

                }else{
                    if (email.equals("admin") && pass.equals("admin")) {
                        Intent intent = new Intent(ClientLogin.this, ADMIN.class);

                        startActivity(intent);

                        //
                    }  else if (!email.matches(emailPattern)) {
                        emailEditText.setError("Enter correct E-mail!");
                    } else if (pass.isEmpty() || pass.length() < 6) {
                        passwordEditText.setError("Enter Password!");
                    } else {
                        loadBar.setTitle("Logging In...");
                        loadBar.setMessage("Please wait, we are currently checking your credentials.");
                        loadBar.setCanceledOnTouchOutside(false);
                        loadBar.show();

                        Auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User = FirebaseAuth.getInstance().getCurrentUser();
                                    if (User.isEmailVerified()) {
                                        loadBar.dismiss();

                                        DatabaseReference isVerifiedAdmin = FirebaseDatabase.getInstance().getReference("Client").child(User.getUid()).child("clientNotVerified");
                                        isVerifiedAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(!snapshot.exists()){

                                                    Intent intent = new Intent(ClientLogin.this, CHome.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else{
                                                    Toast.makeText(ClientLogin.this, "Please wait for admin to verify your account..", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    } else {
                                        User.sendEmailVerification();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ClientLogin.this);
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

                                } else {
                                    loadBar.dismiss();
                                    Toast.makeText(ClientLogin.this, "E-mail / Password is incorrect!", Toast.LENGTH_SHORT).show();
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
        //admin

    }
}