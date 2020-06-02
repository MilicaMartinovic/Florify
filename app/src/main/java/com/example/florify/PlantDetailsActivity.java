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
import com.example.florify.db.services.FetchUserService;
import com.example.florify.listeners.OnFetchUserCompleted;
import com.example.florify.models.Post;
import com.example.florify.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.lujun.androidtagview.TagContainerLayout;

public class PlantDetailsActivity extends AppCompatActivity implements OnFetchUserCompleted {

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
        addView(post.getId());

        imgLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeOrUnlikePost(post.getId(), !isAlreadyLiked);
            }
        });
    }

    private void addView(String id) {
        DBInstance
                .getCollection("posts")
                .document(id)
                .update("viewsNumber", FieldValue.increment(1));
    }

    private void likeOrUnlikePost(String id, final boolean like) {

        this.imgLike.setClickable(false);

        final ArrayList<String> likedPosts = myself.likedPosts;

        if(like) {
            if(likedPosts.size() == 0 || !likedPosts.contains(id))
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
        Map<String, Object> map = new HashMap<String, Object>(){
            {
                put("likedPosts", likedPosts);
            }
        };
        DBInstance
                .getCollection("users")
                .document(session.getUserId())
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()) {
                            int liked = Integer.parseInt(txtLikesNumber.getText().toString());
                            if(like) {
                                imgLike.setImageResource(R.drawable.ic_liked);
                                txtLikesNumber.setText(Integer.toString(++liked));
                            }

                            else {
                                imgLike.setImageResource(R.drawable.ic_like);
                                txtLikesNumber.setText(Integer.toString(--liked));
                            }

                            isAlreadyLiked = !isAlreadyLiked;
                            imgLike.setClickable(true);
                        }
                        else {
                            imgLike.setClickable(true);
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't contact server. Try Again",
                                    Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

    private void setAlreadyLiked(String userId) {
        new FetchUserService(this).execute(userId);
    }

    private void getExtras(Intent intent) {
        post.setPlantName(intent.getStringExtra("plantName"));
        post.setImageUrl(intent.getStringExtra("pictureUrl"));
        post.setDescription(intent.getStringExtra("description"));
        post.setAddedBy(intent.getStringExtra("addedBy"));
        post.setLikesNumber(intent.getIntExtra("likesNumber", 0));
        post.setViewsNumber(intent.getIntExtra("viewsNumber",0));
        Double lat = intent.getDoubleExtra("latitude", 0);
        Double lon = intent.getDoubleExtra("longitude", 0);
        post.setL(new GeoPoint(lat, lon));
        post.setDate(intent.getLongExtra("date", 0));
        post.setTags(intent.getStringArrayListExtra("tags"));
        post.setId(intent.getStringExtra("id"));
        post.setAddedById(intent.getStringExtra("addedById"));
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
            this.imgLike.setImageResource(R.drawable.ic_liked);
       } else {
           this.imgLike.setImageResource(R.drawable.ic_like);
       }
    }
}
