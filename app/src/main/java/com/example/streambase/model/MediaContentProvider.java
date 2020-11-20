package com.example.streambase.model;

import com.google.gson.annotations.SerializedName;


public class MediaContentProvider {

    @SerializedName("icon")
    private String providerLogoURL;

    @SerializedName("display_name")
    private String mediaContentProviderName;

    @SerializedName("url")
    private String streamURL;

    public String getProviderLogoURL() {
        return providerLogoURL;
    }

    public String getMediaContentProviderName() {
        return mediaContentProviderName;
    }

    public String getStreamURL() {
        return streamURL;
    }
}
