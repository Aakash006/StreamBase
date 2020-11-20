package com.example.streambase.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMDBList {
    @SerializedName("results")
    private List<TMDB> media;

    public List<TMDB> getMedia() {
        return media;
    }
}
