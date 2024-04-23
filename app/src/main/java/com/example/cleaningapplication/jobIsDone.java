package com.example.cleaningapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cleaningapplication.Model.Products;
import com.example.cleaningapplication.Model.Workers;
import com.example.cleaningapplication.menu.CHome;
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

public class jobIsDone extends AppCompatActivity {
    private ImageButton arrowBack;
    private EditText comment;
    private TextInputLayout worker1input;
    private TextInputLayout worker2input;
    private TextView rateCount;
    private TextView showRating;
    private TextView workerInformation;
    private ImageView requestPicture;
    private ProgressDialog loadingBar;
    private String chosenWorker = "";
    private TextView requestName;
    private Button workerButton1;
    private Button submit;
    private float dataRatingFinal;
    private int dataRatingCountFinal;
    private Button workerButton2, transactionCompleteLayout;
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private RatingBar ratingBar;
    boolean hasWorker = false;
    private DatabaseReference databaseReference, databaseReference2, databaseReference3, chosenworker, jobIsDone;
    private String chosen_worker1;
    float rating;
    private String productID = "";
    private String dataRatingCount;
    private String dataRating;
    private String requestId = "";
    private String workersID;
    private String workers = "";


    String commentClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_is_done);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        rateCount = (TextView) findViewById(R.id.numberRating);
        showRating = (TextView) findViewById(R.id.textRating);
        ratingBar = (RatingBar) findViewById(R.id.rating);
        requestName = (TextView) findViewById(R.id.RequestName);
        requestPicture = (ImageView) findViewById(R.id.productImageViewDetails);
        submit = (Button) findViewById(R.id.submitButton);
        productID = getIntent().getStringExtra("pid");
        chosenWorker = getIntent().getStringExtra("chosenWorker");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        databaseReference.child(productID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(jobIsDone.this, "productId:" + productID, Toast.LENGTH_SHORT).show();
                Products products = snapshot.getValue(Products.class);
                requestName.setText(products.getProduct_name());
                Picasso.get().load(products.getImage()).into(requestPicture);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Requests").child(productID);
        databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                workersID = snapshot.child("chosenWorker").getValue().toString();
                DatabaseReference Databaseratingcount = FirebaseDatabase.getInstance().getReference("Workers").child(workersID).child("ratingCount");
                Databaseratingcount.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshots) {
                        dataRatingCount = snapshots.getValue().toString();
                        DatabaseReference Databaserating = FirebaseDatabase.getInstance().getReference("Workers").child(workersID).child("rating");

                        Databaserating.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                dataRating = snapshot.getValue().toString();
                                rating = ratingBar.getRating();
                                //rating
                                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                    @Override
                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {




                                        Toast.makeText(jobIsDone.this, "rating " + rating, Toast.LENGTH_SHORT).show();

                                        if (rating <= 1 && rating > 0) {
                                            showRating.setText("Bad ");
                                            rateCount.setText(rating + " / 5");
                                        } else if (rating <= 2 && rating > 1) {
                                            showRating.setText("Ok ");
                                            rateCount.setText(rating + " / 5");
                                        } else if (rating <= 3 && rating > 2) {
                                            showRating.setText("Good ");
                                            rateCount.setText(rating + " / 5");
                                        } else if (rating <= 4 && rating > 3) {
                                            showRating.setText("Very ");
                                            rateCount.setText(rating + " / 5");
                                        } else if (rating == 5) {
                                            showRating.setText("Best ");
                                            rateCount.setText(rating + " / 5");
                                        }




                                        DatabaseReference getProfilePic = FirebaseDatabase.getInstance().getReference("Client").child(User.getUid()).child("profileImage");
                                        getProfilePic.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(snapshot.exists()){

                                                    String profilePic = snapshot.getValue().toString();
                                                    getRequestDetails(productID, chosenWorker,workersID,dataRating,dataRatingCount,rating,profilePic);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });



                                    }
                                });
                                //rating


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        loadingBar = new ProgressDialog(this);


    }

    /*                      */

    private void getRequestDetails(String productID, String chosenWorker,String workersID,String dataRating,String dataRatingCount,float rating,String profilePic) {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests");
        databaseReference.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Products products = snapshot.getValue(Products.class);
                if (snapshot.exists()) {

                    EditText editText = (EditText) findViewById(R.id.client_comment);
                    String comments = editText.getText().toString();

                    submit = (Button) findViewById(R.id.submitButton);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //notify

                            HashMap<String, Object> productMapss = new HashMap<>();
                            productMapss.put("completed","oo");
                            databaseReference.child(productID).updateChildren(productMapss);



                            Calendar calendar = Calendar.getInstance();

                            SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
                            String saveCurrentDate = currentDate.format(calendar.getTime());

                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                            String saveCurrentTime = currentTime.format(calendar.getTime());
                            String productRandomKey = saveCurrentDate + saveCurrentTime;

                            Auth = FirebaseAuth.getInstance();
                            User = Auth.getCurrentUser();

                            DatabaseReference requestName = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                            requestName.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        HashMap<String, Object> productMaps = new HashMap<>();
                                        productMaps.put("Users",User.getUid());
                                        productMaps.put("Date",saveCurrentDate+" "+saveCurrentTime);
                                        productMaps.put("Topic",productID);
                                        String nameOfRequest = snapshot.child("product_name").getValue().toString();
                                        productMaps.put("Action","You've marked your "+nameOfRequest+" request as completed. ");
                                        DatabaseReference logActivity = FirebaseDatabase.getInstance().getReference("Log").child(User.getUid()+productRandomKey);
                                        logActivity.updateChildren(productMaps);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            String date = saveCurrentDate + saveCurrentTime;

                            HashMap<String,Object> comment = new HashMap<>();
                            comment.put("To",workersID);
                            comment.put("From",User.getUid());
                            comment.put("Date",date);
                            comment.put("Message",comments);
                            comment.put("FromImage",profilePic);

                            DatabaseReference commenter = FirebaseDatabase.getInstance().getReference("Comments").child(date);
                            commenter.updateChildren(comment);


                            HashMap<String,Object> notify = new HashMap<>();
                            notify.put("From",User.getUid());
                            notify.put("Message"," confirmed the payment ");
                            notify.put("Id", productID);
                            notify.put("To",workersID);
                            notify.put("Date",date );
                            notify.put("FromPicture", profilePic);
                            notify.put("Clicked","no");
                            notify.put("Clicked"+workersID, "no");
                            notify.put("Request",products.getProduct_name());
                            DatabaseReference toNotification = FirebaseDatabase.getInstance().getReference("Notification").child(date);
                            toNotification.updateChildren(notify);
                            //notify

                            Toast.makeText(jobIsDone.this, "Comment:" + comments, Toast.LENGTH_SHORT).show();

                            databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    HashMap hashMap = new HashMap();
                                    hashMap.put("taposNaWorker", "oo");
                                    hashMap.put("done"+User.getUid(),"yes");
                                    hashMap.put("done"+workersID,"yes");


                                    assert workers != null;
                                    databaseReference3.child("onGoing"+workersID).removeValue();
                                    databaseReference3.child("onGoing"+User.getUid()).removeValue();
                                    databaseReference3.updateChildren(hashMap);

                                    Auth = FirebaseAuth.getInstance();
                                    databaseReference = FirebaseDatabase.getInstance().getReference("Workers").child(workersID);
                                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot snapshot) {

                                            Toast.makeText(jobIsDone.this, "worker:" + workersID, Toast.LENGTH_SHORT).show();


                                            dataRatingCountFinal = Integer.parseInt(dataRatingCount);
                                            int dataRatingCountFinals = dataRatingCountFinal + 1;

                                            dataRatingFinal = Float.parseFloat(dataRating);
                                            dataRatingFinal = (dataRatingFinal + rating) ;
                                            Toast.makeText(jobIsDone.this, "rate : " + dataRatingFinal + "\n" + "rateCount: " + dataRatingCountFinals, Toast.LENGTH_SHORT).show();
                                            float dataRatingFinals = dataRatingFinal;


                                            HashMap<String, Object> hashmap = new HashMap<>();
                                            hashmap.put("Comment", comments);
                                            hashmap.put("rating", dataRatingFinals);
                                            hashmap.put("ratingCount", dataRatingCountFinals);
                                            hashmap.put("finalRating", dataRatingFinals/dataRatingCountFinals);
                                            databaseReference.updateChildren(hashmap);




                                            if (comments == null) {
                                                Toast.makeText(jobIsDone.this, "Please leave a comment", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(jobIsDone.this, "hays", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                }
                            });
                            Intent intent = new Intent(jobIsDone.this, CHome.class);
                            //  databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child(productID);
                            // databaseReference.removeValue();
                            startActivity(intent);
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(jobIsDone.this, "hmmmmm", Toast.LENGTH_SHORT).show();
            }
        });

    }


}


