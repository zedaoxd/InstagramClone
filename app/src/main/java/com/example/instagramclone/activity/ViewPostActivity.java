package com.example.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityViewPostBinding;
import com.example.instagramclone.model.Post;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.StringUtils;

import java.util.Objects;

public class ViewPostActivity extends AppCompatActivity {

    private ActivityViewPostBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);

        initialSettings();
        settingsToolbar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Post post = (Post) bundle.getSerializable(StringUtils.posts);
            User user = (User) bundle.getSerializable(StringUtils.users);

            displayUserData(user);
            displayPostData(post);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void settingsToolbar(){
        setSupportActionBar(binding.include.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setTitle("Postagem");
    }

    private void displayPostData(Post post){
        Glide.with(getApplicationContext())
                .load(Uri.parse(post.getPathPhoto()))
                .into(binding.includeFeed.imagePostSelectedClicked);

        binding.includeFeed.textDescriptionPost.setText(post.getDescription());

    }

    private void displayUserData(User user){
        binding.includeFeed.textNameProfilePostClicked.setText(user.getName());

        String pathImage = user.getPathPhoto();
        if (pathImage != null){
            Glide.with(getApplicationContext())
                    .load(Uri.parse(pathImage))
                    .into(binding.includeFeed.imageProfilePostClicked);
        }
    }

    private void initialSettings(){
        binding = ActivityViewPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}