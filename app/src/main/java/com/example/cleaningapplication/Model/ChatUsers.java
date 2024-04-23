package com.example.cleaningapplication.Model;

public class ChatUsers {

    private String name, userId, status, profileImage;


    public ChatUsers(){}

    public ChatUsers(String name, String userId, String status, String profileImage) {
        this.name = name;
        this.userId = userId;
        this.status = status;
        this.profileImage = profileImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
