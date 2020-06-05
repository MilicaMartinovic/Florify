package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnSetLikesPostCompleted;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.HashMap;

public class SetLikesPostService extends AsyncTask<String, Void, Void> {

    private OnSetLikesPostCompleted listener;

    public SetLikesPostService(OnSetLikesPostCompleted onSetLikesPostCompleted) {
        this.listener = onSetLikesPostCompleted;
    }
    @Override
    protected Void doInBackground(String... strings) { //postId, likesNumber
        String postId = strings[0];
        String likesNumberStr = strings[1];
        final int likesNumber = Integer.parseInt(likesNumberStr);

        DBInstance
                .getCollection("posts")
                .document(postId)
                .update(new HashMap<String, Object>(){
                    {
                        put("likesNumber", likesNumber);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            listener.onSetLikesPostCompleted(true);
                        }
                        else {
                            listener.onSetLikesPostCompleted(false);

                        }
                    }
                });
        return null;
    }
}
