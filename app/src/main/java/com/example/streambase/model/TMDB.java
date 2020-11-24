package com.example.streambase.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TMDB implements Parcelable {

    @SerializedName("id")
    private int id;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("original_name")
    private String tvShowName;

    @SerializedName("original_title")
    private String movieName;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String imageURL;

    @SerializedName("backdrop_path")
    private String imageURLBackdrop;

    protected TMDB(Parcel in) {
        id = in.readInt();
        mediaType = in.readString();
        tvShowName = in.readString();
        movieName = in.readString();
        overview = in.readString();
        imageURL = in.readString();
        imageURLBackdrop = in.readString();
    }

    public TMDB(int id, String mediaType, String tvShowName, String movieName, String overview, String imageURL, String imageURLBackdrop) {
        this.id = id;
        this.mediaType = mediaType;
        this.tvShowName = tvShowName;
        this.movieName = movieName;
        this.overview = overview;
        this.imageURL = imageURL;
        this.imageURLBackdrop = imageURLBackdrop;
    }

    public static final Creator<TMDB> CREATOR = new Creator<TMDB>() {
        @Override
        public TMDB createFromParcel(Parcel in) {
            return new TMDB(in);
        }

        @Override
        public TMDB[] newArray(int size) {
            return new TMDB[size];
        }
    };

    public String getMovieName() {
        return movieName;
    }

    public String getOverview() {
        return overview;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getImageURLBackdrop() {
        return imageURLBackdrop;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(mediaType);
        parcel.writeString(tvShowName);
        parcel.writeString(movieName);
        parcel.writeString(overview);
        parcel.writeString(imageURL);
        parcel.writeString(imageURLBackdrop);
    }
}
