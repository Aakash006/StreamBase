package com.example.streambase;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Media {
    private String name;

    @SerializedName("picture")
    private String imageURL;

    @SerializedName("locations")
    private ArrayList<MediaContentProvider> mediaContentProviderList;

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public ArrayList<MediaContentProvider> getMediaContentProviderList() {
        return mediaContentProviderList;
    }
}
