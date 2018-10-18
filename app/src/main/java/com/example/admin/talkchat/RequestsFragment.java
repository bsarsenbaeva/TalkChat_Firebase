package com.example.admin.talkchat;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {

    private RecyclerView mRequestList;

    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;
    private DatabaseReference mFriendsReqDatabase;
    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;

    private FirebaseUser mCurrent_User;


    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater.inflate(R.layout.fragment_friends, container, false);

        mRequestList = mMainView.findViewById(R.id.friends_list);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        //new
        mFriendsReqDatabase = FirebaseDatabase.getInstance().getReference().child("friends_req");
        mCurrent_User = FirebaseAuth.getInstance().getCurrentUser();


        mRequestList.setHasFixedSize(true);
        mRequestList.setLayoutManager(new LinearLayoutManager(getContext()));

        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Friends,RequestsFragment.RequestViewHolder> requestRecyclerViewAdapter = new FirebaseRecyclerAdapter<Friends, RequestViewHolder>(
                Friends.class,
                R.layout.users_single_layout,
                RequestViewHolder.class,
                mFriendsDatabase
        ) {
            @Override
            protected void populateViewHolder(final RequestViewHolder requestViewHolder, Friends friends, int i) {

                final String list_user_id = getRef(i).getKey();

                mFriendsReqDatabase.child(mCurrent_User.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(mCurrent_user_id)){

                            String reg_type = dataSnapshot.child(mCurrent_user_id).child("request_type").getValue().toString();

                            final String userName = dataSnapshot.child("name").getValue().toString();
                            String userThumb = dataSnapshot.child("thumb_image").getValue().toString();
                            String status = dataSnapshot.child("status").getValue().toString();

                            if(reg_type.equals("sent")){

                                requestViewHolder.setName(userName);
                                requestViewHolder.setUserImage(userThumb, getContext());
                                requestViewHolder.setUserStatus(status);

                            }
                            if(dataSnapshot.hasChild("online")){

                                String userOnline = dataSnapshot.child("online").getValue().toString();
                                requestViewHolder.setUserOnline(userOnline);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };
        mRequestList.setAdapter(requestRecyclerViewAdapter);

    }
    public static class RequestViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public RequestViewHolder(View itemView){
            super(itemView);

            mView = itemView;
        }

        public void setUserStatus(String status){

            TextView userStatusView = mView.findViewById(R.id.users_single_status);
            userStatusView.setText(status);

        }
        public void setName(String name){

            TextView userNameView = mView.findViewById(R.id.users_single_name);
            userNameView.setText(name);
        }
        public void setUserImage(String thumb_image, Context context){
            CircleImageView userImageView = mView.findViewById(R.id.users_single_avatar);

            Picasso.get().load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);
        }
        public void setUserOnline(String online_status){

            ImageView userOnlineView = mView.findViewById(R.id.user_online_icon);

            if(online_status.equals("true")){

                userOnlineView.setVisibility(View.VISIBLE);
            }else{}

            userOnlineView.setVisibility(View.INVISIBLE);

        }
    }
}
