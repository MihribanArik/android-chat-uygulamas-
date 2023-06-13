package com.proje.nediyoki;

import android.os.Parcelable;

public class User {
    public  String uid;
    public String userName;
    public String email;
    public String description;

    public User(){

    }

    public User(String uid, String username, String email, String description){
        this.userName = username;
        this.uid = uid;
        this.email = email;
        this.description = description;
    }
}