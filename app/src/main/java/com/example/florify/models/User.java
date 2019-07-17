package com.example.florify.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String username;
    public String password;
    @Exclude
    public String key;

    public User() {}

    public User(String un, String pw) {
        this.username = un;
        this.password = pw;
    }
}
