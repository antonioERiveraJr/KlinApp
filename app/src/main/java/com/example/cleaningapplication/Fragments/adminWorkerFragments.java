package com.example.cleaningapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningapplication.Adapter.WorkerAdapter;
import com.example.cleaningapplication.Model.Workers;
import com.example.cleaningapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class adminWorkerFragments extends Fragment {
    private RecyclerView recyclerView;

    private WorkerAdapter workerAdapter;
    private List<Workers> mUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.messageRecyclerView_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<>();

        readUsers();


        return view;
    }


    private void readUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Workers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Workers workers1 = snapshot.getValue(Workers.class);
                        assert workers1 != null;

                            mUsers.add(workers1);

                    }
                    workerAdapter = new WorkerAdapter(getContext(), mUsers, false);
                    recyclerView.setAdapter(workerAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
