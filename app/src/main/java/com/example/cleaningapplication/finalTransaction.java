package com.example.cleaningapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class finalTransaction extends AppCompatActivity {
    ProgressDialog loadingBar;
    private Uri beforeUri, afterUri;
    private ImageView beforeImage, afterImage;
    private TextView done;
    private FirebaseUser User;
    private FirebaseAuth Auth;
    private String productID="",downloadAfterUrl, downloadBeforeUrl;
    private String workerIsDone="";
    private StorageReference requestBeforeloc, requestAfterloc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_transaction);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        requestBeforeloc = FirebaseStorage.getInstance().getReference("Request Before");
        requestAfterloc = FirebaseStorage.getInstance().getReference("Request After");
        productID = getIntent().getStringExtra("pid");
        workerIsDone = getIntent().getStringExtra("workerIsDone");
        done = (TextView) findViewById(R.id.Done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage(productID);
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
                            productMaps.put("Date",saveCurrentDate+" "+saveCurrentTime);
                            String nameOfRequest = snapshot.child("product_name").getValue().toString();
                            productMaps.put("Topic",nameOfRequest);
                            productMaps.put("Action","You finished the "+nameOfRequest + " request in "+workerIsDone+" minutes. ");
                            DatabaseReference logActivity = FirebaseDatabase.getInstance().getReference("Log").child(User.getUid()+productRandomKey);
                            logActivity.updateChildren(productMaps);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
        loadingBar = new ProgressDialog(this);
        beforeImage = (ImageView) findViewById(R.id.beforeImage);
        afterImage = (ImageView) findViewById(R.id.afterImage);




    }

    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        beforeUri = data.getData();
                        beforeImage = (ImageView)findViewById(R.id.beforeImage);
                        beforeImage.setImageURI(beforeUri);
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> sActivityResultLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data2 = result.getData();
                        afterUri = data2.getData();
                        afterImage = (ImageView)findViewById(R.id.afterImage);
                        afterImage.setImageURI(afterUri);
                    }
                }
            }
    );
    private void loadImage(String productID){
        Toast.makeText(this, "id"+productID, Toast.LENGTH_SHORT).show();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        String productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = requestBeforeloc.child(beforeUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(beforeUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(finalTransaction.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
                        downloadBeforeUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadBeforeUrl = task.getResult().toString();
                            HashMap<String,Object> hashMapx = new HashMap<>();
                            hashMapx.put("beforeImage", downloadBeforeUrl);
                            DatabaseReference add = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                            add.updateChildren(hashMapx);
                        }

                    }
                });
            }
        });

        final StorageReference filePaths = requestAfterloc.child(afterUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTasks = filePaths.putFile(afterUri);
        uploadTasks.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(finalTransaction.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                /*Toast.makeText(AddNewItem.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();*/
                Task<Uri> urlTask = uploadTasks.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        downloadAfterUrl = filePaths.getDownloadUrl().toString();
                        return filePaths.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadAfterUrl = task.getResult().toString();


                            HashMap<String,Object> hashMaps = new HashMap<>();
                            hashMaps.put("afterImage", downloadAfterUrl);
                            DatabaseReference add = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
                            add.updateChildren(hashMaps);
                        }

                    }
                });
            }
        });
       // HashMap<String,Object> hashMap = new HashMap<>();
      //  hashMap.put("beforeImage", downloadBeforeUrl);
      //  hashMap.put("afterImage", downloadAfterUrl);
      //  DatabaseReference add = FirebaseDatabase.getInstance().getReference("Requests").child(productID);
      //  add.updateChildren(hashMap);
        Intent intent = new Intent(finalTransaction.this, WTransaction.class);
        startActivity(intent);
    }

    public void openFileDialog(View view) {
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.setType("image/*");
        data = Intent.createChooser(data, "Choose a file");
        sActivityResultLauncher.launch(data);
    }

    public void openFileDialog2(View view) {
        Intent data2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data2.setType("image/*");
        data2 = Intent.createChooser(data2, "Choose a file");
        sActivityResultLauncher2.launch(data2);
    }
}