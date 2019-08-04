package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnFetchPostsCompleted;
import com.example.florify.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FetchPostsService extends AsyncTask<Void, Void, Void> {

    private OnFetchPostsCompleted listener;

    public FetchPostsService(OnFetchPostsCompleted onFetchPostsCompleted) {
        this.listener = onFetchPostsCompleted;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        final ArrayList<Post> posts = new ArrayList<>();

        CollectionReference postsReference = DBInstance.getCollection("posts");
        postsReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Post post = document.toObject(Post.class);
                                posts.add(post);
                            }
                            if(posts.size() > 0) {
                                listener.onFetchCompleted(posts);
                            }
                        }
                    }
                });
        return null;
    }
}
