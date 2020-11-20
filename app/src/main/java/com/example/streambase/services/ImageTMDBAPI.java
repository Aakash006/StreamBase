package com.example.streambase.services;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ImageTMDBAPI {
    @GET
    @Streaming
    Call<ResponseBody> getImage(
            @Url String url
    );
}
