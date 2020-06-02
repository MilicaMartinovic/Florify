package com.example.florify.db.services;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.listeners.OnFetchUserCompleted;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class FetchUserService extends AsyncTask<String, Void, Void> {

    private OnFetchUserCompleted listener;

    public FetchUserService(OnFetchUserCompleted onFetchUserCompleted)
    {
        this.listener = onFetchUserCompleted;
    }

    @Override
    protected Void doInBackground(String... strings) {
        final User user = new User();

        String userUid = strings[0];
        DocumentReference usersReference = DBInstance.getCollection("users").document(userUid);
        usersReference.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            User user = documentSnapshot.toObject(User.class);
                            listener.onFetchCompleted(user);
                        }
                    }
                });
        return null;
    }
}
