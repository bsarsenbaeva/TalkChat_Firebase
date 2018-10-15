package com.example.admin.talkchat;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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
        public ImageView messageImage;

        public MessageViewHolder(@NonNull View view) {
            super(view);

             messageText = view.findViewById(R.id.message_text_layout);
             profileImage = view.findViewById(R.id.message_profile_layout);
             messageAuthor = view.findViewById(R.id.name_text_layout);
             messageTime = view.findViewById(R.id.time_text_layout);
             messageImage = view.findViewById(R.id.message_image_layout);

        }
        //new method
        public void setData(Messages messages){

           messageText.setText(messages.getMessage());

        }

    }


    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.MessageViewHolder viewHolder, int position) {

        String current_user_id = mAuth.getCurrentUser().getUid();
        String from_user = mMessageList.get(position).getFrom();
        String message_type = mMessageList.get(position).getType();

        DatabaseReference mUserData = mUsersDatabase.child(from_user);

        mUserData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String image = dataSnapshot.child("thumb_image").getValue().toString();

                viewHolder.messageAuthor.setText(name);

                Picasso.get().load(image)
                        .placeholder(R.drawable.default_avatar).into(viewHolder.profileImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //-----------first version of chat, where users have different color of message
        /*if(from_user.equals(current_user_id)){

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setBackgroundColor(Color.WHITE);
            viewHolder.messageText.setTextColor(Color.BLACK);

        }else{

            viewHolder.messageText.setBackgroundResource(R.drawable.message_text_background);
            viewHolder.messageText.setTextColor(Color.WHITE);
        }*/

        if(message_type.equals("text")) {

            viewHolder.messageText.setText(mMessageList.get(position).getMessage());//show entered message
            viewHolder.messageImage.setVisibility(View.INVISIBLE);

        }else{

            viewHolder.messageText.setVisibility(View.INVISIBLE);

            Picasso.get().load(mMessageList.get(position).getMessage())
                    .placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);
        }
    }


    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


}
