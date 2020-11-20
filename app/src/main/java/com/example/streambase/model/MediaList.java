package com.example.streambase.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MediaList {

    @SerializedName("collection")
    private ArrayList<Media> mediaContentList;

    public ArrayList<Media> getMediaContentList() {
        return mediaContentList;
    }
}
