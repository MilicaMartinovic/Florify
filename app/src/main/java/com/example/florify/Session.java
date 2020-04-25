package com.example.florify;

import android.content.Context;
import android.content.SharedPreferences;

public class Session {

    private SharedPreferences.Editor editor;
    public SharedPreferences preferences;
    public Session(Context context) {

        preferences = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = preferences.edit();
    }

    public void addUserEmail(String email) {
        editor.putString("email", email);
        editor.commit();
    }

    public void addUsername(String username) {
        editor.putString("username", username);
        editor.commit();
    }


    public void addUserId(String id) {
        editor.putString("id", id);
    }

    public String getUserEmail() {
        return this.preferences.getString("email", "");
    }

    public String getUserId() {
        return this.preferences.getString("id", "");
    }

    public String getUsername() {
        return this.preferences.getString("username", "");
    }
}
