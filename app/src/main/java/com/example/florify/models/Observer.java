package com.example.florify.models;

public interface Observer {
    void update(Subject observable, Object arg);
}
