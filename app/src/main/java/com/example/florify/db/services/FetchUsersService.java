package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnFetchUsersCompleted;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FetchUsersService extends AsyncTask<ArrayList<String>, Void, Void>  {

    private OnFetchUsersCompleted listener;

    public FetchUsersService(OnFetchUsersCompleted onFetchUsersCompleted) {
        this.listener = onFetchUsersCompleted;
    }


    @Override
    protected Void doInBackground(ArrayList<String>... arrayLists) {
        ArrayList<String> ids = arrayLists[0];

        if(ids != null) {
            final ArrayList<User> users = new ArrayList<>();
            DBInstance.getCollection("users")
                    .whereIn("id", ids)
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
                                    listener.onFetchUsersCompleted(users);
                            }
                        }
                    });
        }
        else {
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
                                    listener.onFetchUsersCompleted(users);
                            }
                        }
                    });
        }

        return null;
    }
}
