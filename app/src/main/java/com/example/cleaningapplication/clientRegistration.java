package com.example.cleaningapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class clientRegistration extends AppCompatActivity {


    private Button registerBtn;
    private TextView loginIntsead;

    private Uri profileUri, sssUri, nbiUri, idUri;
    private static final int GalleryPick = 1;

    private EditText fullName, password, confirmPassword, phoneNumber, address, birthDate, email;
    private ImageView identification, sss, nbi;
    private CircleImageView profile;
    private String sssUrl, nbiUrl, idUrl, profileUrl, downloadProfileUrl, downloadsssUrl, downdloadnbiUrl, downloadidUrl;
    private DatabaseReference databaseReference, coinRef;
    private StorageReference registrationProfile,registrationId;
    final Calendar myCalendar = Calendar.getInstance();
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog loadBar;
    DatabaseReference databaseReferences;
    private FirebaseAuth Auth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register_panel);
        //birthdate
        birthDate = (EditText) findViewById(R.id.client_birthday);
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(clientRegistration.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //storage get
        registrationProfile = FirebaseStorage.getInstance().getReference().child("Workers profile");
        registrationId = FirebaseStorage.getInstance().getReference().child("Workers id");
        //Initialize firebase
        Auth = FirebaseAuth.getInstance();

        //Progress Dialog
        loadBar = new ProgressDialog(this);
        //EditText
        email = (EditText) findViewById(R.id.client_email);
        fullName = (EditText) findViewById(R.id.client_full_name);
        password = (EditText) findViewById(R.id.client_password);
        confirmPassword = (EditText) findViewById(R.id.client_confirm_password);
        phoneNumber = (EditText) findViewById(R.id.client_number);
        address = (EditText) findViewById(R.id.client_address);
        //Button
        registerBtn = (Button) findViewById(R.id.registerButton);
        //Images
        identification = (ImageView)findViewById(R.id.client_id);
        profile = (CircleImageView) findViewById(R.id.client_picture);



        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuthentication();
            }
        });
        //Switch to Login Panel
        loginIntsead = (TextView) findViewById(R.id.rgt_logininstead);
        loginIntsead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(clientRegistration.this, WorkerLogIn.class);
                startActivity(intent);
            }
        });

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        birthDate.setText(dateFormat.format(myCalendar.getTime()));
    }
    //IMAGE PROCESS

    ActivityResultLauncher<Intent>  sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Intent data = result.getData();
                        profileUri = data.getData();
                        profile.setImageURI(profileUri);
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> sActivityResultLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Intent data2 = result.getData();
                        idUri = data2.getData();
                        identification.setImageURI(idUri);
                    }
                }
            }
    );
    public void openFileDialog(View view){
        Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data.setType("image/*");
        data = Intent.createChooser(data, "Choose a file");
        sActivityResultLauncher.launch(data);
    }
    public void openFileDialog2(View view){
        Intent data2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        data2.setType("image/*");
        data2 = Intent.createChooser(data2, "Choose a file");
        sActivityResultLauncher2.launch(data2);
    }

    //
    private void PerformAuthentication() {
        String emailAdd = email.getText().toString();
        String name = fullName.getText().toString();
        String pass = password.getText().toString();
        String confirmPass = confirmPassword.getText().toString();
        String phone = phoneNumber.getText().toString();
        String workerAddress = address.getText().toString();
        String birth = birthDate.getText().toString();

        if (profileUri == null) {
            Toast.makeText(this, "Profile image is needed...", Toast.LENGTH_SHORT).show();
        } else if (idUri == null) {
            Toast.makeText(this, "ID image is needed...", Toast.LENGTH_SHORT).show();
        } else if (name.isEmpty()) {
            fullName.setError("Enter Full Name");
            Toast.makeText(this, "Please input your full name...", Toast.LENGTH_SHORT).show();
        } else if (pass.isEmpty()) {
            password.setError("Enter Password!");
            Toast.makeText(this, "Please input your password...", Toast.LENGTH_SHORT).show();
        } else if (!pass.equals(confirmPass)) {
            confirmPassword.setError("Password does not match!");
            Toast.makeText(this, "Password does not match...", Toast.LENGTH_SHORT).show();
        } else if (phone.isEmpty()) {
            phoneNumber.setError("Enter your 11 digit phone number");
            Toast.makeText(this, "Please enter your phone number...", Toast.LENGTH_SHORT).show();
        } else if (workerAddress.isEmpty()) {
            address.setError("Enter your address");
            Toast.makeText(this, "Please enter your address...", Toast.LENGTH_SHORT).show();
        } else if (birth.isEmpty()) {
            birthDate.setError("Enter your birthdate");
            Toast.makeText(this, "Please enter your birthdate...", Toast.LENGTH_SHORT).show();
        } else if (emailAdd.isEmpty()) {
            email.setError("Enter your Email Address");
            Toast.makeText(this, "Please enter your email Address...", Toast.LENGTH_SHORT).show();
        } else {

            imageLoader(name, pass, phone, workerAddress, birth, emailAdd);
        }
    }

    private void imageLoader(String name, String pass, String phone, String workerAddress,
                             String birth, String emailAdd) {


        //imageloader

        //profile
        ProgressDialog loadingBar = new ProgressDialog(this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        String saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String saveCurrentTime = currentTime.format(calendar.getTime());

        String productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = registrationProfile.child(profileUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(profileUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(clientRegistration.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
                        downloadProfileUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadProfileUrl = task.getResult().toString();

                        }

                    }
                });
            }
        });

        final StorageReference filePaths = registrationId.child(idUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTasks = filePaths.putFile(idUri);
        uploadTasks.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(clientRegistration.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
                        downloadidUrl = filePaths.getDownloadUrl().toString();
                        return filePaths.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadidUrl = task.getResult().toString();


                            register(name, pass, phone, workerAddress, birth, emailAdd);
                        }

                    }
                });
            }
        });


        //id

    }


    private void register(String name, String pass, String phone, String workerAddress,
                          String birth, String emailAdd) {

        loadBar.setTitle("Creating Account");
        loadBar.setMessage("Please wait, we are currently checking your credentials.");
        loadBar.setCanceledOnTouchOutside(false);
        loadBar.show();

//
        Auth.createUserWithEmailAndPassword(emailAdd, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if (user != null) {


                        uid = user.getUid();
                    }

                    //

                    databaseReference = FirebaseDatabase.getInstance().getReference("Client").child(uid);

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("email", emailAdd);
                    hashMap.put("userId", uid);
                    hashMap.put("name", name);
                    hashMap.put("phoneNumber", phone);
                    hashMap.put("address", workerAddress);
                    hashMap.put("birthDate", birth);
                    hashMap.put("password", pass);
                    hashMap.put("profileImage", downloadProfileUrl);
                    hashMap.put("id", downloadidUrl);
                    hashMap.put("status", "offline");
                    hashMap.put("user", "worker");
                    hashMap.put("clientNotVerified", "yes");

                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loadBar.dismiss();
                                sendUserToNextActivity();
                                Toast.makeText(clientRegistration.this, "Account Created Successfull!", Toast.LENGTH_SHORT).show();
                            } else {
                                loadBar.dismiss();
                                Toast.makeText(clientRegistration.this, " " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    databaseReference = FirebaseDatabase.getInstance().getReference("ChatUsers").child(uid);
                    HashMap<String, String> chatusermap = new HashMap<>();
                    chatusermap.put("userId", uid);
                    chatusermap.put("name", name);
                    //nottapos
                    chatusermap.put("profileImage", downloadProfileUrl);
                    chatusermap.put("status", "offline");
                    databaseReference.setValue(chatusermap);


                    databaseReferences = FirebaseDatabase.getInstance().getReference("ACCOUNTS");
                    HashMap<Object, Object> accounts = new HashMap<>();
                    accounts.put(phone, uid);
                    databaseReferences.setValue(accounts);

                } else {
                    loadBar.dismiss();
                    Toast.makeText(clientRegistration.this, " " + task.getException(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    private void sendUserToNextActivity() {
        Intent intent = new Intent(clientRegistration.this, ClientLogin.class);
        startActivity(intent);
        finish();
    }

}