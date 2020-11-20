package com.example.streambase;

import com.google.gson.annotations.SerializedName;

public class TMDBModel {
    @SerializedName("backdrop_path")
    private String imageURL;

    @SerializedName("original_title")
    private String name;

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }
}
