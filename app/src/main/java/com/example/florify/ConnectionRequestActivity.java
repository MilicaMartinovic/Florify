package com.example.florify;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.florify.adapters.ReyclerViewConnectionRequestsAdapter;
import com.example.florify.db.services.FetchUserService;
import com.example.florify.db.services.FetchUsersService;
import com.example.florify.listeners.OnFetchUserCompleted;
import com.example.florify.listeners.OnFetchUsersCompleted;
import com.example.florify.models.User;

import java.util.ArrayList;

public class ConnectionRequestActivity extends AppCompatActivity implements OnFetchUserCompleted,  OnFetchUsersCompleted {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private ReyclerViewConnectionRequestsAdapter recyclerViewAdapter;
    private Session session;
    private ArrayList<User> users;
    private TextView textViewNoNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_request);


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        users = new ArrayList<>();
        session = new Session(this);
        recyclerView = findViewById(R.id.recycler_view_connection_requests);
        textViewNoNew = findViewById(R.id.txtNoNewConnectionRequests);

        populateConnectionRequests();

        recyclerViewAdapter = new ReyclerViewConnectionRequestsAdapter(users, getApplicationContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerViewAdapter.notifyDataSetChanged();
    }

    private void populateConnectionRequests() {
        new FetchUserService(this).execute(session.getUserId());
    }

    @Override
    public void onFetchCompleted(User user) {
        ArrayList<String> ids = user.newConnectionRequests;
        for(String id : user.oldConnectionRequests)
            ids.add(id);
        if(ids.size() > 0)
            new FetchUsersService(this).execute(ids);
        else
            textViewNoNew.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFetchUsersCompleted(ArrayList<User> users) {
        recyclerViewAdapter = new ReyclerViewConnectionRequestsAdapter(users, getApplicationContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
