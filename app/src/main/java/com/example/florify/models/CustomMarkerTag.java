package com.example.florify.models;

;
public class CustomMarkerTag {

    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CustomMarkerType getType() {
        return type;
    }

    public void setType(CustomMarkerType type) {
        this.type = type;
    }

    public CustomMarkerType type;
}
