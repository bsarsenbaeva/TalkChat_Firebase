package com.example.admin.talkchat;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by b.sarsenbaeva on 09.10.2018.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Messages> mMessageList;
    private FirebaseAuth mAuth;
    private DatabaseReference mUsersDatabase;

    public MessageAdapter(List<Messages> mMessageList) {
        this.mMessageList = mMessageList;
        mAuth = FirebaseAuth.getInstance();

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);

        return new MessageViewHolder(v);
    }



    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText,messageTime,messageAuthor;
        public CircleImageView profileImage;

        public MessageViewHolder(@NonNull View view) {
            super(view);

             messageText = view.findViewById(R.id.message_text_layout);
             profileImage = view.findViewById(R.id.message_profile_layout);
             messageAuthor = view.findViewById(R.id.name_text_layout);
             messageTime = view.findViewById(R.id.time_text_layout);

        }
        //new method
        public void setData(Messages messages){

           messageText.setText(messages.getMessage());
        }
    }


    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder viewHolder, int position) {

        String current_user_id = mAuth.getCurrentUser().getUid();

        //Messages c = mMessageList.get(position);

        //String from_user = c.getFrom();

        String user_name = mUsersDatabase.child(current_user_id).child("name").toString();

        String from_user = mMessageList.get(position).getFrom();

        if(from_user.equals(current_user_id)){

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setBackgroundColor(Color.WHITE);
            viewHolder.messageText.setTextColor(Color.BLACK);

        }else{

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);
        }

       // viewHolder.messageText.setText(c.getMessage());
        viewHolder.messageText.setText(mMessageList.get(position).getMessage());//show entered message
        viewHolder.messageAuthor.setText(user_name);
    }



    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


}
