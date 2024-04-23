package com.example.cleaningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cleaningapplication.Model.Client;
import com.example.cleaningapplication.Model.Products;
import com.example.cleaningapplication.Model.Workers;
import com.example.cleaningapplication.menu.CHome;
import com.example.cleaningapplication.menu.CNotification;
import com.example.cleaningapplication.menu.CTransaction;
import com.example.cleaningapplication.menu.WProfiles;
import com.example.cleaningapplication.menu.WTransaction;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class CRequest extends AppCompatActivity {

    private ImageButton arrowBack;
    private ImageView addProductPicture;
    private Boolean approved = false;

    private Boolean hasWorker = false;
    private Button addProductBTN;
    private EditText productName, productPrice, producDetails, productLocation, ListerPhoneNumber, listerShop;

    private String listers_shopName, saveCurrentDate, saveCurrentTime, product_name,
            product_price, product_details, product_location, product_number, productCategory, approvedOrNot , requestDatetobedone,requester, request_worker1,request_worker;

    private String hasWorkerLoad = "waiting for worker";
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef, databaseReference;
    private ProgressDialog loadingBar;
    private int limitPrice;

    private EditText requestDate;

    final Calendar myCalendar = Calendar.getInstance();
    private FirebaseUser User;
    private FirebaseAuth Auth;

    String[] category = {"Pest Control", "General Cleaning", "Mattress Cleaning", "Sofa Steam"};
    AutoCompleteTextView categortSelector;
    ArrayAdapter<String> adapterItems;
    String categoryItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crequest);

        requestDate = (EditText) findViewById(R.id.request_date_tobedone);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        requestDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(CRequest.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        arrowBack = (ImageButton) findViewById(R.id.arrowback_SellersAddNewProduct);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CRequest.this, CHome.class);
                startActivity(intent);
            }
        });

        addProductPicture = (ImageView) findViewById(R.id.select_product_image);
        addProductPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addProductBTN = (Button) findViewById(R.id.add_new_product);
        addProductBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRequest();
            }
        });

        loadingBar = new ProgressDialog(this);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Request Images");

        categortSelector = findViewById(R.id.itemCategory);
        adapterItems = new ArrayAdapter<>(this, R.layout.categort_list, category);
        categortSelector.setAdapter(adapterItems);

        categortSelector.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categoryItem = parent.getItemAtPosition(position).toString();
            }
        });

        productName = (EditText) findViewById(R.id.product_name);
        productPrice = (EditText) findViewById(R.id.product_price);
        producDetails = (EditText) findViewById(R.id.product_description);
        productLocation = (EditText) findViewById(R.id.product_location);
        ListerPhoneNumber = (EditText) findViewById(R.id.sellers_phoneNumber);
        listerShop = (EditText) findViewById(R.id.add_shop_name);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Client").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                ListerPhoneNumber.setText(client.getPhoneNumber());
                listerShop.setText(client.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CRequest.this, "Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addRequest() {

        requestDatetobedone = requestDate.getText().toString();
        product_name = productName.getText().toString();
        productCategory = categortSelector.getText().toString();
        product_price = productPrice.getText().toString();
        limitPrice = Integer.parseInt(product_price);
        product_details = producDetails.getText().toString();
        product_location = productLocation.getText().toString();
        product_number = ListerPhoneNumber.getText().toString();
        listers_shopName = listerShop.getText().toString();
        if (ImageUri == null) {
            Toast.makeText(this, "Product image is needed...", Toast.LENGTH_SHORT).show();
        } else if (limitPrice < 300) {
            Toast.makeText(this, "Invalid Value", Toast.LENGTH_SHORT).show();
            productPrice.setError("The minimum price is 300");
        } else if (TextUtils.isEmpty(productCategory)) {
            Toast.makeText(this, "Please select category...", Toast.LENGTH_SHORT).show();
            categortSelector.setError("Please select category");
        } else if (TextUtils.isEmpty(product_name)) {
            Toast.makeText(this, "Please input product name...", Toast.LENGTH_SHORT).show();
            productName.setError("Enter Product Name");
        } else if (TextUtils.isEmpty(product_price)) {
            Toast.makeText(this, "Please input product price...", Toast.LENGTH_SHORT).show();
            productPrice.setError("Enter Product Price");
        } else if (TextUtils.isEmpty(product_details)) {
            Toast.makeText(this, "Please input product details...", Toast.LENGTH_SHORT).show();
            producDetails.setError("Enter Product Details");
        } else if (TextUtils.isEmpty(product_location)) {
            Toast.makeText(this, "Please input product location...", Toast.LENGTH_SHORT).show();
            productLocation.setError("Enter Product Location");
        }else if (TextUtils.isEmpty(requestDatetobedone)) {
            Toast.makeText(this, "Please input date..", Toast.LENGTH_SHORT).show();
            requestDate.setError("Enter Date");
        } else {
            StoreProductInformation();
        }
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        requestDate.setText(dateFormat.format(myCalendar.getTime()));
    }


    private void StoreProductInformation() {
        loadingBar.setTitle("Adding New Request");
        loadingBar.setMessage("Loading...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(CRequest.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                /*Toast.makeText(AddNewItem.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();*/
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();
                            SaveProductInfoToDatabase();

                        }

                    }
                });
            }
        });
    }

    private void SaveProductInfoToDatabase() {



        //admin
        if (!approved) {
            approvedOrNot = "waiting for approval";
        } else if (approved) {
            approvedOrNot = "approved";
        }
        //if worker is available
        //  if(!hasWorker){
        //      hasWorkerload = "waiting";
        //  }else if (hasWorker){
        //     hasWorkerload = "worker is available";
        //  }



        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("check if request is not verified", "yes");
        productMap.put("category", productCategory);
        productMap.put("status", hasWorkerLoad);
        productMap.put("remarks", approvedOrNot);
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("worker available", hasWorkerLoad);
        productMap.put("image", downloadImageUrl);
        productMap.put("product_name", product_name);
        productMap.put("product_price", product_price);
        productMap.put("product_details", product_details);
        productMap.put("product_location", product_location);
        productMap.put("sellers_contact", product_number);
        productMap.put("requester", listers_shopName);
        productMap.put("userId", User.getUid());
        productMap.put("pending"+User.getUid(),"yes");
        productMap.put("search", product_name.toLowerCase());
        productMap.put("requestdate", requestDatetobedone);
        productMap.put("worker", request_worker);
        productMap.put("worker1", request_worker1);

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        HashMap<String, Object> productMaps = new HashMap<>();
        productMaps.put("Users",User.getUid());
        productMaps.put("Date",saveCurrentDate+" "+saveCurrentTime);
        productMaps.put("Topic",product_name);
        productMaps.put("Action","You've made a request: "+product_name);

        DatabaseReference logActivity = FirebaseDatabase.getInstance().getReference("Log").child(User.getUid()+productRandomKey);
        logActivity.updateChildren(productMaps);


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        ProductsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CRequest.this, CTransaction.class);
                    intent.putExtra("userId", User.getUid());
                    intent.putExtra("pid", productRandomKey);
                    showProductToWorkers();

                    loadingBar.dismiss();
                    Toast.makeText(CRequest.this, "Request has been added!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(CRequest.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void showProductToWorkers() {

        if (!approved) {
            approvedOrNot = "waiting";
        } else {
            approvedOrNot = "approved";
        }

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("category", productCategory);
        productMap.put("status", hasWorkerLoad);
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("image", downloadImageUrl);
        productMap.put("product_name", product_name);
        productMap.put("product_price", product_price);
        productMap.put("product_details", product_details);
        productMap.put("product_location", product_location);
        productMap.put("sellers_contact", product_number);
        productMap.put("lister_shopName", listers_shopName);
        productMap.put("userId", User.getUid());
        productMap.put("search", product_name.toLowerCase());


        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();


        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Requests");
        ProductsRef.child(productRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CRequest.this, CTransaction.class);
                    Intent toNotify = new Intent(CRequest.this, CNotification.class);
                    toNotify.putExtra("userId", User.getUid());
                    toNotify.putExtra("pid", productRandomKey);
                    intent.putExtra("userId", User.getUid());
                    intent.putExtra("pid", productRandomKey);
                    sendBroadcast(toNotify);
                    startActivity(intent);
                    finish();

                    loadingBar.dismiss();
                    Toast.makeText(CRequest.this, "Request has been added!", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(CRequest.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            addProductPicture.setImageURI(ImageUri);
        }
    }

}