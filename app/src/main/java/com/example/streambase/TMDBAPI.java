package com.example.streambase;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface TMDBAPI {

    @GET("trending/all/day")
    Call<MediaList> getTrendingList(
            @QueryMap Map<String, String> queries,
            @HeaderMap Map<String, String> headers
    );

    @GET
    @Streaming
    Call<ResponseBody> getImage(
            @Url String url
    );
}