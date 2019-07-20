package com.example.florify.db.subjects;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;

import com.example.florify.db.DBInstance;
import com.example.florify.models.Observer;
import com.example.florify.models.Subject;
import com.example.florify.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserSubject implements Subject {
    private ArrayList<Observer> observers;
    private DatabaseReference databaseReference;
    private User user;

    public UserSubject() {
        user = new User();
        observers = new ArrayList<>();
        this.databaseReference = DBInstance.getReference();
        Query query = this.databaseReference.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                notifyObservers(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void register(Observer observer) {
        if(!this.observers.contains(observer))
            this.observers.add(observer);
    }

    @Override
    public void unregister(Observer observer) {

    }

    @Override
    public void notifyObservers(Object arg) {
        for (final Observer observer : observers) {
            observer.update(this, arg);
        }
    }
}
