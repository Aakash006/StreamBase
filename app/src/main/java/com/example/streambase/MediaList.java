package com.example.streambase;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MediaList<T> {
    @SerializedName("results")
    public List<T> media;

    public List<T> getMedia() {
        return media;
    }
}
