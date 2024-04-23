package com.example.cleaningapplication.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cleaningapplication.Fragments.ChatsFragment;
import com.example.cleaningapplication.Fragments.ClientsFragment;
import com.example.cleaningapplication.Fragments.WorkersFragment;
import com.example.cleaningapplication.Model.ChatUsers;
import com.example.cleaningapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CMessages extends AppCompatActivity {

    private ImageButton ArrowBack;
    private ImageView profile;
    private ImageButton homeButtons, messageButtons, transactionButtons, notificationButtons, profileButtons;

    private ImageView notify;
    FirebaseUser User;
    private FirebaseAuth Auth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmessage);
        FirebaseAuth Auth = FirebaseAuth.getInstance();
        User = Auth.getCurrentUser();
        notify = (ImageView) findViewById(R.id.red_notification);

        //menu

        homeButtons = (ImageButton) findViewById(R.id.homeButton);
        homeButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CMessages.this, CHome.class);
                startActivity(intent);
            }
        });

        notificationButtons = (ImageButton) findViewById(R.id.fab);
        notificationButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CMessages.this, CNotification.class);
                startActivity(intent);
            }
        });
        transactionButtons = (ImageButton) findViewById(R.id.transactionButton);
        transactionButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CMessages.this, CTransaction.class);
                startActivity(intent);
            }
        });
        profileButtons = (ImageButton) findViewById(R.id.profileButton);
        profileButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CMessages.this, CProfiles.class);
                startActivity(intent);
            }
        });

        profile = (ImageView) findViewById(R.id.chats_my_profile_seller);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CMessages.this, CProfiles.class);
                startActivity(intent);
            }
        });
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


        reference = FirebaseDatabase.getInstance().getReference("ChatUsers").child(User.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatUsers users = snapshot.getValue(ChatUsers.class);

                    Glide.with(getApplicationContext()).load(users.getProfileImage()).into(profile);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CMessages.this, "Error", Toast.LENGTH_SHORT).show();
            }

        });


        TabLayout tabLayout = findViewById(R.id.tab_layout_seller);
        ViewPager viewPager = findViewById(R.id.view_pager_seller);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new WorkersFragment(), "Workers");
        viewPagerAdapter.addFragment(new ClientsFragment(), "Client");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }

    private void status(String status) {
        User = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("ChatUsers").child(User.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}