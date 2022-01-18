package com.example.instagramclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.instagramclone.R;
import com.example.instagramclone.adapter.AdapterComments;
import com.example.instagramclone.databinding.ActivityCommentsBinding;
import com.example.instagramclone.model.Comment;
import com.example.instagramclone.model.Feed;
import com.example.instagramclone.model.User;
import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CommentsActivity extends AppCompatActivity {

    private ActivityCommentsBinding binding;
    private String idPost;
    private User currentUser;
    private AdapterComments adapterComments;
    private List<Comment> commentList;
    private DatabaseReference firebaseRef;
    private DatabaseReference commentsRef;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        initialSettings();

        settingsToolbar();
        retrieveComments();
        buttonSaveComment();
        settingsRecyclerView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        retrieveComments();
    }

    @Override
    protected void onStop() {
        super.onStop();
        commentsRef.removeEventListener(valueEventListener);
    }

    private void retrieveComments(){

        valueEventListener = commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentList.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    commentList.add(ds.getValue(Comment.class));
                }

                adapterComments.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialSettings(){
        binding = ActivityCommentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idPost = bundle.getString(StringUtils.idPost);
        }

        currentUser = UserFirebase.getDataCurrentUser();
        commentList = new ArrayList<>();
        firebaseRef = FirebaseUtils.getDatabaseReference();
        commentsRef = firebaseRef.child(StringUtils.comments).child(idPost);
    }

    private void settingsRecyclerView(){
        adapterComments = new AdapterComments(getApplicationContext(), commentList);

        binding.recyclerComments.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerComments.setHasFixedSize(true);
        binding.recyclerComments.setAdapter(adapterComments);
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