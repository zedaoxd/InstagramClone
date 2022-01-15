package com.example.instagramclone.model;

import com.example.instagramclone.util.Base64Utils;
import com.example.instagramclone.util.StringUtils;
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
    private String photoPath;

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

    private Map<String, Object> convertToMap(){

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("email", this.email);
        userMap.put("name", this.name);
        userMap.put("id", this.id);
        userMap.put("pathPhoto", this.photoPath);

        return userMap;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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
