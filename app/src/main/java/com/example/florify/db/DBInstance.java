package com.example.florify.db;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DBInstance {
    private FirebaseFirestore database;
    private static volatile DBInstance dbInstance;

    private DBInstance() {
        database = FirebaseFirestore.getInstance();
    }

    private FirebaseFirestore getFirebaseInstance() {
        return database;
    }

    private static DBInstance getInstance() {
        if(dbInstance == null) {
            dbInstance = new DBInstance();
        }
        return dbInstance;
    }

    public static CollectionReference getCollection(String collection) {
        return getInstance().getFirebaseInstance().collection(collection);
    }
}
