package com.example.florify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.florify.db.subjects.UserSubject;
import com.example.florify.models.Observer;
import com.example.florify.models.Subject;
import com.example.florify.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements Observer {

    private DatabaseReference mRef;
    private HashMap<String, Integer> userKeyIndexMapping;
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private User user;
    private UserSubject userSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        userSubject = new UserSubject();
        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        mRef = FirebaseDatabase.getInstance().getReference("users");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User value = dataSnapshot.getValue(User.class);
                user = new User(value.username, value.password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etUsername.getText().toString();

                if (user.username.compareTo(username) == 0 && user.password.compareTo(password) == 0) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Wrong password or username. Try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    @Override
//    public void update(Observable observable, Object o) {
//
//    }

    @Override
    public void update(Subject observable, Object arg) {
        if(observable.equals(this.userSubject)) {
            this.user = (User)arg;
        }
    }
}
