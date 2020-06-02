package com.example.florify.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.florify.R;
import com.example.florify.adapters.RecyclerViewAdapterContributions;
import com.example.florify.models.Post;

import java.util.ArrayList;

public class ContributionsPopup  extends Dialog implements android.view.View.OnClickListener {

    private ArrayList<Post> posts;
    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private RecyclerViewAdapterContributions recyclerViewAdapter;
    private OnContributionsClose onContributionsClose;

    public ContributionsPopup(@NonNull Context context, ArrayList<Post> posts) {
        super(context);
        this.posts = posts;
    }

    public ContributionsPopup(@NonNull Context context,
                              int themeResId,
                              ArrayList<Post> posts,
                              OnContributionsClose onContributionsClose) {
        super(context, themeResId);
        this.onContributionsClose = onContributionsClose;
        this.posts = posts;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contributions_popup_wrapper);

        recyclerView = findViewById(R.id.recycler_view_contributions);
        recyclerViewAdapter = new RecyclerViewAdapterContributions(posts, this.getContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        gridLayoutManager = new GridLayoutManager(this.getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnContributionsClose)
            onContributionsClose.OnContributionsClose();
    }
}
