package com.example.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.adapter.AdapterGridPhotos;
import com.example.instagramclone.databinding.ActivityFriendProfileBinding;
import com.example.instagramclone.model.Post;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FriendProfileActivity extends AppCompatActivity {

    private ActivityFriendProfileBinding binding;
    private User selectedUser;
    private User currentUser;
    private AdapterGridPhotos adapterGridPhotos;

    private DatabaseReference firebaseRef;
    private DatabaseReference usersRef;
    private DatabaseReference friendRef;
    private DatabaseReference currentUserRef;
    private DatabaseReference followersRef;
    private DatabaseReference postsUserRef;

    private ValueEventListener valueEventListener;
    private String idCurrentUser;
    private List<Post> posts;

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
        openPhotoClicked();
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

    private void openPhotoClicked(){
        binding.includeFragment.gridViewPerfil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post post = posts.get(i);
                Intent intent = new Intent(FriendProfileActivity.this, ViewPostActivity.class);
                intent.putExtra(StringUtils.posts, post);
                intent.putExtra(StringUtils.users, selectedUser);
                startActivity(intent);
            }
        });
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

    private void initializeImageLoader(){
        ImageLoaderConfiguration settings = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        ImageLoader.getInstance().init(settings);
    }

    private void loadingPhotosPosts(){
        posts = new ArrayList<>();

        postsUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int gridSize = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridSize / 3;
                binding.includeFragment.gridViewPerfil.setColumnWidth(imageWidth);

                List<Uri> urlPhotos = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    posts.add(post);
                    urlPhotos.add(Uri.parse(post.getPathPhoto()));
                }
                Collections.reverse(posts);

                adapterGridPhotos = new AdapterGridPhotos(getApplicationContext(), R.layout.grid_post, urlPhotos);
                binding.includeFragment.gridViewPerfil.setAdapter(adapterGridPhotos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void verifyUserFollowFriend(){
        DatabaseReference followerRef = followersRef
                .child(selectedUser.getId())
                .child(idCurrentUser);

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

        HashMap<String,Object> dataCurrentUser = new HashMap<>();
        dataCurrentUser.put("name", currentUser.getName());
        dataCurrentUser.put("pathPhoto", currentUser.getPathPhoto());

        DatabaseReference followerRef = followersRef
                .child(userFriend.getId())
                .child(currentUser.getId());

        followerRef.setValue(dataCurrentUser);

        binding.includeFragment.profileActionButton.setText(R.string.following);
        binding.includeFragment.profileActionButton.setOnClickListener(null);

        incrementFollowingLoggedUser(currentUser);
        increaseFriendFollowers(userFriend);
    }

    private void incrementFollowingLoggedUser(User user){

        int following = user.getFollowing() + 1;

        HashMap<String,Object> dataFollowing = new HashMap<>();
        dataFollowing.put("following", following);

        DatabaseReference followingUser = usersRef
                .child(user.getId());

        followingUser.updateChildren(dataFollowing);
    }

    private void increaseFriendFollowers(User friend){
        int followers = friend.getFollowers() + 1;

        HashMap<String,Object> dataFollowers = new HashMap<>();
        dataFollowers.put("followers", followers);

        DatabaseReference followersUser = usersRef
                .child(friend.getId());

        followersUser.updateChildren(dataFollowers);
    }

    private void retrieveSelectedUser(){
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            selectedUser = (User) bundle.getSerializable(StringUtils.friendProfile);
            postsUserRef = FirebaseUtils.getDatabaseReference()
                .child(StringUtils.posts)
                .child(selectedUser.getId());

            initializeImageLoader();
            loadingPhotosPosts();
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