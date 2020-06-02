package com.example.florify.models;

import java.util.ArrayList;

public class PostFilters {
    private Boolean searchPlants;

    public PostFilters() {
        searchPlants = true;
        plantName = null;
        postTypes = new ArrayList<>();
        radius = 0;
        dateRange = DateRangeItems.ANYTIME;
    }
    public PostFilters(Boolean searchPlants, String plantName, ArrayList<PostType> postTypes, int radius, DateRangeItems dateRange) {
        this.searchPlants = searchPlants;
        this.plantName = plantName;
        this.postTypes = postTypes;
        this.radius = radius;
        this.dateRange = dateRange;
    }

    public Boolean getSearchPlants() {
        return searchPlants;
    }

    public void setSearchPlants(Boolean searchPlants) {
        this.searchPlants = searchPlants;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public ArrayList<PostType> getPostTypes() {
        return postTypes;
    }

    public void setPostTypes(ArrayList<PostType> postTypes) {
        this.postTypes = postTypes;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public DateRangeItems getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRangeItems dateRange) {
        this.dateRange = dateRange;
    }

    private String plantName;
    private ArrayList<PostType> postTypes;
    private int radius;
    private DateRangeItems dateRange;
}
