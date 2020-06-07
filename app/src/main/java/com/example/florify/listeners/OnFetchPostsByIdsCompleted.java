package com.example.florify.listeners;

import com.example.florify.models.Post;

import java.util.ArrayList;

public interface OnFetchPostsByIdsCompleted {
    void onFetchPostsByIdsCompleted(ArrayList<Post> posts);
}
