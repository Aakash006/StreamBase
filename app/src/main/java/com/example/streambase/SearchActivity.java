package com.example.streambase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.example.streambase.adapters.SearchMediaAdapter;
import com.example.streambase.model.TMDB;
import com.example.streambase.model.TMDBList;
import com.example.streambase.services.TMDBAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    private SearchView mSearchView;
    private ListView mListView;
    private TMDBList mMediaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBarToolbar);
        getSupportActionBar().setTitle("Search");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        mSearchView = findViewById(R.id.search);
        mListView = findViewById(R.id.listOfResults);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchMedia(mSearchView.getQuery().toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mListView.setVisibility(View.INVISIBLE);
        mListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(SearchActivity.this, MediaInfoActivity.class);
            String selectedItem = adapterView.getItemAtPosition(i).toString();
            for (TMDB media : mMediaList.getMedia()) {
                if ((media.getMovieName() != null && media.getMovieName().equals(selectedItem)) || (media.getTvShowName() != null && media.getTvShowName().equals(selectedItem))) {
                    intent.putExtra("data", media);
                    break;
                }
            }
            startActivity(intent);
        });
    }


    private void fetchMedia(String mediaName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.tmdb_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Map<String, String> queryParametersMap = new HashMap<>();
        queryParametersMap.put("api_key", getString(R.string.tmdb_key));
        queryParametersMap.put("query", mediaName);

        TMDBAPI api = retrofit.create(TMDBAPI.class);
        Call<TMDBList> call = api.getMediaList(queryParametersMap);

        call.enqueue(new Callback<TMDBList>() {
            @Override
            public void onResponse(Call<TMDBList> call, Response<TMDBList> response) {
                if (response.isSuccessful()) {
                    mMediaList = response.body();
                    List<String> moviesOrShows = new ArrayList<>();
                    for (TMDB media : mMediaList.getMedia())
                        moviesOrShows.add(media.getMediaType().equals("tv") ? media.getTvShowName() : media.getMovieName());

                    mListView.setVisibility(View.VISIBLE);
                    mListView.setAdapter(new SearchMediaAdapter(getApplicationContext(), 0, moviesOrShows));
                }
            }

            @Override
            public void onFailure(Call<TMDBList> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.getStackTrace();
            }
        });

    }

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
}