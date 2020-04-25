package com.example.florify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

@IgnoreExtraProperties
public class User {
    public String username;
    public String password;
    public String id;


    public int views;
    public int likes;
    @Exclude
    public String key;
    public String email, motherland;
    public ArrayList<Post> posts;
    public ArrayList<User> connections;
    public GeoPoint location;

    public String badge;

    public User() {
        posts = new ArrayList<>();
        connections = new ArrayList<>();
        views = 0;
        likes = 0;
        badge = "rookie";
    }

    public User(String username, String password, String email, String location) {
        this();
        this.username = username;
        this.password = password;
        this.email = email;
        this.motherland = location;
    }


}
