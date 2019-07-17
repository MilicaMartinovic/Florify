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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Observable;

public class UserSubject implements Subject {
    private ArrayList<Observer> observers;
    private DatabaseReference databaseReference;
    private ArrayList<User> users;

    public UserSubject() {
        users = new ArrayList<>();
        observers = new ArrayList<>();
        this.databaseReference = DBInstance.getReference("users");
        this.databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

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
    public void notifyObservers() {
        for (final Observer observer : observers) {
            observer.update(this, users);
        }
    }
}
