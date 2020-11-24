package com.example.streambase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.streambase.adapters.MediaServiceProviderAdapter;
import com.example.streambase.model.MediaCollection;
import com.example.streambase.model.MediaContentProvider;
import com.example.streambase.model.TMDB;
import com.example.streambase.services.UTellyAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OLDMediaInfoActivity extends AppCompatActivity {
    private static final String TAG = "MediaInfoActivity";

    private ListView services;
    private MediaServiceProviderAdapter mMediaServiceProviderAdapter;

    private TMDB mMedia;
    private List<String> mMediaServiceProviderList;
    private List<String> mMediaServiceProviderUrlList;
    private boolean isFavourite = false;
    private StreamBaseDB mDB;
    private static final String REMOVE_FAV = "Remove Favourite";
    private static final String ADD_FAV = "Add to Favourite";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.old_media_info_activity);
        mDB = new StreamBaseDB(getApplicationContext(), null, null, 0);

        Intent intent = getIntent();
        mMedia = intent.getParcelableExtra("data");

        Toolbar actionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBarToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(mMedia.getMovieName() == null ? mMedia.getTvShowName() : mMedia.getMovieName());

        ImageView posterImgView = findViewById(R.id.mediaImage);
        Button favoriteBtn = findViewById(R.id.add_to_db);
        if (mDB.isFavourite(mMedia.getId())) {
            isFavourite = true;
            favoriteBtn.setText(REMOVE_FAV);
        } else {
            favoriteBtn.setText(ADD_FAV);
        }
        services = findViewById(R.id.services);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        mMediaServiceProviderList = new ArrayList<>();
        mMediaServiceProviderUrlList = new ArrayList<>();

        fetchMediaContentProviderList(mMedia.getId());
        Glide.with(getApplicationContext())
                .load(getString(R.string.image_tmdb_url_original) + mMedia.getImageURLBackdrop())
                .into(posterImgView);

        mMediaServiceProviderAdapter = new MediaServiceProviderAdapter(getApplicationContext(), 0, mMediaServiceProviderList, mMediaServiceProviderUrlList);

        favoriteBtn.setOnClickListener(e -> {
            if (!isFavourite) {
                favoriteBtn.setText(REMOVE_FAV);

                mDB.addRecord(mMedia, mMediaServiceProviderList);
                Toast.makeText(getApplicationContext(), "Success media added!", Toast.LENGTH_SHORT).show();
            } else {
                boolean removed = mDB.removeFavourite(mMedia.getId());
                if (removed) {
                    favoriteBtn.setText(ADD_FAV);
                    Toast.makeText(getApplicationContext(), "Media successfully removed from favourites!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Media could not removed from favourites!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("data", this.mMedia);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mMedia = savedInstanceState.getParcelable("data");
    }

    public void fetchMediaContentProviderList(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.utelly_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Map<String, String> queryParametersMap = new HashMap<>();
        queryParametersMap.put("source_id", String.valueOf(id));
        queryParametersMap.put("source", "tmdb");

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("x-rapidapi-host", getString(R.string.host));
        headersMap.put("x-rapidapi-key", getString(R.string.utelly_key));

        UTellyAPI api = retrofit.create(UTellyAPI.class);
        Call<MediaCollection> call = api.getMediaList(queryParametersMap, headersMap);
        call.enqueue(new Callback<MediaCollection>() {
            @Override
            public void onResponse(Call<MediaCollection> call, Response<MediaCollection> response) {
                if (response.isSuccessful()) {
                    ArrayList<MediaContentProvider> mediaContentProviders = response.body().getMedia().getMediaContentProviderList();
                    if (mediaContentProviders != null) {
                        for (MediaContentProvider mc : mediaContentProviders) {
                            mMediaServiceProviderList.add(mc.getMediaContentProviderName());
                            mMediaServiceProviderUrlList.add(mc.getStreamURL());
                        }
                        services.setAdapter(mMediaServiceProviderAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<MediaCollection> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.getStackTrace();
            }
        });
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = item -> {
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



}
