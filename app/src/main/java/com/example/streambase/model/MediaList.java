package com.example.streambase.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaList {
    @SerializedName("results")
    private List<Media> media;

    public List<Media> getMedia() { return media; }
}
