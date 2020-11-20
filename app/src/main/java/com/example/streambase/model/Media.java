package com.example.streambase.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Media {

    @SerializedName("name")
    private String name;

    @SerializedName("locations")
    private ArrayList<MediaContentProvider> mediaContentProviderList;

    public ArrayList<MediaContentProvider> getMediaContentProviderList() {
        return mediaContentProviderList;
    }

    public String getName() {
        return name;
    }
}
