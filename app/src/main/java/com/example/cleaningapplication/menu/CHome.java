package com.example.cleaningapplication.menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cleaningapplication.CRequest;
import com.example.cleaningapplication.Model.Client;
import com.example.cleaningapplication.R;
import com.example.cleaningapplication.ClientReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CHome extends AppCompatActivity {
    private ImageButton messageButtons, transactionButtons, notificationButtons, profileButtons, reportButton;
    private Button requestButtons;
    private Switch switchButton;
    DatabaseReference ProductsRef;
    private ImageView notify;
    FirebaseUser user;
    RelativeLayout layout;
    private TextView name;
    private CircleImageView image;
    Dialog myDialog,myDialogs;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chome);

        FirebaseAuth Auth = FirebaseAuth.getInstance();
        user = Auth.getCurrentUser();



        name = (TextView) findViewById(R.id.Name);
        image = (CircleImageView)findViewById(R.id.profileImage);
        messageFromAdmin(v);

        DatabaseReference getData = FirebaseDatabase.getInstance().getReference("Client").child(user.getUid());
        getData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                if(snapshot.exists()){
                    name.setText(client.getName());

                    Glide.with(getApplicationContext()).load(client.getProfileImage()).into(image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        notify = (ImageView) findViewById(R.id.red_notification);
        messageButtons = (ImageButton) findViewById(R.id.messageButton);
        messageButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CHome.this, CMessages.class);
                startActivity(intent);
            }
        });
        notificationButtons = (ImageButton) findViewById(R.id.fab);
        notificationButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CHome.this, CNotification.class);
                startActivity(intent);
            }
        });
        transactionButtons = (ImageButton) findViewById(R.id.transactionButton);
        transactionButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CHome.this, CTransaction.class);
                startActivity(intent);
            }
        });
        profileButtons = (ImageButton) findViewById(R.id.profileButton);
        profileButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CHome.this, CProfiles.class);
                startActivity(intent);
            }
        });


        requestButtons = (Button) findViewById(R.id.requestButton);
        requestButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //popUP
                myDialog = new Dialog(CHome.this);
                CreatepupUpWindow(v);
                //

            }
        });


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Notification");
        ProductsRef.orderByChild("Clicked"+user.getUid()).equalTo("no").addValueEventListener(new ValueEventListener() {
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


    }
    public void onBackPressed(){
        Intent backs = new Intent(this, CHome.class);
        startActivity(backs);

    }

    private void messageFromAdmin(View v){
        myDialogs = new Dialog(CHome.this);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Message From Admin").child(user.getUid());

        databaseReference.child("message").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TextView txtclose, message;
                    String messageFromAdmin;

                    messageFromAdmin = snapshot.getValue().toString();
                    myDialogs.setContentView(R.layout.message_from_admin);
                    message = (TextView) myDialogs.findViewById(R.id.message);
                    message.setText(messageFromAdmin);
                    txtclose = (TextView) myDialogs.findViewById(R.id.Gotit);
                    txtclose.setText("GOT IT!");

                    txtclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myDialogs.dismiss();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Message From Admin").child(user.getUid());
                           // Intent intent = new Intent(CHome.this, CHome.class);
                          //  startActivity(intent);
                            databaseReference.removeValue();

                        }
                    });
                    myDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialogs.show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CreatepupUpWindow(View v) {

        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.activity_request_pop_up);
        txtclose = (TextView) myDialog.findViewById(R.id.Gotit);
        txtclose.setText("GOT IT!");
        btnFollow = (Button) myDialog.findViewById(R.id.skipped);

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
                Intent intent = new Intent(CHome.this, CRequest.class);
                startActivity(intent);

            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }


}