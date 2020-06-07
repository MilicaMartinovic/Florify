package com.example.florify;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.florify.adapters.RecyclerViewAdapterPendingConnections;
import com.example.florify.db.services.FetchPendingConnectionsService;
import com.example.florify.listeners.OnFetchPendingConnectionsCompleted;
import com.example.florify.models.User;

import java.util.ArrayList;

public class PendingConnectionsActivity extends AppCompatActivity implements OnFetchPendingConnectionsCompleted {
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private RecyclerViewAdapterPendingConnections recyclerViewAdapter;
    private Session session;
    private ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_connections);
        recyclerView = findViewById(R.id.recycler_view_pending_connections);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        users = new ArrayList<>();
        session = new Session(this);

        populatePendingConnections();
    }

    private void populatePendingConnections() {
        new FetchPendingConnectionsService(this)
                .execute(session.getUserId());
    }

    @Override
    public void onFetchPendingConnectionsCompleted(ArrayList<User> pendingConnections) {
        recyclerViewAdapter = new RecyclerViewAdapterPendingConnections(users,
                getApplicationContext(), true);
        recyclerView.setAdapter(recyclerViewAdapter);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerViewAdapter.notifyDataSetChanged();
    }
}
