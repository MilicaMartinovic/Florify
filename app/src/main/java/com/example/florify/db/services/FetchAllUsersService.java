package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnFetchAllUsersCompleted;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.example.florify.models.User;

import java.util.ArrayList;

public class FetchAllUsersService extends AsyncTask<String, Void, Void> {

    private OnFetchAllUsersCompleted listener;
    public FetchAllUsersService(OnFetchAllUsersCompleted onFetchAllUsersCompleted) {
        this.listener = onFetchAllUsersCompleted;
    }

    @Override
    protected Void doInBackground(String... strings) {
        final ArrayList<User> users = new ArrayList<>();
        DBInstance.getCollection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                User user = doc.toObject(User.class);
                                users.add(user);
                            }
                            if(users.size() > 0)
                                listener.OnFetchAllUsersCompleted(users);
                        }
                        listener.OnFetchAllUsersCompleted(null);
                    }
                });
        return null;
    }
}
