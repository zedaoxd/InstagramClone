package com.example.instagramclone.model;

import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.StringUtils;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Post implements Serializable {
    private String id;
    private String userId;
    private String description;
    private String pathPhoto;

    public Post() {
        DatabaseReference reference = FirebaseUtils.getDatabaseReference();
        DatabaseReference postRef = reference.child(StringUtils.posts);
        this.id = postRef.push().getKey();
    }

    public boolean save(){
        DatabaseReference reference = FirebaseUtils.getDatabaseReference();
        DatabaseReference postRef = reference
                .child(StringUtils.posts)
                .child(this.userId)
                .child(this.id);

        postRef.setValue(this);
        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }
}
