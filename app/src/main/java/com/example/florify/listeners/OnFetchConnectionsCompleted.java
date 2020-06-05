package com.example.florify.listeners;

import com.example.florify.models.User;

import java.util.ArrayList;

public interface OnFetchConnectionsCompleted {
    void onFetchConnectionsCompleted(ArrayList<User> users);
}
