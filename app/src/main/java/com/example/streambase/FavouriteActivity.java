package com.example.streambase;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streambase.adapters.MediaAdapter;
import com.example.streambase.model.TMDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    private static final String TAG = "FavouriteActivity";

    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_activity);

        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBarToolbar);
        getSupportActionBar().setTitle("Favorites");

        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setSelectedItemId(R.id.nav_fav);
        navigationView.setOnNavigationItemSelectedListener(navLlistener);

        mRecyclerView = findViewById(R.id.favorite_media);

        getFavourites();
    }

    public void getFavourites() {
        StreamBaseDB streamBaseDB = new StreamBaseDB(this, null, null, 1);
        HashMap<TMDB, String> favorites = streamBaseDB.getAllFavoriteMedia();
        List<TMDB> mediaList = new ArrayList<>();
        mediaList.addAll(favorites.keySet());

        MediaAdapter mediaAdapter = new MediaAdapter(getApplicationContext(), mediaList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        mRecyclerView.setAdapter(mediaAdapter);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navLlistener = item -> {
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
