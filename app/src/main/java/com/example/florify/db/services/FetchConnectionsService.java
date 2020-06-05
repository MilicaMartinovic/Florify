package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnFetchConnectionsCompleted;
import com.example.florify.listeners.OnFetchUsersCompleted;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class FetchConnectionsService extends AsyncTask<String, Void, ArrayList<User>> implements OnFetchUsersCompleted {

    private OnFetchConnectionsCompleted listener;
    public FetchConnectionsService(OnFetchConnectionsCompleted onFetchConnectionsCompleted) {
        listener = onFetchConnectionsCompleted;
    }
    @Override
    protected ArrayList<User> doInBackground(String... strings) {
        final ArrayList<User> users = new ArrayList<>();
        final OnFetchUsersCompleted ctx = this;

        DBInstance
                .getCollection("users")
                .document(strings[0])
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            User user = task.getResult().toObject(User.class);
                            new FetchUsersService(ctx).execute(user.connections);
                        }
                        else {
                            listener.onFetchConnectionsCompleted(null);
                        }
                    }
                });
        return null;
    }

    @Override
    public void onFetchUsersCompleted(ArrayList<User> users) {
        if(users != null) {
            listener.onFetchConnectionsCompleted(users);
        }
    }
}
