package com.example.florify.listeners;

import com.example.florify.models.User;

import java.util.ArrayList;

public interface OnFetchPendingConnectionsCompleted {
    void onFetchPendingConnectionsCompleted(ArrayList<User> pendingConnections);
}
