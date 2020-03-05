package com.example.florify;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.florify.db.services.FetchUserService;
import com.example.florify.listeners.OnFetchUserCompleted;
import com.example.florify.models.User;

public class ProfileActivity extends AppCompatActivity implements OnFetchUserCompleted {

    private TextView txtProfileName;
    private TextView txtProfileLocation;
    private TextView txtProfileLevel;
    private TextView txtProfileContributionScore;
    private TextView txtProfilePosts;
    private TextView txtProfileLikes;
    private TextView txtProfileViews;
    private TextView txtProfileConnections;

    private Session session;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getLayoutReferences();

        session = new Session(getApplicationContext());
        PopulateUsersData();
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

    private void PopulateUsersData() {
        new FetchUserService(this).execute(session.getUserId());
    }
    @Override
    public void onFetchCompleted(User user) {
        this.user = user;
    }
}
