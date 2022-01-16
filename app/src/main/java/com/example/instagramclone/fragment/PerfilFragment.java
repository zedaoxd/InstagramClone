package com.example.instagramclone.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.activity.EditPerfilActivity;
import com.example.instagramclone.adapter.AdapterGridPhotos;
import com.example.instagramclone.databinding.FragmentPerfilBinding;
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
import java.util.List;

public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private User currentUser;
    private DatabaseReference postCurrentUserRef;
    private DatabaseReference userRef;
    private DatabaseReference usersRef;
    private DatabaseReference firebaseRef;
    private ValueEventListener valueEventListener;

    public PerfilFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        initialSettings();
        clickButtonEditPerfil();
        retrieveCurrentUser();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        recoverUserProfileData();
    }

    @Override
    public void onStop() {
        super.onStop();
        userRef.removeEventListener(valueEventListener);
    }

    private void initialSettings(){
        firebaseRef = FirebaseUtils.getDatabaseReference();
        usersRef = firebaseRef.child(StringUtils.users);
    }

    private void recoverUserProfileData(){
        userRef = usersRef.child(currentUser.getId());
        valueEventListener = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                String followers = String.valueOf(user.getFollowers());
                String following = String.valueOf(user.getFollowing());
                String posts = String.valueOf(user.getPosts());
                String pathPhoto = user.getPathPhoto();
                Uri url = Uri.parse(pathPhoto);

                binding.textFollowers.setText(followers);
                binding.textFollowing.setText(following);
                binding.textPublications.setText(posts);
                if (url != null){
                    Glide.with(getActivity())
                            .load(url)
                            .into(binding.circleImagePerfil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveCurrentUser(){
        currentUser = UserFirebase.getDataCurrentUser();
        postCurrentUserRef = FirebaseUtils.getDatabaseReference()
                .child(StringUtils.posts)
                .child(currentUser.getId());

        initializeImageLoader();
        loadingPhotosPosts();
    }

    private void loadingPhotosPosts(){
        postCurrentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int gridSize = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridSize / 3;
                binding.gridViewPerfil.setColumnWidth(imageWidth);


                List<Uri> urlPhotos = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    urlPhotos.add(Uri.parse(post.getPathPhoto()));
                }

                AdapterGridPhotos adapterGridPhotos = new AdapterGridPhotos(getActivity(), R.layout.grid_post, urlPhotos);
                binding.gridViewPerfil.setAdapter(adapterGridPhotos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeImageLoader(){
        ImageLoaderConfiguration settings = new ImageLoaderConfiguration
                .Builder(getActivity())
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();

        ImageLoader.getInstance().init(settings);
    }

    private void clickButtonEditPerfil(){
        binding.profileActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), EditPerfilActivity.class));
            }
        });
    }
}