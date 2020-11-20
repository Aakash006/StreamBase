package com.example.streambase.model;

import com.google.gson.annotations.SerializedName;

public class TMDB {

    @SerializedName("id")
    private int id;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("original_name")
    private String tvShowName;

    @SerializedName("original_title")
    private String movieName;

    @SerializedName("poster_path")
    private String imageURL;

    public String getMovieName() {
        return movieName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getId() {
        return id;
    }

    public String getMediaType() {
        return mediaType;
    }

    public String getTvShowName() {
        return tvShowName;
    }
}
