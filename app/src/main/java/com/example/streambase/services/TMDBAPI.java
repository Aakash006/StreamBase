package com.example.streambase.services;

import com.example.streambase.model.TMDBList;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface TMDBAPI {

    @GET("trending/all/day")
    Call<TMDBList> getTrendingList(
            @QueryMap Map<String, String> queries
    );

    @GET("search/multi")
    Call<TMDBList> getMediaList(
            @QueryMap Map<String, String> queries
    );

    @GET
    @Streaming
    Call<ResponseBody> getImage(
            @Url String url
    );
}