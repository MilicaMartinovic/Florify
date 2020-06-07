package com.example.florify.db.services;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.florify.listeners.OnFetchMyPostsViewsAndLikesComplted;
import com.example.florify.listeners.OnFetchPostsByIdsCompleted;
import com.example.florify.listeners.OnFetchUserCompleted;
import com.example.florify.models.Post;
import com.example.florify.models.User;

import java.util.ArrayList;

public class FetchMyPostsViewsAndLIkes extends AsyncTask<String, Void, Void >
implements OnFetchPostsByIdsCompleted, OnFetchUserCompleted {

    Handler handler;
    int position;
    private User user;
    private OnFetchMyPostsViewsAndLikesComplted listener;

    public FetchMyPostsViewsAndLIkes(OnFetchMyPostsViewsAndLikesComplted onFetchMyPostsViewsAndLikesComplted,
                                     com.example.florify.models.User user) {
        this.user = user;
        listener = onFetchMyPostsViewsAndLikesComplted;
    }
    @Override
    protected Void doInBackground(String... strings) {
        String userId = strings[0];
        new FetchUserService(this).execute(userId);
        return null;
    }

    @Override
    public void onFetchPostsByIdsCompleted(ArrayList<Post> posts) {
        int views = 0;
        int likes = 0;
        for(Post post : posts) {
            views += post.getViewsNumber();
            likes += post.getLikesNumber();
        }
        listener.OnFetchMyPostsViewsAndLikesComplted(views, likes, user);
    }

    @Override
    public void onFetchCompleted(User user) {
        if(user != null && user.posts != null && user.posts.size() > 0) {
            new FetchPostsByIdsService(this).execute(user.posts);
        }
        if(user.posts.size() == 0) {
            listener.OnFetchMyPostsViewsAndLikesComplted(0, 0, user);
        }
    }

    private void sendMessageToUIThread(int views, int likes, int position) {
        Bundle bundle = new Bundle();
        Message message = new Message();
        bundle.putInt("views", views);
        bundle.putInt("likes", likes);
        bundle.putInt("position", position);
        message.setData(bundle);
        handler.sendMessage(message);
    }
}
