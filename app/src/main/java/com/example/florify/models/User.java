package com.example.florify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {
    public String username;
    public String password;
    public String id;

    @Exclude
    public String key;
    public String email, location;
    public ArrayList<Post> usersContributions;
    public ArrayList<Picture> likes;

    public User() {
        usersContributions = new ArrayList<>();
        likes = new ArrayList<>();
    }


}
