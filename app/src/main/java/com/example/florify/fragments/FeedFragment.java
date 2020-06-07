package com.example.florify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.florify.R;
import com.example.florify.adapters.RecyclerViewAdapter;
import com.example.florify.db.services.FetchPostsService;
import com.example.florify.db.services.SearchPostsByTextService;
import com.example.florify.listeners.OnFeedFragmentReady;
import com.example.florify.listeners.OnFetchPostsCompleted;
import com.example.florify.listeners.OnSearchPostsByTextCompleted;
import com.example.florify.models.Post;

import java.util.ArrayList;

public class FeedFragment extends Fragment implements OnFetchPostsCompleted, OnSearchPostsByTextCompleted {

    private View view;

    private ArrayList<Post> posts;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private RecyclerViewAdapter recyclerViewAdapter;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayList<String> autoompleteList = new ArrayList<>();
    private OnFeedFragmentReady onFeedFragmentReady;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        View appBar = inflater.inflate(R.layout.custom_app_bar, container, false);

        this.view = view;
        posts = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recycler_view);

        autoCompleteTextView = appBar.findViewById(R.id.search_auto_complete);
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
        recyclerViewAdapter.notifyDataSetChanged();

        populateAutocomplete();
    }

    private void populateAutocomplete() {

        for(Post post: posts) {
            autoompleteList.add(post.getPlantName());
            autoompleteList.add(post.getPostType().toString());
            for(String tag: post.getTags()) {
                autoompleteList.add(tag);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                autoompleteList.toArray(new String[autoompleteList.size()])
        );
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),
                "Position: " + position + " item: " + autoompleteList.get(position),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void searchByTextString(String search) {
        new SearchPostsByTextService(FeedFragment.this).execute(search);
    }

    @Override
    public void onSearchPostsByTextCompleted(ArrayList<Post> posts) {
        recyclerViewAdapter = new RecyclerViewAdapter(posts, this.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerViewAdapter.notifyDataSetChanged();
    }
}
