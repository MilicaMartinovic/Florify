package com.example.florify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.florify.adapters.RecyclerViewAdapterScoreboard;
import com.example.florify.db.services.FetchAllUsersService;
import com.example.florify.db.services.FetchMyPostsViewsAndLIkes;
import com.example.florify.helpers.ContributionScoreCalc;
import com.example.florify.listeners.OnFetchAllUsersCompleted;
import com.example.florify.listeners.OnFetchMyPostsViewsAndLikesComplted;
import com.example.florify.models.ScoreboardUsersVM;
import com.example.florify.models.User;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreboradActivity extends AppCompatActivity implements OnFetchAllUsersCompleted, OnFetchMyPostsViewsAndLikesComplted {
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private RecyclerViewAdapterScoreboard recyclerViewAdapter;
    private Session session;
    private ArrayList<User> users;
    private ArrayList<ScoreboardUsersVM> usersVMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreborad);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        users = new ArrayList<>();
        usersVMS = new ArrayList<>();
        session = new Session(this);
        recyclerView = findViewById(R.id.recycler_view_scoreboard);

        populateScoreboard();
    }

    private void populateScoreboard() {
        new FetchAllUsersService(this).execute();
    }

    @Override
    public void OnFetchAllUsersCompleted(ArrayList<User> users) {

        if(users != null) {
            this.users = users;

            for(User user : users) {
                new FetchMyPostsViewsAndLIkes(this, user)
                        .execute(user.id);
            }
        }
    }

    @Override
    public void OnFetchMyPostsViewsAndLikesComplted(int views, int likes, com.example.florify.models.User user) {
        this.usersVMS.add(new ScoreboardUsersVM(
                user.id,
                user.imageUrl,
                user.username,
                ContributionScoreCalc.calculateScore(likes,views,user.posts.size()))
        );
        if(users.size() == usersVMS.size()) {
            sortAndPost(usersVMS);
        }
    }

    private void sortAndPost(ArrayList<ScoreboardUsersVM> usersVMS) {
        Collections.sort(usersVMS);

        recyclerViewAdapter = new RecyclerViewAdapterScoreboard(usersVMS,
                getApplicationContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerViewAdapter.notifyDataSetChanged();
    }

}
