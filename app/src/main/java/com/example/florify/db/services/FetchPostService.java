package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnFetchPostCompleted;
import com.example.florify.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class FetchPostService extends AsyncTask<String, Void, Void> {

    private OnFetchPostCompleted listener;

    public FetchPostService(OnFetchPostCompleted onFetchPostCompleted) {
        this.listener = onFetchPostCompleted;
    }
    @Override
    protected Void doInBackground(String... strings) {
        String id = strings[0];
        DBInstance
                .getCollection("posts")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            Post post = task.getResult().toObject(Post.class);
                            listener.OnFetchPostCompleted(post);
                        }
                        else {
                            listener.OnFetchPostCompleted(null);
                        }
                    }
                });
        return null;
    }
}
