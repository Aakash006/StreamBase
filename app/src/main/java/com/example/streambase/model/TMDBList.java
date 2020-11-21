package com.example.streambase.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TMDBList implements Parcelable {
    @SerializedName("results")
    private List<TMDB> media;

    protected TMDBList(Parcel in) {
        media = in.createTypedArrayList(TMDB.CREATOR);
    }

    public static final Creator<TMDBList> CREATOR = new Creator<TMDBList>() {
        @Override
        public TMDBList createFromParcel(Parcel in) {
            return new TMDBList(in);
        }

        @Override
        public TMDBList[] newArray(int size) {
            return new TMDBList[size];
        }
    };

    public List<TMDB> getMedia() {
        return media;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(media);
    }
}
