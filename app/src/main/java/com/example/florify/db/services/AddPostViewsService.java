package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnAddPostViewsCompleted;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;

public class AddPostViewsService extends AsyncTask<String, Void, Void> {

    private OnAddPostViewsCompleted listener;

    public AddPostViewsService(OnAddPostViewsCompleted onAddPostViewsCompleted) {
        listener = onAddPostViewsCompleted;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String id = strings[0];
        DBInstance
                .getCollection("posts")
                .document(id)
                .update("viewsNumber", FieldValue.increment(1))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        listener.onAddPostViewsCompleted(task.isSuccessful());
                    }
                });
        return null;
    }
}
