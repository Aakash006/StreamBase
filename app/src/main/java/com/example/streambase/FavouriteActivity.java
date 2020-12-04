package com.example.streambase;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.streambase.adapters.MediaAdapter;
import com.example.streambase.model.TMDB;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;

public class FavouriteActivity extends AppCompatActivity {
    private static final String TAG = "FavouriteActivity";

    private RecyclerView mRecyclerView;

    private ArrayList<TMDB> mMediaList;

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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("data", this.mMediaList);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mMediaList = savedInstanceState.getParcelableArrayList("data");
    }

    public void getFavourites() {
        StreamBaseDB streamBaseDB = new StreamBaseDB(this, null, null, 1);
        HashMap<TMDB, String> favorites = streamBaseDB.getAllFavoriteMedia();
        mMediaList = new ArrayList<>(favorites.keySet());

        MediaAdapter mediaAdapter = new MediaAdapter(getApplicationContext(), mMediaList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
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
