package com.example.cleaningapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cleaningapplication.Model.Workers;
import com.example.cleaningapplication.menu.WProfiles;
import com.example.cleaningapplication.menu.WHome;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class clientEditProfile extends AppCompatActivity {

    FirebaseAuth Auth;
    FirebaseUser User;
    private ImageButton settingsArrowBack;
    private CircleImageView profileImage;
    private EditText address, phoneNumber, fistname, middlename, lastname;
    private Button updateBTN;
    private TextView changeProfilePic;
    private DatabaseReference databaseReference;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfilePrictureRef;
    private String checker = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_layout);

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("WProfiles pictures");

        settingsArrowBack = (ImageButton) findViewById(R.id.arrowback_settings);
        settingsArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(clientEditProfile.this, WHome.class);
                startActivity(intent);
            }
        });
        profileImage = (CircleImageView) findViewById(R.id.profile_image001);
        changeProfilePic = (TextView) findViewById(R.id.changeProfileImageBTN);
        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                CropImage.activity(imageUri).setAspectRatio(1, 1).start(clientEditProfile.this);
            }
        });

        address = (EditText) findViewById(R.id.change_Address_settingLayout);
        phoneNumber = (EditText) findViewById(R.id.change_phoneNumber_settingLayout);
        fistname = (EditText) findViewById(R.id.change_firstName_settingLayout);
        middlename = (EditText) findViewById(R.id.change_middleName_settingLayout);
        lastname = (EditText) findViewById(R.id.change_lastName_settingLayout);

        updateBTN = (Button) findViewById(R.id.settingUpdateButton);
        updateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checker.equals("clicked")) {
                    updateWithProfilePic();
                } else {
                    updateWithNoProfilePic();
                }
            }
        });

        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Workers").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Workers workers = snapshot.getValue(Workers.class);
                assert workers != null;

                phoneNumber.setText(workers.getPhoneNumber());
                address.setText(workers.getAddress());
                fistname.setText(workers.getName());

                if (workers.getProfileImage().equals("default")) {
                    profileImage.setImageResource(R.drawable.ateammaraylogo);
                } else {
                    Glide.with(getApplicationContext()).load(workers.getProfileImage()).into(profileImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(clientEditProfile.this, "Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateWithProfilePic() {
        String phone = phoneNumber.getText().toString();
        String add = address.getText().toString();
        String last = lastname.getText().toString();
        String first = fistname.getText().toString();
        String middle = middlename.getText().toString();

        if (phone.isEmpty()) {
            phoneNumber.setError("Enter your 11 digit phone number");
            Toast.makeText(clientEditProfile.this, "Please enter your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(add)) {
            address.setError("Enter your address");
            Toast.makeText(clientEditProfile.this, "Please write your address...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(last)) {
            lastname.setError("Enter your lastname");
            Toast.makeText(clientEditProfile.this, "Please write your last name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(first)) {
            fistname.setError("Enter your first name");
            Toast.makeText(clientEditProfile.this, "Please write your first name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(middle)) {
            middlename.setError("Enter your middle name");
            Toast.makeText(clientEditProfile.this, "Please write your middle name / initial...", Toast.LENGTH_SHORT).show();
        }else {
            updateDate(phone, add, last, first, middle);
        }
    }

    private void updateWithNoProfilePic() {
        String phone = phoneNumber.getText().toString();
        String add = address.getText().toString();
        String last = lastname.getText().toString();
        String first = fistname.getText().toString();
        String middle = middlename.getText().toString();

        if (phone.isEmpty()) {
            phoneNumber.setError("Enter your 11 digit phone number");
            Toast.makeText(clientEditProfile.this, "Please enter your phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(add)) {
            address.setError("Enter your address");
            Toast.makeText(clientEditProfile.this, "Please write your address...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(last)) {
            lastname.setError("Enter your lastname");
            Toast.makeText(clientEditProfile.this, "Please write your last name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(first)) {
            fistname.setError("Enter your first name");
            Toast.makeText(clientEditProfile.this, "Please write your first name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(middle)) {
            middlename.setError("Enter your middle name");
            Toast.makeText(clientEditProfile.this, "Please write your middle name / initial...", Toast.LENGTH_SHORT).show();
        }else {
            updateData(phone, add, last, first, middle);
        }
    }

    private void updateData(String phone, String add, String last, String first, String middle) {

        HashMap Users = new HashMap();
        Users.put("phoneNumber", phone);
        Users.put("houseAddress", add);
        Users.put("lastName", last);
        Users.put("firstName", first);
        Users.put("middleName", middle);
        Users.put("search", last);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Workers");
        databaseReference.child(User.getUid()).updateChildren(Users).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(clientEditProfile.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(clientEditProfile.this, WProfiles.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(clientEditProfile.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("ChatUsers");
        HashMap ChatUsers = new HashMap();
        ChatUsers.put("name", last + " " + first);
        ref1.child(User.getUid()).updateChildren(ChatUsers);

    }


    private void updateDate(String phone, String add, String last, String first, String middle) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Update WProfiles");
        progressDialog.setMessage("Please wait, while we are updating your account information");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePrictureRef
                    .child(User.getUid() + ".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Workers");

                        HashMap Users = new HashMap();
                        Users.put("phoneNumber", phone);
                        Users.put("houseAddress", add);
                        Users.put("lastName", last);
                        Users.put("firstName", first);
                        Users.put("middleName", middle);
                        Users.put("search", last);
                        Users.put("profileImage", myUrl);
                        ref.child(User.getUid()).updateChildren(Users);

                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("ChatUsers");
                        HashMap ChatUsers = new HashMap();
                        ChatUsers.put("name", last + " " + first);
                        ChatUsers.put("profileImage", myUrl);
                        ref1.child(User.getUid()).updateChildren(ChatUsers);

                        progressDialog.dismiss();

                        startActivity(new Intent(clientEditProfile.this, WProfiles.class));
                        Toast.makeText(clientEditProfile.this, "WProfiles updated successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(clientEditProfile.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();

            profileImage.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Please Select Image.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(clientEditProfile.this, clientEditProfile.class));
            finish();
        }
    }


}