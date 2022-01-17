package com.example.instagramclone.model;

public class Feed {

    private String id;
    private String pathPhoto;
    private String description;
    private String userName;
    private String pathPhotoProfileUser;

    public Feed() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPathPhoto() {
        return pathPhoto;
    }

    public void setPathPhoto(String pathPhoto) {
        this.pathPhoto = pathPhoto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPathPhotoProfileUser() {
        return pathPhotoProfileUser;
    }

    public void setPathPhotoProfileUser(String pathPhotoProfileUser) {
        this.pathPhotoProfileUser = pathPhotoProfileUser;
    }
}
