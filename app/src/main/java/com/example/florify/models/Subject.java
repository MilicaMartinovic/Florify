package com.example.florify.models;

public interface Subject {
    void register(Observer observer);
    void unregister(Observer observer);
    void notifyObservers(Object arg);
}
