package com.example.instagramclone.model;

import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.StringUtils;
import com.example.instagramclone.util.UserFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

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

    public boolean save(DataSnapshot snapshot){

        Map object = new HashMap();
        User currentUser = UserFirebase.getDataCurrentUser();
        DatabaseReference firebaseRef = FirebaseUtils.getDatabaseReference();

        // reference for post
        String path = "/" + getUserId() + "/" + getId();
        object.put("/" + StringUtils.posts +  path, this);

        // reference for feed
        for (DataSnapshot followers : snapshot.getChildren()){

            String idFollower = followers.getKey();

            HashMap<String, Object> dataFollow = new HashMap<>();
            dataFollow.put("pathPhoto", getPathPhoto());
            dataFollow.put("description", getDescription());
            dataFollow.put("id", getId());
            dataFollow.put("userName", currentUser.getName());
            dataFollow.put("pathPhotoProfileUser", currentUser.getPathPhoto());

            String ids = "/" + idFollower + "/" + getId();
            object.put("/" + StringUtils.feed +  ids, dataFollow);
        }

        firebaseRef.updateChildren(object);
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
