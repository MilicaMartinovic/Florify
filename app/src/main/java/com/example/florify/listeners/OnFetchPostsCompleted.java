package com.example.florify.listeners;

import com.example.florify.models.Post;

import java.util.ArrayList;

public interface OnFetchPostsCompleted {
    void onFetchCompleted(ArrayList<Post> posts);
}
