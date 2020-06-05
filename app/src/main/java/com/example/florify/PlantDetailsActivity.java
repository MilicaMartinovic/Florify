package com.example.florify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.florify.db.DBInstance;
import com.example.florify.db.services.AddPostViewsService;
import com.example.florify.db.services.FetchPostService;
import com.example.florify.db.services.FetchUserService;
import com.example.florify.db.services.SetLikesPostService;
import com.example.florify.listeners.OnAddPostViewsCompleted;
import com.example.florify.listeners.OnFetchPostCompleted;
import com.example.florify.listeners.OnFetchUserCompleted;
import com.example.florify.listeners.OnSetLikesPostCompleted;
import com.example.florify.models.Post;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

import co.lujun.androidtagview.TagContainerLayout;

public class PlantDetailsActivity extends AppCompatActivity
        implements OnFetchUserCompleted, OnFetchPostCompleted, OnAddPostViewsCompleted, OnSetLikesPostCompleted {

    private Post post;
    private TextView txtPlantName, txtAddedBy, txtLikesNumber, txtViewsNumber, txtDescription;
    private ImageView imgPlant;
    private ImageButton imgLike;
    private TagContainerLayout mTagGroup;
    private Session session;
    private boolean isAlreadyLiked = false;
    private User myself;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);
        post = new Post();
        session = new Session(this);

        txtPlantName = findViewById(R.id.txtPlantDetailPlantName);
        txtAddedBy = findViewById(R.id.txtPlantDetailAddedBy);
        txtLikesNumber = findViewById(R.id.txtPlantDetailLikesNumber);
        txtViewsNumber = findViewById(R.id.txtPlantDetailViewsNumber);
        txtDescription = findViewById(R.id.txtPlantDetailDescription);
        imgPlant = findViewById(R.id.imgPlantDetailsImage);
        imgLike = findViewById(R.id.imgLike);

        getExtras(getIntent());

        fetchPost(post.getId());
        addView(post.getId());

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeOrUnlikePost(post.getId(), !isAlreadyLiked);
            }
        });
    }

    private void fetchPost(String id) {
        new FetchPostService(this).execute(id);
    }

    private void addView(String id) {
        new AddPostViewsService(this).execute(id);
    }

    private void likeOrUnlikePost(String id, final boolean like) {

        this.imgLike.setClickable(false);
        final ArrayList<String> likedPosts = myself.likedPosts;

        if(like) {
            if(!likedPosts.contains(id))
                likedPosts.add(id);
            else
                Toast.makeText(getApplicationContext(),
                        "Already liked",
                        Toast.LENGTH_SHORT).show();
        }
        else {
            if(likedPosts.contains(id))
                likedPosts.remove(id);
        }

        DBInstance
                .getCollection("users")
                .document(session.getUserId())
                .update(new HashMap<String, Object>(){
                    {
                        put("likedPosts", likedPosts);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            final int liked = Integer.parseInt(txtLikesNumber.getText().toString());

                            new SetLikesPostService(PlantDetailsActivity.this)
                                    .execute(
                                            post.getId(),
                                            Integer.toString(liked + (like ? 1 : -1))
                                    );
                        }
                        else {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't contact server. Try Again",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                        imgLike.setClickable(true);
                    }
                });

    }

    private void setAlreadyLiked(String userId) {
        new FetchUserService(this).execute(userId);
    }

    private void getExtras(Intent intent) {
        post.setId(intent.getStringExtra("id"));
    }

    public void onClickAddedBy(View v) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        intent.putExtra("userId", post.getAddedById());
        startActivity(intent);
    }

    @Override
    public void onFetchCompleted(User user) {
        myself = user;
       if(user.likedPosts.contains(post.getId())) {
           isAlreadyLiked = true;
           imgLike.setImageResource(R.drawable.ic_liked);
       } else {
           imgLike.setImageResource(R.drawable.ic_like);
       }
    }

    @Override
    public void OnFetchPostCompleted(Post post) {
        if(post!= null) {
            this.post = post;
            fillPlantDetails(post);
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    "Couldn't fetch plant details. Reload",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void fillPlantDetails(Post post) {
        mTagGroup = findViewById(R.id.tabGroupPlantDetailsTags);
        mTagGroup.setTags(post.getTags());

        View current = getCurrentFocus();
        if (current != null) current.clearFocus();

        txtPlantName.setText(post.getPlantName());
        txtAddedBy.setText(post.getAddedBy());
        txtLikesNumber.setText(Integer.toString(post.getLikesNumber()));
        txtViewsNumber.setText(Integer.toString(post.getViewsNumber()));
        txtDescription.setText(post.getDescription());
        Glide.with(getApplicationContext())
                .asBitmap()
                .load(post.getImageUrl())
                .into(imgPlant);

        setAlreadyLiked(session.getUserId());
    }

    @Override
    public void onAddPostViewsCompleted(boolean success) {
        if(!success)
            Toast.makeText(
                getApplicationContext(),
                "Server issue, try again",
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onSetLikesPostCompleted(boolean success) {
        if(success) {
            imgLike.setImageResource(!isAlreadyLiked ?
                    R.drawable.ic_like :
                    R.drawable.ic_liked);
            int likes = Integer.parseInt(txtLikesNumber.getText().toString());
            txtLikesNumber.setText(Integer.toString(likes + (!isAlreadyLiked ? 1 : -1)));
            isAlreadyLiked = !isAlreadyLiked;
        }
        else {
            Toast.makeText(
                    getApplicationContext(),
                    "Server issue, try again",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}
