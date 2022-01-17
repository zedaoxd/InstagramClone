package com.example.instagramclone.model;

import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.StringUtils;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Objects;

public class LikesPosts {

    private Feed feed;
    private int quantityLikes;
    private User user;

    public LikesPosts() {
        quantityLikes = 0;
    }

    public void save(){

        HashMap<String, Object> data = new HashMap<>();
        data.put("user", user.getName());
        data.put("pathImage", user.getPathPhoto());

        DatabaseReference firebaseRef = FirebaseUtils.getDatabaseReference();
        DatabaseReference likesRef = firebaseRef
                .child(StringUtils.likesPosts)
                .child(feed.getId())
                .child(user.getId());
        likesRef.setValue(data);

        updateQuantityLike(1);
    }

    public void removeLike(){
        DatabaseReference firebaseRef = FirebaseUtils.getDatabaseReference();
        DatabaseReference likesRef = firebaseRef
                .child(StringUtils.likesPosts)
                .child(feed.getId())
                .child(user.getId());
        likesRef.removeValue();

        updateQuantityLike(-1);
    }

    private void updateQuantityLike(int value){
        DatabaseReference firebaseRef = FirebaseUtils.getDatabaseReference();
        DatabaseReference likesRef = firebaseRef
                .child(StringUtils.likesPosts)
                .child(feed.getId())
                .child(StringUtils.quantityLikes);

        setQuantityLikes(value + getQuantityLikes());
        likesRef.setValue(getQuantityLikes());
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public int getQuantityLikes() {
        return quantityLikes;
    }

    public void setQuantityLikes(int quantityLikes) {
        this.quantityLikes = quantityLikes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
