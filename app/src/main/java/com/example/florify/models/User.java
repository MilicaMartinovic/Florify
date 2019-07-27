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
    public ArrayList<Post> posts;
    public ArrayList<Picture> likes;

    public User() {
        posts = new ArrayList<>();
        likes = new ArrayList<>();
    }

    public User(String un, String pw) {
        this.username = un;
        this.password = pw;
    }
    public User(String un, String pw, String email, String location, Post[] posts, Picture[] likes) {
        this.username = un;
        this.password = pw;
        this.email = email;
        this.location = location;
        for (Post post : posts) {
            this.posts.add(post);
        }
        for(Picture pic : likes) {
            this.likes.add(pic);
        }
    }
    public User(String id, String un, String pw, String email) {
        this.id = id;
        this.username = un;
        this.password = pw;
        this.email = email;
    }
}
