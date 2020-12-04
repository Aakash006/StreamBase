package com.example.streambase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streambase.adapters.MediaAdapter;
import com.example.streambase.model.TMDB;
import com.example.streambase.model.TMDBList;
import com.example.streambase.services.TMDBAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView mRecyclerView;
    private MediaAdapter mMediaAdapter;
    private ArrayList<TMDB> mMediaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMediaList = new ArrayList<>();

        // UI Components
        Toolbar ActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(ActionBarToolbar);
        getSupportActionBar().setTitle("Trending");

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setSelectedItemId(R.id.nav_home);
        navigationView.setOnNavigationItemSelectedListener(navListener);

        mMediaAdapter = new MediaAdapter(getApplicationContext(), mMediaList);

        mRecyclerView = findViewById(R.id.trending_media);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        mRecyclerView.setAdapter(mMediaAdapter);

        if (savedInstanceState == null) {
            getTrendingMedia();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mMediaList", mMediaList);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mMediaList = (ArrayList<TMDB>) savedInstanceState.getSerializable("mMediaList");
        mMediaAdapter.updateData(this.mMediaList);
    }

    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.nav_search:
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                overridePendingTransition(0, 0);
                return true;
            case R.id.nav_fav:
                startActivity(new Intent(getApplicationContext(), FavouriteActivity.class));
                overridePendingTransition(0, 0);
                return true;
        }
        return false;
    };

    public void getTrendingMedia() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.tmdb_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Map<String, String> queryParametersMap = new HashMap<>();
        queryParametersMap.put("api_key", getString(R.string.tmdb_key));

        TMDBAPI api = retrofit.create(TMDBAPI.class);
        Call<TMDBList> call = api.getTrendingList(queryParametersMap);

        call.enqueue(new Callback<TMDBList>() {
            @Override
            public void onResponse(Call<TMDBList> call, Response<TMDBList> response) {
                if (response.isSuccessful()) {
                    mMediaList = (ArrayList<TMDB>) response.body().getMedia();
                    mMediaAdapter.updateData(mMediaList);
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    mRecyclerView.setAdapter(mMediaAdapter);
                } else {
                    Log.d(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TMDBList> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.getStackTrace();
            }
        });
    }
}