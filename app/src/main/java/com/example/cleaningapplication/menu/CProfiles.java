package com.example.cleaningapplication.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cleaningapplication.ClientLogin;
import com.example.cleaningapplication.ClientReport;
import com.example.cleaningapplication.Model.Client;
import com.example.cleaningapplication.R;
import com.example.cleaningapplication.clientLogActivity;
import com.example.cleaningapplication.workerEditProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class CProfiles extends AppCompatActivity {
    private ImageButton homeButtons,messageButtons,transactionButtons,notificationButtons,editProfileButton,logoutButton;

    private ImageView notify,logAction,reportButton;
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;

    private EditText email, address, contact;
    private TextView shopName, ownersName;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cprofile);
        reportButton = (ImageView) findViewById(R.id.reportMessage);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CProfiles.this, ClientReport.class);
                startActivity(intent);
            }
        });

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        logAction = (ImageView)findViewById(R.id.logActivity);
        logAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CProfiles.this, clientLogActivity.class);
                startActivity(intent);
            }
        });
        DatabaseReference ProductsRefs = FirebaseDatabase.getInstance().getReference().child("Notification");
        ProductsRefs.orderByChild("Clicked" + User.getUid()).equalTo("no").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){
                    notify.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        notify = (ImageView) findViewById(R.id.red_notification);
        //menu


        logoutButton = (ImageButton) findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.signOut();
                startActivity(new Intent(CProfiles.this, ClientLogin.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

            }
        });
        //menu

        homeButtons = (ImageButton) findViewById(R.id.homeButton);
        homeButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CProfiles.this, CHome.class);
                startActivity(intent);
            }
        });
        messageButtons= (ImageButton) findViewById(R.id.messageButton);
        messageButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CProfiles.this, CMessages.class);
                startActivity(intent);
            }
        });
        notificationButtons = (ImageButton) findViewById(R.id.fab);
        notificationButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CProfiles.this, CNotification.class);
                startActivity(intent);
            }
        });
        transactionButtons = (ImageButton) findViewById(R.id.transactionButton);
        transactionButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CProfiles.this, CTransaction.class);
                startActivity(intent);
            }
        });



        email = (EditText) findViewById(R.id.sellerBirthDate);
        address = (EditText) findViewById(R.id.shopAddressTXT);
        contact = (EditText) findViewById(R.id.shopContactNumberTXT);

        ownersName = (TextView) findViewById(R.id.ownersnametxt);
        ownersName.setAllCaps(true);
        profileImage = (CircleImageView) findViewById(R.id.savedProfileImage_seller);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Client").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                assert client != null;
                    ownersName.setText(client.getName());

                    email.setText(client.getbirthDate());
                    address.setText(client.getAddress());
                    contact.setText(client.getPhoneNumber());

                    if (client.getProfileImage().equals("default")) {
                        profileImage.setImageResource(R.drawable.profile);
                    } else {
                        Glide.with(getApplicationContext()).load(client.getProfileImage()).into(profileImage);

                    }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CProfiles.this, "Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
