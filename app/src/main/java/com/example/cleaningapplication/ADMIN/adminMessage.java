package com.example.cleaningapplication.ADMIN;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cleaningapplication.Adapter.MessageAdminAdapter;
import com.example.cleaningapplication.Fragments.ChatsFragment;
import com.example.cleaningapplication.Fragments.ClientsFragment;
import com.example.cleaningapplication.Fragments.WorkersFragment;
import com.example.cleaningapplication.Model.ChatUsers;
import com.example.cleaningapplication.Model.Client;
import com.example.cleaningapplication.Model.Products;
import com.example.cleaningapplication.Model.Reports;
import com.example.cleaningapplication.Model.messageAdmin;
import com.example.cleaningapplication.ProductDetails;
import com.example.cleaningapplication.R;
import com.example.cleaningapplication.ViewHolder.ClientViewHolder;
import com.example.cleaningapplication.ViewHolder.ReportViewHolder;
import com.example.cleaningapplication.ViewHolder.SellersProductViewHolder;
import com.example.cleaningapplication.ViewYourProductDetails;
import com.example.cleaningapplication.menu.CHome;
import com.example.cleaningapplication.menu.CMessages;
import com.example.cleaningapplication.menu.CNotification;
import com.example.cleaningapplication.menu.CProfiles;
import com.example.cleaningapplication.menu.CTransaction;
import com.example.cleaningapplication.menu.WHome;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class adminMessage extends AppCompatActivity {

    private FirebaseUser User;
    private FirebaseAuth Auth;
    private DatabaseReference databaseReference, ProductsRef;
    private ImageButton ArrowBack;
    private String productID = "";
    private String theuserID = "";

    private ImageView arrowBack;
    RecyclerView myitemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_message);

        arrowBack = (ImageButton) findViewById(R.id.arrowback_message);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminMessage.this, ADMIN.class);
                startActivity(intent);
            }
        });
        myitemList = (RecyclerView) findViewById(R.id.myitemsrecyclerview);
        myitemList.setHasFixedSize(true);
        myitemList.setLayoutManager(new LinearLayoutManager(adminMessage.this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Reports");


        FirebaseRecyclerOptions<Reports> options = new FirebaseRecyclerOptions.Builder<Reports>()
                .setQuery(ProductsRef.orderByChild("report").equalTo("yes"), Reports.class).build();

        FirebaseRecyclerAdapter<Reports, ReportViewHolder> adapter =
                new FirebaseRecyclerAdapter<Reports, ReportViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ReportViewHolder reportViewHolder, int i, @NonNull Reports reports) {
                        reportViewHolder.ReportSubjects.setText(reports.getReportSubject());
                        reportViewHolder.ReportSender.setText(reports.getUserId());

                        reportViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(adminMessage.this, messageDetails.class);
                                intent.putExtra("reportId", reports.getReportId());
                                startActivity(intent);
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_admin_layout, parent, false);
                        ReportViewHolder holder = new ReportViewHolder(view);
                        return holder;
                    }
                };
        myitemList.setAdapter(adapter);
        adapter.startListening();

    }
}
