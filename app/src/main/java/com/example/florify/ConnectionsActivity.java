package com.example.florify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.florify.adapters.RecyclerViewAdapterPendingConnections;
import com.example.florify.db.services.FetchConnectionsService;
import com.example.florify.listeners.OnFetchConnectionsCompleted;
import com.example.florify.models.User;

import java.util.ArrayList;

public class ConnectionsActivity extends AppCompatActivity implements OnFetchConnectionsCompleted {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private RecyclerViewAdapterPendingConnections recyclerViewAdapter;
    private Session session;
    private ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);
        recyclerView = findViewById(R.id.recycler_view_connections);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        users = new ArrayList<>();
        session = new Session(this);

        populateConnections();
    }

    private void populateConnections() {
        new FetchConnectionsService(this).execute(session.getUserId());
    }

    @Override
    public void onFetchConnectionsCompleted(ArrayList<User> users) {
        recyclerViewAdapter = new RecyclerViewAdapterPendingConnections(users,
                getApplicationContext(), false);
        recyclerView.setAdapter(recyclerViewAdapter);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerViewAdapter.notifyDataSetChanged();
    }
}