/*               if (snapshot.exists()) {
                String workersId = snapshot.child("chosenWorker").getValue().toString();


                    EditText editText = (EditText) findViewById(R.id.client_comment);
                    String comments = editText.getText().toString();

                    Toast.makeText(jobIsDone.this, "productId:" + productID, Toast.LENGTH_SHORT).show();
                    Products products = snapshot.getValue(Products.class);
                    requestName.setText(products.getProduct_name());
                    Picasso.get().load(products.getImage()).into(requestPicture);

                    submit = (Button) findViewById(R.id.submitButton);

                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            //comments
                            DatabaseReference commentWorker = FirebaseDatabase.getInstance().getReference("Workers").child(workersId);

                            Toast.makeText(jobIsDone.this, "Comment:" + comments, Toast.LENGTH_SHORT).show();

                            Toast.makeText(jobIsDone.this, "worker:" + workersId, Toast.LENGTH_SHORT).show();

                            HashMap<String, Object> hashmaps = new HashMap<>();
                            hashmaps.put("Comment", comments);
                            commentWorker.updateChildren(hashmaps);


                            Intent intent = new Intent(jobIsDone.this, CHome.class);
                            //  databaseReference = FirebaseDatabase.getInstance().getReference().child("Requests").child(productID);
                            // databaseReference.removeValue();
                            startActivity(intent);

                            //comment


                        }
                    });


                    databaseReference3 = FirebaseDatabase.getInstance().getReference().child("Requests").child(productID);

                    databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override

                        public void onDataChange(DataSnapshot snapshot) {
                            HashMap hashMap = new HashMap();
                            hashMap.put("taposNaWorker", "oo");
                            databaseReference3.updateChildren(hashMap);

                            assert workers != null;
                            String workersID = snapshot.child("chosenWorker").getValue().toString();

                            Toast.makeText(jobIsDone.this, "tryy" + workersID, Toast.LENGTH_SHORT).show();
                            Auth = FirebaseAuth.getInstance();
                            databaseReference = FirebaseDatabase.getInstance().getReference("Workers").child(workersID);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {




                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(jobIsDone.this, "hays", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(jobIsDone.this, "hmmmmm", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
*/