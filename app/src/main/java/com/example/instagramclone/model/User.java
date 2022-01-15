package com.example.instagramclone.model;

import androidx.annotation.NonNull;

import com.example.instagramclone.util.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;
    private String password;
    private String pathPhoto;
    private int followers = 0;
    private int following = 0;
    private int posts = 0;

    public User() {
    }

    public void save(){
        DatabaseReference databaseReference = FirebaseUtils.getDatabaseReference();
        DatabaseReference userRef = databaseReference.child("users").child(this.id);
        userRef.setValue(this);
    }

    public void update(){
        DatabaseReference databaseReference = FirebaseUtils.getDatabaseReference();
        DatabaseReference userRef = databaseReference.child("users").child(this.id);

        userRef.updateChildren(convertToMap());
    }

    @NonNull
    private Map<String, Object> convertToMap(){

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", this.email);
        userMap.put("name", this.name);
        userMap.put("id", this.id);
        userMap.put("pathPhoto", this.pathPhoto);
        userMap.put("followers", this.followers);
        userMap.put("following", this.following);
        userMap.put("posts", this.posts);

        return userMap;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getPosts() {
        return posts;
    }

    public void setPosts(int posts) {
        this.posts = posts;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name.toUpperCase();
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
