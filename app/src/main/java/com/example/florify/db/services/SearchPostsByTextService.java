package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnSearchPostsByTextCompleted;
import com.example.florify.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchPostsByTextService extends AsyncTask<String, Void, Void> {

    private OnSearchPostsByTextCompleted listener;
    private ArrayList<Post> posts;

    public SearchPostsByTextService(OnSearchPostsByTextCompleted onSearchPostsByTextCompleted){
        this.listener = onSearchPostsByTextCompleted;
        posts = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(String... strings) {
        String searchText = strings[0];
        getByName(searchText);
        return null;
    }

    private void getByName(final String searchText) {
        DBInstance
                .getCollection("posts")
                .whereEqualTo("plantName", searchText)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<Post> postsName = task.getResult().toObjects(Post.class);
                            if(postsName.size() > 0)
                                posts.addAll(postsName);
                            getByTag(searchText);
                        }
                    }
                });
    }

    private void getByTag(final String searchText) {
        DBInstance
                .getCollection("posts")
                .whereArrayContains("tags", searchText)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<Post> postsName = task.getResult().toObjects(Post.class);
                            if(postsName.size() > 0)
                                posts.addAll(postsName);
                            getByPostType(searchText);
                        }
                    }
                });
    }

    private void getByPostType(final String searchText) {
        DBInstance
                .getCollection("posts")
                .whereEqualTo("postType", searchText)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<Post> postsName = task.getResult().toObjects(Post.class);
                            if(postsName.size() > 0)
                                posts.addAll(postsName);
                            listener.onSearchPostsByTextCompleted(posts);
                        }
                    }
                });
    }
}
