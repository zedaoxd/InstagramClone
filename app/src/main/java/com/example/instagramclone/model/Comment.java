package com.example.instagramclone.model;

import com.example.instagramclone.util.FirebaseUtils;
import com.example.instagramclone.util.StringUtils;
import com.google.firebase.database.DatabaseReference;

public class Comment {
    private String idComment;
    private String idPost;
    private String idUser;
    private String pathPhoto;
    private String userName;
    private String comment;

    public Comment() {
    }

    public boolean save(){

        DatabaseReference firebaseRef = FirebaseUtils.getDatabaseReference()
                .child(StringUtils.comments)
                .child(getIdPost());

        String keyComment = firebaseRef.push().getKey();
        setIdComment(keyComment);

        firebaseRef.child(getIdComment()).setValue(this);

        return true;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
