package com.example.florify.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;

public class Post {
    private int likesNumber;
    private int viewsNumber;
    private String addedBy;
    private GeoPoint location;
    private String description;
    private ArrayList<String> tags;
    private long date;
    private String plantName;

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    private PostType postType;

    private String imageUrl;

    public Post(int likesNumber,
                int viewsNumber,
                String addedBy,
                GeoPoint location,
                String description,
                ArrayList<String> tags,
                long date,
                String plantName,
                String imageUrl,
                PostType postType) {

        this.tags = new ArrayList<String>();

        this.likesNumber = likesNumber;
        this.viewsNumber = viewsNumber;
        this.addedBy = addedBy;
        this.location = location;
        this.description = description;
        this.tags = tags;
        this.date = date;
        this.plantName = plantName;
        this.imageUrl = imageUrl;
        this.postType = postType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Post() {
        tags = new ArrayList<String>();
    }


    public int getLikesNumber() {
        return likesNumber;
    }

    public void setLikesNumber(int likesNumber) {
        this.likesNumber = likesNumber;
    }

    public int getViewsNumber() {
        return viewsNumber;
    }

    public void setViewsNumber(int viewsNumber) {
        this.viewsNumber = viewsNumber;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descirption) {
        this.description = descirption;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

}
