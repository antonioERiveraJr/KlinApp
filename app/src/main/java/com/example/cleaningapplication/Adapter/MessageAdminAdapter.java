package com.example.cleaningapplication.Adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cleaningapplication.Model.messageAdmin;
import com.example.cleaningapplication.R;

import java.util.ArrayList;

public class MessageAdminAdapter extends RecyclerView.Adapter<MessageAdminAdapter.MessagetoAdminViewHolder> {

    Context context;
    ArrayList<messageAdmin> List;

    public Context getContext() {
        return context;
    }

    public ArrayList<messageAdmin> getList() {
        return List;
    }

    public MessageAdminAdapter(Context context, ArrayList<messageAdmin> list){
        this.context = context;
        this.List = list;
    }


    @NonNull
    @Override
    public MessagetoAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_message_list,parent,false);
        return new MessagetoAdminViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessagetoAdminViewHolder holder, int position) {

        messageAdmin message = List.get(position);
        holder.Message.setText(message.getMessage());
        holder.Time.setText(message.getTime());
        holder.Sender.setText(message.getSender());
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public static class MessagetoAdminViewHolder extends RecyclerView.ViewHolder {

        public TextView Sender;
        public TextView Message;
        public TextView Time;

        public MessagetoAdminViewHolder(@NonNull View itemView) {
            super(itemView);

            Sender = itemView.findViewById(R.id.senderAdmin);
            Message = itemView.findViewById(R.id.messageAdmin);
            Time = itemView.findViewById(R.id.timeAdmin);
        }
    }


}
