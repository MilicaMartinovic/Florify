package com.example.florify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.florify.R;
import com.example.florify.adapters.RecyclerViewAdapter;
import com.example.florify.db.services.FetchPostsService;
import com.example.florify.listeners.OnFetchPostsCompleted;
import com.example.florify.models.Post;

import java.util.ArrayList;

public class FeedFragment extends Fragment implements OnFetchPostsCompleted {

    private View view;

    private ArrayList<Post> posts;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;


    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        this.view = view;
        posts = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);

        this.popupdatePlantList();

        return view;
    }

    public void popupdatePlantList() {
        new FetchPostsService(this).execute();
    }

    @Override
    public void onFetchCompleted(ArrayList<Post> posts) {
        this.posts = posts;
        recyclerViewAdapter = new RecyclerViewAdapter(posts, this.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        gridLayoutManager = new GridLayoutManager(this.getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
