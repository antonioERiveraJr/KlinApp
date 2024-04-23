package com.example.cleaningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cleaningapplication.Model.Products;
import com.example.cleaningapplication.Model.Workers;
import com.example.cleaningapplication.menu.WHome;
import com.example.cleaningapplication.menu.WTransaction;
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

public class ProductDetails extends AppCompatActivity {

    private ImageButton arrowBack;
    private EditText productName, productPrice, producDetails, productLocation, productPhoneNumber, postedDate, postedTime, worker1, worker2,
            listersName;
    private ImageView ProductPicture;
    private String productID = "";
    private String theuserID = "";
    private String date;
    private Button messageBTN, startButton, stopButton;

    private String applicantDates = "";
    private String trry;
    private String productRandomKey, saveCurrentDate, saveCurrentTime;
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference, ProductsRef;
    private DatabaseReference checkWorker;
    private DatabaseReference checkCLientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        arrowBack = (ImageButton) findViewById(R.id.arrowback_ProductDetails);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetails.this, WHome.class);
                startActivity(intent);
            }
        });

        productName = (EditText) findViewById(R.id.product_name_details);
        productPrice = (EditText) findViewById(R.id.product_price_details);
        producDetails = (EditText) findViewById(R.id.product_description_details);
        productLocation = (EditText) findViewById(R.id.product_location_details);
        productPhoneNumber = (EditText) findViewById(R.id.sellers_phoneNumber_details);
        postedDate = (EditText) findViewById(R.id.sellers_posted_date);
        postedTime = (EditText) findViewById(R.id.sellers_posted_time);
        listersName = (EditText) findViewById(R.id.ListerName);

        ProductPicture = (ImageView) findViewById(R.id.productImageDetails);

        applicantDates = getIntent().getStringExtra("applicantDate");
        productID = getIntent().getStringExtra("pid");
        theuserID = getIntent().getStringExtra("userId");



        messageBTN = (Button) findViewById(R.id.message_seller_btn);

        getProductDetails(productID,theuserID);

        DatabaseReference removeButton = FirebaseDatabase.getInstance().getReference("Applicants");
        removeButton.orderByChild("Togetter2").equalTo(User.getUid()+productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    messageBTN.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void getProductDetails(String productID/*, String theuserID*/,String theuserID) {
        // Toast.makeText(this, "Request ID: "+productID, Toast.LENGTH_SHORT).show();
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        productsRef.child(/*theuserID + " " + */productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Products products = snapshot.getValue(Products.class);
                    productName.setText(products.getProduct_name());
                    productPrice.setText("₱ " + products.getProduct_price());
                    producDetails.setText(products.getProduct_details());
                    productLocation.setText("Location: " + products.getProduct_location());
                    productPhoneNumber.setText("Contact # " + products.getSellers_contact());
                    productRandomKey = products.getDate() + products.getTime();
                    postedDate.setText("Date Listed: " + products.getDate());
                    postedTime.setText("Working day: " + products.getRequestdate());
                    listersName.setText(products.getLister_shopName());


                    messageBTN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DatabaseReference requestName = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                            requestName.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        Calendar calendar = Calendar.getInstance();

                                        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                                        String saveCurrentDate = currentDate.format(calendar.getTime());

                                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                                        String saveCurrentTime = currentTime.format(calendar.getTime());

                                        String productRandomKey = saveCurrentDate + saveCurrentTime;
                                        HashMap<String, Object> productMaps = new HashMap<>();
                                        productMaps.put("Users",User.getUid());
                                        productMaps.put("Date",productRandomKey);
                                        String nameOfRequest = snapshot.child("product_name").getValue().toString();
                                        productMaps.put("Topic",nameOfRequest);
                                        productMaps.put("Action","You accepted the request "+nameOfRequest + ".");
                                        DatabaseReference logActivity = FirebaseDatabase.getInstance().getReference("Log").child(User.getUid()+productRandomKey);
                                        logActivity.updateChildren(productMaps);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference getProfilePic = FirebaseDatabase.getInstance().getReference("Workers").child(User.getUid()).child("profileImage");
                            getProfilePic.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {


                                        String profilePic = snapshot.getValue().toString();

                                        updateWorkerAcceptedRequest(String.valueOf(products.getProduct_name()), productID, profilePic,theuserID);
                                        Intent intent = new Intent(ProductDetails.this, WTransaction.class);
                                        intent.putExtra("userId", theuserID);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    });

                    Picasso.get().load(products.getImage()).into(ProductPicture);

                    if (products.getUserId().equals(User.getUid())) {
                        messageBTN.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void updateWorkerAcceptedRequest(String productName, String productID, String profilePic,String theuserID) {

        Calendar calendar = Calendar.getInstance();


        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        date = saveCurrentDate + saveCurrentTime;



        HashMap<String, Object> notify = new HashMap<>();
        notify.put("To", theuserID);
        notify.put("Message", " accepted your request ");
        notify.put("Id", productID);
        notify.put("From", User.getUid());
        notify.put("FromPicture", profilePic);
        notify.put("Date", date);
        notify.put("Clicked", "no");
        notify.put("Clicked" + theuserID, "no");
        notify.put("Request", productName);
        DatabaseReference toNotification = FirebaseDatabase.getInstance().getReference("Notification").child(date);
        toNotification.updateChildren(notify);


        DatabaseReference getProfilePic = FirebaseDatabase.getInstance().getReference("Workers");
        getProfilePic.child(User.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {

                    Workers workers = snapshot.getValue(Workers.class);
                    Calendar calendar = Calendar.getInstance();

                    SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                    String saveCurrentDate = currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                    String saveCurrentTime = currentTime.format(calendar.getTime());

                    float finalRating = workers.getRating();
                    int finalCount = workers.getRatingCount();
                    float rating =finalRating/finalCount;

                    String date = saveCurrentDate + saveCurrentTime;
                    HashMap<String, Object> hashMapxs = new HashMap<>();
                    hashMapxs.put("applicantId", User.getUid());
                    hashMapxs.put("Togetter",theuserID+productID);
                    hashMapxs.put("Togetter2",User.getUid()+productID);
                    hashMapxs.put("To", theuserID);
                    hashMapxs.put("requestId", productID);
                    hashMapxs.put("applicantDate", date);
                    hashMapxs.put("applicantImage", workers.getProfileImage());
                    hashMapxs.put("applicantName", workers.getName());
                    hashMapxs.put("applicantRatings", workers.getRating());
                    hashMapxs.put("applicantEmail", workers.getEmail());
                    hashMapxs.put("applicantAddress", workers.getAddress());
                    hashMapxs.put("number", workers.getPhoneNumber());


                    DatabaseReference updateListofWorker = FirebaseDatabase.getInstance().getReference("Applicants").child(User.getUid()+date);
                    updateListofWorker.updateChildren(hashMapxs);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}