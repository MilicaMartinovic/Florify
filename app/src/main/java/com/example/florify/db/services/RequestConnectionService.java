package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnRequestedConnectionService;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestConnectionService extends AsyncTask<String, Void, Void> {
    private OnRequestedConnectionService listener;

    public RequestConnectionService(OnRequestedConnectionService onRequestedConnectionService) {
        this.listener = onRequestedConnectionService;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String myId = strings[0];
        String toAddId = strings[1];
        AddToPendingAndNewConnections(myId, toAddId);
        return null;
    }

    private void AddToPendingAndNewConnections(final String myId, final String toAddId) {
        DBInstance
                .getCollection("users")
                .document(myId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            User user = snapshot.toObject(User.class);
                            ArrayList<String> pending = user.pendingConnectionRequests;
                            pending.add(toAddId);
                            Map<String, Object> map = new HashMap<>();
                            map.put("pendingConnectionRequests", pending);

                            DBInstance
                                    .getCollection("users")
                                    .document(myId)
                                    .update(map)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                AddToNewConnections(myId, toAddId);
                                            }
                                            else {
                                                listener.OnRequestedConnectionService(false);
                                            }
                                        }
                                    });
                        }
                        else {
                            listener.OnRequestedConnectionService(false);
                        }
                    }
                });
    }

    private void AddToNewConnections(final String myId, String toAddId) {

        DBInstance
                .getCollection("users")
                .document(toAddId)
                .update(new HashMap<String, Object>() {
                    {
                        put("newConnectionRequests", new ArrayList<>(Arrays.asList(myId)));
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            listener.OnRequestedConnectionService(true);
                        }
                        else {
                            listener.OnRequestedConnectionService(false);
                        }
                    }
                });
    }
}
