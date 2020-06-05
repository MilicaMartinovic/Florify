package com.example.florify.listeners;

import android.graphics.Bitmap;

import com.example.florify.models.User;

public interface OnLoadedFromWeb {
    void onLoadedFromWeb(Bitmap bitmap, User user);
}
