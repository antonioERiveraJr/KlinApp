package com.example.cleaningapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cleaningapplication.ChatsActivity;
import com.example.cleaningapplication.Model.Chats;
import com.example.cleaningapplication.Model.Client;
import com.example.cleaningapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {

    private Context mcontext;
    private List<Client> mUsers;
    private boolean isChat;

    public ClientAdapter (Context mcontext, List<Client> mUsers, boolean isChat){
        this.mUsers = mUsers;
        this.mcontext = mcontext;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ClientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.users_item_messages, parent, false);
        return new ClientAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientAdapter.ViewHolder holder, int position) {
        final Client sellers = mUsers.get(position);
        holder.name.setText(sellers.getShopName());
        if(sellers.getProfileImage().equals("default")){
            holder.profile_image.setImageResource(R.drawable.logo);
        }else{
            Glide.with(mcontext).load(sellers.getProfileImage()).into(holder.profile_image);
        }

        if (isChat){
            lastMessage(sellers.getUserId(), holder.last_message);
        }else {
            holder.last_message.setVisibility(View.GONE);
        }

        if (isChat){
            if (sellers.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.INVISIBLE);
            }else{
                holder.img_on.setVisibility(View.INVISIBLE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        }else{
            holder.img_on.setVisibility(View.INVISIBLE);
            holder.img_off.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, ChatsActivity.class);
                intent.putExtra("userId", sellers.getUserId());
                mcontext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_message;

        public ViewHolder(View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.users_name_messsages);
            profile_image = itemView.findViewById(R.id.users_profile_image_messages);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_message = itemView.findViewById(R.id.last_message);

        }

    }

    String theLastMessage;

    private void lastMessage(final String userId, final TextView last_message){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chats chat = snapshot.getValue(Chats.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userId) ||
                            chat.getReceiver().equals(userId) && chat.getSender().equals(firebaseUser.getUid())){
                        theLastMessage = chat.getMessage();
                    }
                }
                switch (theLastMessage){
                    case "default":
                        last_message.setText("No Message");
                        break;
                    default:
                        last_message.setText(theLastMessage);
                        break;
                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
