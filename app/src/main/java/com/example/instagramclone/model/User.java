package com.example.instagramclone.model;

import com.example.instagramclone.util.Base64Utils;
import com.example.instagramclone.util.ContsStringUtils;
import com.example.instagramclone.util.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String email;
    private String password;
    private String photoPath;

    public User() {
    }

    public void save(){
        try {

            String userIdentifier = Base64Utils.encode(this.getEmail());
            this.setId(userIdentifier);
            DatabaseReference reference = FirebaseUtils.getDatabaseReference();
            DatabaseReference users = reference
                    .child(ContsStringUtils.users)
                    .child(userIdentifier);
            users.setValue(this);

        } catch (Exception e){

            e.printStackTrace();

        }
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
