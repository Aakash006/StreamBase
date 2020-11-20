package com.example.streambase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private BottomNavigationView nav;
    private MediaList<TMDBModel> mediaList;
    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nav = findViewById(R.id.bottom_nav);
        nav.setSelectedItemId(R.id.nav_home);
        nav.setOnNavigationItemSelectedListener(navLlistener);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.tmdb_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        getTrendingMedia();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navLlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;

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
        }
    };

    public void getTrendingMedia() {
        Map<String, String> queryParametersMap = new HashMap<>();
        queryParametersMap.put("api_key", getString(R.string.tmdb_key));


        TMDBAPI api = retrofit.create(TMDBAPI.class);
        Call<MediaList> call = api.getTrendingList(queryParametersMap);

        call.enqueue(new Callback<MediaList>() {
            @Override
            public void onResponse(Call<MediaList> call, retrofit2.Response<MediaList> response) {
                if(response.isSuccessful()) {
                    System.out.println("***** ");
                    mediaList = response.body();
                    List<String> moviesOrShows = new ArrayList<>();
                    for (TMDBModel m : mediaList.getMedia()) {
                        moviesOrShows.add(m.getName());
                        System.out.println("name: " + m.getName());
                    }
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MediaList> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.getStackTrace();
            }
        });
    }
}