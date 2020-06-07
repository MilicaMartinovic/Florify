package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnFetchPostsByIdsCompleted;
import com.example.florify.models.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FetchPostsByIdsService extends AsyncTask<ArrayList<String>, Void, Void> {

    OnFetchPostsByIdsCompleted listener;

    public FetchPostsByIdsService(OnFetchPostsByIdsCompleted onFetchPostsByIdsCompleted) {
        listener = onFetchPostsByIdsCompleted;
    }

    @Override
    protected Void doInBackground(ArrayList<String>... arrayLists) {
        ArrayList<String> ids = arrayLists[0];

        DBInstance
                .getCollection("posts")
                .whereIn("id", ids)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<Post> posts = task.getResult().toObjects(Post.class);
                            listener.onFetchPostsByIdsCompleted(new ArrayList<Post>(posts));
                        }
                    }
                });
        return null;
    }
}
