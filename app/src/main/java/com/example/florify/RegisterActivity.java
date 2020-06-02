package com.example.florify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.florify.db.DBInstance;
import com.example.florify.models.Post;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etEmail, etConfirmPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        session = new Session(this);

        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etEmail = findViewById(R.id.etRegisterEmail);
        etConfirmPassword = findViewById(R.id.etRegisterConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    final String username = etUsername.getText().toString();
                    final String email = etEmail.getText().toString();
                    final String password = etPassword.getText().toString();
                    String confirmPassword = etConfirmPassword.getText().toString();


                    if(password.compareTo(confirmPassword) == 0) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            final FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                            final String id = firebaseUser.getUid();
                                            final Map<String, Object> user = new HashMap<>();
                                            user.put("username", username);
                                            user.put("email", firebaseUser.getEmail());
                                            user.put("password", password);
                                            user.put("id", id);
                                            user.put("likes", 0);
                                            user.put("views", 0);
                                            user.put("badge", "rookie");
                                            user.put("posts", new ArrayList<Post>());
                                            user.put("connections", new ArrayList<User>());
                                            user.put("motherland", "Serbia, Nis");
                                            user.put("location", null);
                                            user.put("newConnectionRequests", new ArrayList<String>());
                                            user.put("oldConnectionRequests", new ArrayList<String>());
                                            user.put("pendingConnectionRequests", new ArrayList<String>());
                                            user.put("imageUrl", "https://www.kindpng.com/picc/m/78-785827_user-profile-avatar-login-account-male-user-icon.png");
                                            user.put("likedPosts", new ArrayList<>());

                                            DBInstance.getCollection("users").document(id)
                                                    .set(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            updateUI(firebaseUser);
                                                            session.addUserEmail(email);
                                                            session.addUserId(id);
                                                            session.addUsername(username);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Register failed. Try again",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(RegisterActivity.this, "Autentication failed",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                }
                catch(Exception e) {

                }

            }
        });
    }

    private void updateUI(FirebaseUser user) {
        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(i);
    }
}
