package com.example.florify.listeners;

import com.example.florify.models.User;

import java.util.ArrayList;

public interface OnFetchUsersCompleted {
    void onFetchUsersCompleted(ArrayList<User> users);
}
