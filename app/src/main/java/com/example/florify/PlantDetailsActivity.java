package com.example.florify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.florify.models.Post;
import com.google.firebase.firestore.GeoPoint;

import co.lujun.androidtagview.TagContainerLayout;

public class PlantDetailsActivity extends AppCompatActivity {

    private Post post;
    private TextView txtPlantName, txtAddedBy, txtLikesNumber, txtViewsNumber, txtDescription;
    private ImageView imgPlant;
    private TagContainerLayout mTagGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_details);
        post = new Post();

        txtPlantName = findViewById(R.id.txtPlantDetailPlantName);
        txtAddedBy = findViewById(R.id.txtPlantDetailAddedBy);
        txtLikesNumber = findViewById(R.id.txtPlantDetailLikesNumber);
        txtViewsNumber = findViewById(R.id.txtPlantDetailViewsNumber);
        txtDescription = findViewById(R.id.txtPlantDetailDescription);
        imgPlant = findViewById(R.id.imgPlantDetailsImage);

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
        post.setLocation(new GeoPoint(lat, lon));
        post.setDate(intent.getLongExtra("date", 0));
        post.setTags(intent.getStringArrayListExtra("tags"));
    }
}
