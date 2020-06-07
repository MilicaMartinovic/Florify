package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnFetchPendingConnectionsCompleted;
import com.example.florify.listeners.OnFetchUsersCompleted;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class FetchPendingConnectionsService extends AsyncTask<String, Void, Void> implements OnFetchUsersCompleted {

    private OnFetchPendingConnectionsCompleted listener;
    private OnFetchUsersCompleted ctx;

    public FetchPendingConnectionsService(OnFetchPendingConnectionsCompleted onFetchPendingConnectionsCompleted){
        this.listener = onFetchPendingConnectionsCompleted;
        this.ctx = this;
    }
    @Override
    protected Void doInBackground(String... strings) {
        String userId = strings[0];

        DBInstance
                .getCollection("users")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);
                            ArrayList<String> pending = user.pendingConnectionRequests;
                            if(pending.size() > 0) {
                                new FetchUsersService(ctx).execute(pending);
                            }
                            else {
                                listener.onFetchPendingConnectionsCompleted(new ArrayList<User>());
                            }
                        }
                        else
                            listener.onFetchPendingConnectionsCompleted(null);
                    }
                });

        return null;
    }

    @Override
    public void onFetchUsersCompleted(ArrayList<User> users) {
        listener.onFetchPendingConnectionsCompleted(users);
    }
}
