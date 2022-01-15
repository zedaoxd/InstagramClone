package com.example.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityFriendProfileBinding;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class FriendProfileActivity extends AppCompatActivity {

    private ActivityFriendProfileBinding binding;
    private User selectedUser;
    private User currentUser;

    private DatabaseReference firebaseRef;
    private DatabaseReference usersRef;
    private DatabaseReference friendRef;
    private DatabaseReference currentUserRef;
    private DatabaseReference followersRef;

    private ValueEventListener valueEventListener;
    private String idCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        binding = ActivityFriendProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialSettings();

        retrieveSelectedUser();
        settingsToolbar();
        setPhotoFriend();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recoverFriendProfileData();
        recoverLoggedUserData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        friendRef.removeEventListener(valueEventListener);
    }

    private void initialSettings(){
        binding.includeFragment.profileActionButton.setText(R.string.loading);

        /* Firebase */
        firebaseRef = FirebaseUtils.getDatabaseReference();
        usersRef = firebaseRef.child(StringUtils.users);
        followersRef = firebaseRef.child(StringUtils.followers);

        /* User id */
        idCurrentUser = UserFirebase.getUserId();
    }

    private void verifyUserFollowFriend(){
        DatabaseReference followerRef = followersRef
                .child(idCurrentUser)
                .child(selectedUser.getId());

        followerRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    enableFollowButton(true);
                } else {
                    enableFollowButton(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void enableFollowButton(boolean follow){

        if (follow){

            binding.includeFragment.profileActionButton.setText(R.string.following);

        } else {

            binding.includeFragment.profileActionButton.setText(R.string.follow);

            binding.includeFragment.profileActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveFollower(currentUser, selectedUser);
                }
            });

        }
    }

    private void saveFollower(User currentUser, User userFriend){

        HashMap<String,Object> dataFriend = new HashMap<>();
        dataFriend.put("name", userFriend.getName());
        dataFriend.put("pathPhoto", userFriend.getPathPhoto());

        DatabaseReference followerRef = followersRef
                .child(currentUser.getId())
                .child(userFriend.getId());

        followerRef.setValue(dataFriend);

        binding.includeFragment.profileActionButton.setText(R.string.following);
        binding.includeFragment.profileActionButton.setOnClickListener(null);
    }

    private void retrieveSelectedUser(){
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            selectedUser = (User) bundle.getSerializable(StringUtils.friendProfile);
        }
    }

    private void setPhotoFriend(){
        String pathImage = selectedUser.getPathPhoto();
        if (pathImage != null){
            Uri url = Uri.parse(pathImage);
            Glide.with(getApplicationContext())
                    .load(url)
                    .into(binding.includeFragment.circleImagePerfil);
        } else {
            binding.includeFragment.circleImagePerfil.setImageResource(R.drawable.avatar);
        }
    }

    private void recoverLoggedUserData(){
        currentUserRef = usersRef.child(idCurrentUser);

        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                currentUser = snapshot.getValue(User.class);

                verifyUserFollowFriend();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recoverFriendProfileData(){
        friendRef = usersRef.child(selectedUser.getId());
        valueEventListener = friendRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                String followers = String.valueOf(user.getFollowers());
                String following = String.valueOf(user.getFollowing());
                String posts = String.valueOf(user.getPosts());

                binding.includeFragment.textFollowers.setText(followers);
                binding.includeFragment.textFollowing.setText(following);
                binding.includeFragment.textPublications.setText(posts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void settingsToolbar(){
        setSupportActionBar(binding.includeToolbar.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setTitle(selectedUser.getName());
    }
}