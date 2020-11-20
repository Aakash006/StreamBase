package com.example.streambase;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMDBList {
    @SerializedName("results")
    private List<TMDBModel> media;

    public List<TMDBModel> getMedia() { return media; }
}
