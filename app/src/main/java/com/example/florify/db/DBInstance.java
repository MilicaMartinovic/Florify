package com.example.florify.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DBInstance {
    private FirebaseDatabase database;
    private static volatile DBInstance dbInstance;

    private DBInstance() {
        database = FirebaseDatabase.getInstance();
    }

    private FirebaseDatabase getFirebaseInstance() {
        return database;
    }

    private static DBInstance getInstance() {
        if(dbInstance == null) {
            dbInstance = new DBInstance();
        }
        return dbInstance;
    }

    public static DatabaseReference getReference(String reference) {
        return getInstance().getFirebaseInstance().getReference(reference);
    }
}
