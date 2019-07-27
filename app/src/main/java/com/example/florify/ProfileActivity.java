package com.example.florify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView txtProfileName;
    private TextView txtProfileLocation;
    private TextView txtProfileLevel;
    private TextView txtProfileContributionScore;
    private TextView txtProfilePosts;
    private TextView txtProfileLikes;
    private TextView txtProfileViews;
    private TextView txtProfileConnections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getLayoutReferences();


    }

    private void getLayoutReferences() {
        txtProfileName = findViewById(R.id.txtProfileName);
        txtProfileLocation = findViewById(R.id.txtProfileLocation);
        txtProfileLevel = findViewById(R.id.txtProfileLevel);
        txtProfileContributionScore = findViewById(R.id.txtProfileContributionScore);
        txtProfilePosts = findViewById(R.id.txtProfilePosts);
        txtProfileLikes = findViewById(R.id.txtProfileLikes);
        txtProfileViews = findViewById(R.id.txtProfileViews);
        txtProfileConnections = findViewById(R.id.txtProfileConnections);
    }
}
