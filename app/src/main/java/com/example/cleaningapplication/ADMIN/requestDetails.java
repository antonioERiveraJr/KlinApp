package com.example.cleaningapplication.ADMIN;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cleaningapplication.CRequest;
import com.example.cleaningapplication.Model.Products;
import com.example.cleaningapplication.R;
import com.example.cleaningapplication.menu.CHome;
import com.example.cleaningapplication.menu.CTransaction;
import com.example.cleaningapplication.menu.WHome;
import com.example.cleaningapplication.menu.WProfiles;
import com.example.cleaningapplication.menu.WTransaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class requestDetails extends AppCompatActivity {

    private ImageButton arrowBack;
    private EditText productName, productPrice, producDetails, productLocation, productPhoneNumber, postedDate, postedTime, worker1, worker2,
            listersName;
    private ImageView ProductPicture;
    private String productID = "";
    private String theuserID = "";
    private Button messageBTNs, dontVerify;
    private String verify = "yes";
    private TextView price, location, time;
    Dialog myDialog;

    private String trry;
    private String productRandomKey, saveCurrentDate, saveCurrentTime;
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference checkWorker;
    private DatabaseReference checkWorker2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);

        arrowBack = (ImageButton) findViewById(R.id.arrowback_ProductDetails);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requestDetails.this, ADMIN.class);
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

        productID = getIntent().getStringExtra("pid");
        theuserID = getIntent().getStringExtra("userId");

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        messageBTNs = (Button) findViewById(R.id.verify);

        dontVerify = (Button) findViewById(R.id.dontVerify);

        getProductDetails(productID, theuserID);

    }


    private void getProductDetails(String productID/*, String theuserID*/, String theuserID) {
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
                    postedTime.setText("Time Listed: " + products.getTime());
                    listersName.setText(products.getLister_shopName());


                    Picasso.get().load(products.getImage()).into(ProductPicture);

                    dontVerify.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {

                                        //popUP
                                        myDialog = new Dialog(requestDetails.this);
                                        CreatepupUpWindow(v, theuserID, productID);
                                        //


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });

                    messageBTNs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Calendar calendar = Calendar.getInstance();

                            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                            saveCurrentDate = currentDate.format(calendar.getTime());

                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                            saveCurrentTime = currentTime.format(calendar.getTime());

                           String date = saveCurrentDate + saveCurrentTime;

                            DatabaseReference notifyClient = FirebaseDatabase.getInstance().getReference("Notification").child(date);
                            HashMap<String,Object> notify = new HashMap<>();
                            notify.put("To",theuserID);
                            notify.put("Message"," verified your request ");
                            notify.put("Id", productID);
                            notify.put("From","Admin");
                            notify.put("Date",date );
                            notify.put("FromPicture","default");
                            notify.put("Clicked","no");
                            notify.put("Clicked"+theuserID, "no");
                            notify.put("Request",products.getProduct_name());
                            notifyClient.updateChildren(notify);

                            productsRef.child(productID).orderByChild("Mattress Cleaning").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {

                                        String categoryTransaction = "verified"+theuserID;
                                        HashMap<String, Object> productMaps = new HashMap<>();
                                        productMaps.put("category", "verifiedmattressCleaning");
                                        productMaps.put("verified", "yes");
                                        productMaps.put("verifiedtxt","yes");
                                        productMaps.put(categoryTransaction, "yes");
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                                        databaseReference.updateChildren(productMaps);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            productsRef.child(productID).orderByChild("Sofa Steam").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {

                                        HashMap<String, Object> productMaps = new HashMap<>();
                                        productMaps.put("category", "verifiedsofaSteam");
                                        productMaps.put("verified", "yes");
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                                        databaseReference.updateChildren(productMaps);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            productsRef.child(productID).orderByChild("General Cleaning").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {

                                        HashMap<String, Object> productMaps = new HashMap<>();
                                        productMaps.put("category", "verifiedgeneralCleaning");
                                        productMaps.put("verified", "yes");
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                                        databaseReference.updateChildren(productMaps);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            productsRef.child(productID).orderByChild("Pest Control").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {

                                        HashMap<String, Object> productMaps = new HashMap<>();
                                        productMaps.put("category", "verifiedpestControl");
                                        productMaps.put("verified", "yes");
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                                        databaseReference.updateChildren(productMaps);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            productsRef.child(productID).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    productsRef.child(productID).child("check if request is not verified").removeValue();


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            Intent intent = new Intent(requestDetails.this, ADMIN.class);
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void CreatepupUpWindow(View v, String theuserID, String productID) {

        myDialog.setContentView(R.layout.not_verified_popup);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {


                    Products products = snapshot.getValue(Products.class);
                    //location
                    location = (TextView) myDialog.findViewById(R.id.location);
                    location.setText("Location");

                    location.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                            databaseReferences.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Products products = snapshot.getValue(Products.class);
                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("to", theuserID);
                                    hashMap.put("topic", products.getProduct_name());
                                    hashMap.put("message", "Due to a lack of address information, your request " + products.getProduct_name() + " has been declined.");

                                    myDialog.dismiss();
                                    databaseReferences.removeValue();
                                    databaseReference.child("Message From Admin").child(theuserID).updateChildren(hashMap);
                                    Intent intent = new Intent(requestDetails.this, ADMIN.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                    });

                    //Time
                    time = (TextView) myDialog.findViewById(R.id.time);
                    time.setText("Time");

                    time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                            databaseReferences.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Products products = snapshot.getValue(Products.class);
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("to", theuserID);
                                    hashMap.put("topic", products.getProduct_name());


                                    hashMap.put("message", "Due to your request " + products.getProduct_name() + " being made two days prior to the requested date, it has been rejected.");
                                    databaseReference.child("Message From Admin").child(theuserID).updateChildren(hashMap);
                                    myDialog.dismiss();
                                    DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                                    databaseReferences.removeValue();
                                    Intent intent = new Intent(requestDetails.this, ADMIN.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    });

                    //Price
                    price = (TextView) myDialog.findViewById(R.id.price);
                    price.setText("Price");

                    price.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                            databaseReferences.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Products products = snapshot.getValue(Products.class);
                                    DatabaseReference databaseReferences = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("to", theuserID);
                                    hashMap.put("topic", products.getProduct_name());

                                    hashMap.put("message", "Your request " + products.getProduct_name() + " has been turned down because the initial payment amount is unknown.");
                                    databaseReference.child("Message From Admin").child(theuserID).updateChildren(hashMap);
                                    myDialog.dismiss();
                                    databaseReferences.removeValue();
                                    Intent intent = new Intent(requestDetails.this, ADMIN.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });


                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}

