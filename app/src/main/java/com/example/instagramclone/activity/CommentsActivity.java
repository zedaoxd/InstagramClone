package com.example.instagramclone.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.databinding.ActivityCommentsBinding;
import com.example.instagramclone.model.Comment;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.UserFirebase;

import java.util.Objects;

public class CommentsActivity extends AppCompatActivity {

    private ActivityCommentsBinding binding;
    private String idPost;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        initialSettings();

        settingsToolbar();
        buttonSaveComment();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void initialSettings(){
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idPost = bundle.getString(StringUtils.idPost);
        }

        currentUser = UserFirebase.getDataCurrentUser();
    }

    private void buttonSaveComment(){
        binding.buttonSaveComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String comment = binding.textComment.getText().toString();
                if (!comment.equals("")){
                    Comment comment1 = new Comment();
                    comment1.setIdPost(idPost);
                    comment1.setIdUser(currentUser.getId());
                    comment1.setUserName(currentUser.getName());
                    comment1.setPathPhoto(currentUser.getPathPhoto());
                    comment1.setComment(comment);
                    comment1.save();
                }

                binding.textComment.setText("");
            }
        });
    }

    private void settingsToolbar(){
        setSupportActionBar(binding.includeToolbar.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        getSupportActionBar().setTitle(R.string.comments);
    }
}