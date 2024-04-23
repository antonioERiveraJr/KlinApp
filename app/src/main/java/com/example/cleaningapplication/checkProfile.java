package com.example.cleaningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cleaningapplication.ADMIN.adminMessage;
import com.example.cleaningapplication.ADMIN.messageDetails;
import com.example.cleaningapplication.Model.Applicants;
import com.example.cleaningapplication.Model.Comments;
import com.example.cleaningapplication.Model.Notifcation;
import com.example.cleaningapplication.Model.Products;
import com.example.cleaningapplication.Model.Reports;
import com.example.cleaningapplication.Model.Workers;
import com.example.cleaningapplication.ViewHolder.CommentsViewHolder;
import com.example.cleaningapplication.ViewHolder.NotificationViewHolder;
import com.example.cleaningapplication.ViewHolder.ReportViewHolder;
import com.example.cleaningapplication.menu.WNotification;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class checkProfile extends AppCompatActivity {
    private ImageButton ArrowBack;

    private RatingBar ratingBars;
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference;
    TextInputLayout commentLayout;
    private EditText email, address, contact, commentss;
    private TextView lastname, firstname, viewAll;
    private CircleImageView profileImage;
    Dialog myDialog;
    View v;
    private String productID = "";
    private String applicantsId;
    private String applicantDates="";
    private String message, from, date;
    private ImageView fromImage;
    RecyclerView myitemList;
    private Button choose;
    String chosenWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_profile);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        applicantsId = getIntent().getStringExtra("applicantId");
        applicantDates = getIntent().getStringExtra("applicantDate");
        productID = getIntent().getStringExtra("pid");
        chosenWorker = getIntent().getStringExtra("chosenWorker");
        ArrowBack = (ImageButton) findViewById(R.id.Arrowback);

        myitemList = (RecyclerView) findViewById(R.id.comments);
        myitemList.setHasFixedSize(true);
        myitemList.setLayoutManager(new LinearLayoutManager(checkProfile.this));
        ArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(checkProfile.this, ViewYourProductDetails.class);
                intent.putExtra("applicantDate",applicantDates);
                intent.putExtra("chosenWorker", chosenWorker);
                intent.putExtra("pid", productID);
                startActivity(intent);
            }
        });
        choose = (Button) findViewById(R.id.chooseWorker);
        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Requests").child(productID).child("chosenWorker");
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    choose.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());

                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                String saveCurrentTime = currentTime.format(calendar.getTime());

                String productRandomKey = saveCurrentDate + saveCurrentTime;

                Auth = FirebaseAuth.getInstance();
                User = Auth.getCurrentUser();

                DatabaseReference getName = FirebaseDatabase.getInstance().getReference("Workers").child(applicantsId);
                getName.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String nameOfWorker =snapshot.child("name").getValue().toString();
                            DatabaseReference requestName = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                            requestName.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        HashMap<String, Object> productMaps = new HashMap<>();
                                        productMaps.put("Users",User.getUid());
                                        productMaps.put("Date",saveCurrentDate+" "+saveCurrentTime);
                                        productMaps.put("Topic",applicantsId);
                                        String nameOfRequest = snapshot.child("product_name").getValue().toString();
                                        productMaps.put("Action","You've chosen "+nameOfWorker+" as your "+nameOfRequest + " employee.");
                                        DatabaseReference logActivity = FirebaseDatabase.getInstance().getReference("Log").child(User.getUid()+productRandomKey);
                                        logActivity.updateChildren(productMaps);
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



                DatabaseReference removeToPending = FirebaseDatabase.getInstance().getReference("Requests").child(productID).child("pending"+User.getUid());
                removeToPending.removeValue();
                DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Requests").child(productID);
                //
                //notify
                databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Products products = snapshot.getValue(Products.class);
                            Calendar calendar = Calendar.getInstance();
                            DatabaseReference getProfilePic = FirebaseDatabase.getInstance().getReference("Client").child(User.getUid()).child("profileImage");
                            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                            String saveCurrentDate = currentDate.format(calendar.getTime());

                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                            String saveCurrentTime = currentTime.format(calendar.getTime());

                            String date = saveCurrentDate + saveCurrentTime;

                            getProfilePic.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String profilePic = snapshot.getValue().toString();
                                        HashMap<String, Object> notify = new HashMap<>();
                                        notify.put("From", User.getUid());
                                        notify.put("Message", " has chosen you as worker ");
                                        notify.put("Id", productID);
                                        notify.put("To", applicantsId);
                                        notify.put("Clicked" + applicantsId, "no");
                                        notify.put("Clicked", "no");
                                        notify.put("FromPicture", profilePic);
                                        notify.put("Date", date);
                                        notify.put("Request", products.getProduct_name());
                                        DatabaseReference toNotification = FirebaseDatabase.getInstance().getReference("Notification").child(date);
                                        toNotification.updateChildren(notify);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            //notify


                            HashMap<String, Object> productMap = new HashMap<>();
                            if (snapshot.child("category").equals("verifiedmattressCleaning")) {
                                productMap.put("category", "Mattress Cleaning");
                                productMap.put("onGoing" + User.getUid(), "yes");
                                productMap.put("onGoing" + applicantsId, "yes");
                            } else if (snapshot.child("category").equals("verifiedsofaSteam")) {
                                productMap.put("category", "Sofa Steam");
                                productMap.put("onGoing" + User.getUid(), "yes");
                                productMap.put("onGoing" + applicantsId, "yes");
                            } else if (snapshot.child("category").equals("verifiedgeneralCleaning")) {
                                productMap.put("category", "General Cleaning");
                                productMap.put("onGoing" + User.getUid(), "yes");
                                productMap.put("onGoing" + applicantsId, "yes");
                            } else if (snapshot.child("category").equals("verifiedpestControl")) {
                                productMap.put("category", "Pest Control");
                                productMap.put("onGoing" + User.getUid(), "yes");
                                productMap.put("onGoing" + applicantsId, "yes");
                            }

                            databaseReference3.child("verified" + User.getUid()).removeValue();
                            databaseReference3.child("verified").removeValue();
                            databaseReference3.updateChildren(productMap);
                            databaseReference3.child("worker2").removeValue();


                            HashMap<String, Object> productMaps = new HashMap<>();

                            productMaps.put("chosenWorker", applicantsId);
                            productMaps.put("onGoing" + applicantsId, "yes");
                            productMaps.put("onGoing" + User.getUid(), "yes");
                            HashMap<String, Object> productMapsss = new HashMap<>();
                            productMapsss.put("worker available", "yes");

                            databaseReference3.updateChildren(productMapsss);
                            databaseReference = FirebaseDatabase.getInstance().getReference("Requests");
                            databaseReference.child(productID).updateChildren(productMaps);


                        }else{
                            choose.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //
                DatabaseReference setWorker = FirebaseDatabase.getInstance().getReference("Client").child(User.getUid());
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("chosenWorker", applicantsId);
                Intent intent = new Intent(checkProfile.this, ViewYourProductDetails.class);
                intent.putExtra("pid", productID);
                intent.putExtra("applicantDate",applicantDates);
                startActivity(intent);
            }
        });
        CreatepupUpWindow(chosenWorker);


        ratingBars = (RatingBar) findViewById(R.id.rating);

        ratingBars.setFocusable(false);


        email = (EditText) findViewById(R.id.profileEmail);
        address = (EditText) findViewById(R.id.profileAddress);
        contact = (EditText) findViewById(R.id.profileContactNumber);

        firstname = (TextView) findViewById(R.id.fullName);
        firstname.setAllCaps(true);
        profileImage = (CircleImageView) findViewById(R.id.savedProfileImage);


        getApplicants(applicantsId,applicantDates);

    }

    private void getApplicants(String applicantsId,String applicantDates) {


        DatabaseReference report = FirebaseDatabase.getInstance().getReference("Applicants").child(applicantsId + applicantDates);
        report.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    Applicants applicants = snapshot.getValue(Applicants.class);
                    email.setText(applicants.getApplicantEmail());
                    address.setText(applicants.getApplicantAddress());
                    contact.setText(applicants.getNumber());
                    firstname.setText(applicants.getApplicantName());
                    ratingBars.setRating(applicants.getApplicantRatings());
                    Glide.with(getApplicationContext()).load(applicants.getApplicantImage()).into(profileImage);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void CreatepupUpWindow(String chosenWorker) {

        DatabaseReference ProductsRef = FirebaseDatabase.getInstance().getReference().child("Comments");


        FirebaseRecyclerOptions<Comments> options = new FirebaseRecyclerOptions.Builder<Comments>()
                .setQuery(ProductsRef.orderByChild("To").equalTo(chosenWorker), Comments.class).build();

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