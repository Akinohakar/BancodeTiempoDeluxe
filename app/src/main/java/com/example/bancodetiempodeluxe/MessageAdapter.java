package com.example.bancodetiempodeluxe;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.sun.mail.imap.MessageVanishedEvent;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Messages> mMessagesList;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();

    public MessageAdapter(List<Messages> mMessagesList){
        this.mMessagesList=mMessagesList;


    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_single_layout,parent,false);
        return new MessageViewHolder(v);
    }




    public class MessageViewHolder extends RecyclerView.ViewHolder{
        public TextView messageText;
        public CircleImageView imageUser;
        public LinearLayout message;

        public MessageViewHolder(View view){
            super(view);
            messageText=view.findViewById(R.id.message_text_layout);
            imageUser=view.findViewById(R.id.message_profile_image);
            message=view.findViewById(R.id.message_single_layout);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        String current_user_id=mAuth.getCurrentUser().getUid();
        Messages c=mMessagesList.get(position);
        String from_user=c.getFrom();


        if (from_user.equals(current_user_id)){
            holder.messageText.setBackgroundColor(Color.WHITE);
            holder.messageText.setTextColor(Color.BLACK);
            holder.message.setGravity(Gravity.END);
            holder.imageUser.setVisibility(View.INVISIBLE);

        }else{
            holder.imageUser.setVisibility(View.GONE);
            holder.message.setGravity(Gravity.START);
            holder.messageText.setBackgroundResource(R.drawable.mesage_text_background);
            holder.messageText.setTextColor(Color.WHITE);



        }

        holder.messageText.setText(c.getMessage());

    }

    @Override
    public int getItemCount() {
        return mMessagesList.size();
    }

}
