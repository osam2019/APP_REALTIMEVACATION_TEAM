package com.example.aws.blogapp.Models;

import com.google.firebase.database.ServerValue;

public class Post {


    private String postKey;
    private String title;
    private String description;
    private String description2;
    private String userId;
    private Object timeStamp ;


    public Post(String title, String description, String description2, String userId) {
        this.title = title;
        this.description = description;
        this.description2 = description2;
        this.userId = userId;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

   // make sure to have an empty constructor inside ur model class
    public Post() {
    }


    public String getPostKey() {
        return postKey;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDescription2() { return description2; }

    public String getUserId() {
        return userId;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
