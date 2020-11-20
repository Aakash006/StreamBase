package com.example.streambase.model;

import com.google.gson.annotations.SerializedName;

public class MediaCollection {

    @SerializedName("collection")
    private Media media;

    public Media getMedia() {
        return media;
    }
}
