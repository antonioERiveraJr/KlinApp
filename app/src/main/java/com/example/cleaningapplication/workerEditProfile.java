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
import com.example.cleaningapplication.Model.Client;
import com.example.cleaningapplication.menu.CProfiles;
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

public class workerEditProfile extends AppCompatActivity {

    FirebaseAuth Auth;
    FirebaseUser User;
    private ImageButton settingsArrowBack;
    private CircleImageView profileImage;
    private EditText shopname, address, phoneNumber, ownersname;
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
        setContentView(R.layout.activity_client_edit_profile);

        storageProfilePrictureRef = FirebaseStorage.getInstance().getReference().child("Client pictures");

        settingsArrowBack = (ImageButton) findViewById(R.id.arrowback_editProfile);
        settingsArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(workerEditProfile.this, CProfiles.class);
                startActivity(intent);
            }
        });

        profileImage = (CircleImageView) findViewById(R.id.profile_image002);
        changeProfilePic = (TextView) findViewById(R.id.changeShopImageBTN);
        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checker = "clicked";
                CropImage.activity(imageUri).setAspectRatio(1, 1).start(workerEditProfile.this);
            }
        });

        shopname = (EditText) findViewById(R.id.change_shopname_seller);
        address = (EditText) findViewById(R.id.change_Address_seller);
        phoneNumber = (EditText) findViewById(R.id.change_phoneNumber_seller);
        ownersname = (EditText) findViewById(R.id.change_ownersname_seller);

        updateBTN = (Button) findViewById(R.id.settingUpdateButton_seller);
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
        databaseReference = FirebaseDatabase.getInstance().getReference("Client").child(User.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Client client = snapshot.getValue(Client.class);
                assert client != null;

                shopname.setText(client.getShopName());
                phoneNumber.setText(client.getPhoneNumber());
                address.setText(client.getAddress());
                ownersname.setText(client.getName());

                if (client.getProfileImage().equals("default")) {
                    profileImage.setImageResource(R.drawable.logo);
                } else {
                    Glide.with(getApplicationContext()).load(client.getProfileImage()).into(profileImage);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(workerEditProfile.this, "Error, please report this bug!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateWithProfilePic() {
        String shop = shopname.getText().toString();
        String phone = phoneNumber.getText().toString();
        String add = address.getText().toString();
        String owner = ownersname.getText().toString();


        if (TextUtils.isEmpty(shop)) {
            shopname.setError("Enter your Shopname");
            Toast.makeText(workerEditProfile.this, "Please Enter your Shop name...", Toast.LENGTH_SHORT).show();
        } else if (phone.isEmpty()) {
            phoneNumber.setError("Enter your 11 digit phone number");
            Toast.makeText(workerEditProfile.this, "Please enter your shop phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(add)) {
            address.setError("Enter your shop address");
            Toast.makeText(workerEditProfile.this, "Please write your shop address...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(owner)) {
            ownersname.setError("Enter owners name");
            Toast.makeText(workerEditProfile.this, "Please write your owners name...", Toast.LENGTH_SHORT).show();
        }else {
            updateDate(shop, phone, add, owner);
        }
    }

    private void updateDate(String shop, String phone, String add, String owner) {
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

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Client");

                        HashMap Sellers = new HashMap();
                        Sellers.put("shopName", shop);
                        Sellers.put("phoneNumber", phone);
                        Sellers.put("shopAddress", add);
                        Sellers.put("ownersName", owner);
                        Sellers.put("search", shop);
                        Sellers.put("profileImage", myUrl);
                        ref.child(User.getUid()).updateChildren(Sellers);

                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("ChatUsers");
                        HashMap ChatUsers = new HashMap();
                        ChatUsers.put("name", shop);
                        ChatUsers.put("profileImage", myUrl);
                        ref1.child(User.getUid()).updateChildren(ChatUsers);

                        progressDialog.dismiss();

                        startActivity(new Intent(workerEditProfile.this, CProfiles.class));
                        Toast.makeText(workerEditProfile.this, "WProfiles updated successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(workerEditProfile.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "image is not selected.", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateWithNoProfilePic() {
        String shop = shopname.getText().toString();
        String phone = phoneNumber.getText().toString();
        String add = address.getText().toString();
        String owner = ownersname.getText().toString();

        if (TextUtils.isEmpty(shop)) {
            shopname.setError("Enter your Shopname");
            Toast.makeText(workerEditProfile.this, "Please Enter your Shop name...", Toast.LENGTH_SHORT).show();
        } else if (phone.isEmpty()) {
            phoneNumber.setError("Enter your 11 digit phone number");
            Toast.makeText(workerEditProfile.this, "Please enter your shop phone number...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(add)) {
            address.setError("Enter your shop address");
            Toast.makeText(workerEditProfile.this, "Please write your shop address...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(owner)) {
            ownersname.setError("Enter owners name");
            Toast.makeText(workerEditProfile.this, "Please write your owners name...", Toast.LENGTH_SHORT).show();
        }else {
            updateData(shop, phone, add, owner);
        }
    }

    private void updateData(String shop, String phone, String add, String owner) {
        HashMap Users = new HashMap();
        Users.put("shopName", shop);
        Users.put("phoneNumber", phone);
        Users.put("shopAddress", add);
        Users.put("ownersName", owner);
        Users.put("search", shop);
        Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Client");
        databaseReference.child(User.getUid()).updateChildren(Users).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(workerEditProfile.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(workerEditProfile.this,  CProfiles.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(workerEditProfile.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("ChatUsers");
        HashMap ChatUsers = new HashMap();
        ChatUsers.put("name", shop);
        ref1.child(User.getUid()).updateChildren(ChatUsers);

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

            startActivity(new Intent(workerEditProfile.this, workerEditProfile.class));
            finish();
        }
    }


}