package com.example.streambase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
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

    private BottomNavigationView nav;
    private TMDBList mediaList;
    private Retrofit retrofit;
    private ArrayAdapter mAdapter;
    private ListView trends;
    private ArrayList<String> mediaNames;
    private ArrayList<String> mediaIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nav = findViewById(R.id.bottom_nav);
        nav.setSelectedItemId(R.id.nav_home);
        nav.setOnNavigationItemSelectedListener(navLlistener);

        trends = (ListView) findViewById(R.id.trending_media);

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.tmdb_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if(savedInstanceState == null) {
            getTrendingMedia();
        }

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navLlistener = item -> {
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
    };

    public void getTrendingMedia() {
        Map<String, String> queryParametersMap = new HashMap<>();
        queryParametersMap.put("api_key", getString(R.string.tmdb_key));

        TMDBAPI api = retrofit.create(TMDBAPI.class);
        Call<TMDBList> call = api.getTrendingList(queryParametersMap);

        call.enqueue(new Callback<TMDBList>() {
            @Override
            public void onResponse(Call<TMDBList> call, Response<TMDBList> response) {
                if(response.isSuccessful()) {
                    mediaList = response.body();
                    mediaNames = new ArrayList<>();
                    mediaIcons = new ArrayList<>();
                    for (TMDB m : mediaList.getMedia()) {
                        if (m.getMovieName() != null) {
                            mediaNames.add(m.getMovieName());
                            mediaIcons.add(m.getImageURL());
                        }
                    }
                    setCards(mediaNames, mediaIcons);
                } else {
                    System.out.println(response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<TMDBList> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.getStackTrace();
            }
        });
    }

    public void setCards(ArrayList<String> mediaNames, ArrayList<String> mediaIcons) {
        Activity mActivity = this;
        mAdapter = new ArrayAdapter<String>(this, R.layout.trending_cards, R.id.trending, mediaNames) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the view
                LayoutInflater inflater = mActivity.getLayoutInflater();
                @SuppressLint({"ViewHolder", "InflateParams"}) View itemView = inflater.inflate(R.layout.trending_cards, null, true);

                // Get media name
                String mediaName = mediaNames.get(position);
                String mediaIcon = mediaIcons.get(position);


                // Get the relative layout
                //LinearLayout relativeLayout = (LinearLayout) itemView.findViewById(R.id.rl);

                // Display the media name
                TextView mediaTxt = (TextView) itemView.findViewById(R.id.media_name);
                mediaTxt.setText(mediaName);

                // Get the card view
                //CardView cardView = (CardView) itemView.findViewById(R.id.card_view);

                ImageView serviceIcon = (ImageView) itemView.findViewById(R.id.media_icon);

                Glide.with(getApplicationContext()).load(getString(R.string.image_tmdb_url) + mediaIcon).into(serviceIcon);

                return itemView;
            }
        };

        trends.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("mediaNames", mediaNames);
        outState.putStringArrayList("mediaIcons", mediaIcons);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mediaNames = savedInstanceState.getStringArrayList("mediaNames");
        mediaIcons = savedInstanceState.getStringArrayList("mediaIcons");
    }
}