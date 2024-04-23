package com.example.cleaningapplication.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cleaningapplication.ClientLogin;
import com.example.cleaningapplication.Model.Comments;
import com.example.cleaningapplication.Model.Workers;
import com.example.cleaningapplication.R;
import com.example.cleaningapplication.ViewHolder.CommentsViewHolder;
import com.example.cleaningapplication.ViewYourProductDetails;
import com.example.cleaningapplication.WorkerLogIn;
import com.example.cleaningapplication.WorkerSendMessageToAdmin;
import com.example.cleaningapplication.checkProfile;
import com.example.cleaningapplication.clientLogActivity;
import com.example.cleaningapplication.workerEditProfile;
import com.example.cleaningapplication.workerLogActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class WProfiles extends AppCompatActivity {

    private ImageButton ArrowBack;

    private ImageView notify,logAction,reportButton;
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;
    TextInputLayout commentLayout;
    private EditText email, address, contact,commentss;
    private TextView lastname, firstname;
    private CircleImageView profileImage;
    private ImageButton homeButtons,messageButtons,transactionButtons,notificationButtons,editProfileButton,logoutButton;

    RecyclerView myitemList;
    private RatingBar ratingBars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wprofile);


        logAction = (ImageView)findViewById(R.id.logActivity);
        logAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WProfiles.this, workerLogActivity.class);
                startActivity(intent);
            }
        });
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        myitemList = (RecyclerView) findViewById(R.id.comments);
        myitemList.setHasFixedSize(true);
        myitemList.setLayoutManager(new LinearLayoutManager(WProfiles.this));

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


        ratingBars = (RatingBar) findViewById(R.id.rating);



        notify = (ImageView) findViewById(R.id.red_notification);
        logoutButton = (ImageButton) findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.signOut();
                startActivity(new Intent(WProfiles.this, WorkerLogIn.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();

            }
        });
        homeButtons = (ImageButton) findViewById(R.id.homeButton);
        homeButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WProfiles.this, WHome.class);
                startActivity(intent);
            }
        });
        messageButtons= (ImageButton) findViewById(R.id.messageButton);
        messageButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WProfiles.this, WMessages.class);
                startActivity(intent);
            }
        });
        notificationButtons = (ImageButton) findViewById(R.id.fab);
        notificationButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WProfiles.this, WNotification.class);
                startActivity(intent);
            }
        });
        transactionButtons = (ImageButton) findViewById(R.id.transactionButton);
        transactionButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WProfiles.this, WTransaction.class);
                startActivity(intent);
            }
        });

        ratingBars.setFocusable(false);


        reportButton = (ImageView) findViewById(R.id.reportMessage);
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WProfiles.this, WorkerSendMessageToAdmin.class);
                startActivity(intent);
            }
        });

        email = (EditText) findViewById(R.id.profileEmail);
        address = (EditText) findViewById(R.id.profileAddress);
        contact = (EditText) findViewById(R.id.profileContactNumber);

        firstname = (TextView) findViewById(R.id.fullName);
        firstname.setAllCaps(true);
        profileImage = (CircleImageView) findViewById(R.id.savedProfileImage);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Workers").child(User.getUid());

        databaseReference.child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String getRating = snapshot.getValue().toString();
                    float finalRating = Float.parseFloat(getRating);
                    databaseReference.child("ratingCount").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                int count = Integer.parseInt(snapshot.getValue().toString());
                                ratingBars.setRating(finalRating/count);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                Workers workers = snapshot.getValue(Workers.class);
                assert workers != null;
                firstname.setText(workers.getName());
                email.setText(User.getEmail());
                address.setText(workers.getAddress());
                contact.setText(workers.getPhoneNumber());




                if(workers.getProfileImage().equals("default")){
                    profileImage.setImageResource(R.drawable.profile);
                }else{
                    Glide.with(getApplicationContext()).load(workers.getProfileImage()).into(profileImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(WProfiles.this,"Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });
        ListOfComments();

    }

    private void ListOfComments() {

        DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Comments");


        FirebaseRecyclerOptions<Comments> options = new FirebaseRecyclerOptions.Builder<Comments>()
                .setQuery(ProductsRef.orderByChild("To").equalTo(User.getUid()), Comments.class).build();

        FirebaseRecyclerAdapter<Comments, CommentsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CommentsViewHolder commentsViewHolder, int i, @NonNull Comments comments) {



                        DatabaseReference getName = FirebaseDatabase.getInstance().getReference("Client").child(comments.getFrom()).child("name");
                        getName.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    String name = snapshot.getValue().toString();
                                    commentsViewHolder.txtMessage.setText(comments.getMessage());
                                    commentsViewHolder.txtDate.setText(comments.getDate());
                                    commentsViewHolder.txtFrom.setText(name);
                                    Picasso.get().load(comments.getFromImage()).into(commentsViewHolder.fromImage);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }

                    @NonNull
                    @Override
                    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_layout, parent, false);
                        CommentsViewHolder holder = new CommentsViewHolder(view);
                        return holder;
                    }
                };
        myitemList.setAdapter(adapter);
        adapter.startListening();

    }
}