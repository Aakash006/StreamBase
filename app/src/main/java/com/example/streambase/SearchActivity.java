package com.example.streambase;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.streambase.model.MediaContentProvider;
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

    EditText search;
    ListView listOfResults;
    private Typeface typeface;
    private TMDBList mediaList;
    private BottomNavigationView nav;
    private Toolbar mActionBarToolbar;
    private ArrayList<MediaContentProvider> result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        nav = findViewById(R.id.bottom_nav);
        nav.setSelectedItemId(R.id.nav_search);
        nav.setOnNavigationItemSelectedListener(navLlistener);

        search = (EditText) findViewById(R.id.search);
        listOfResults = (ListView) findViewById(R.id.listOfResults);
        typeface = getResources().getFont(R.font.roboto_medium2);
        mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Search");

        search.setOnKeyListener((view, i, keyEvent) -> {
            // if Enter key is pressed invoke Volley
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {

                fetchMedia(search.getText().toString());
                return true;
            }
            return false;
        });


        listOfResults.setVisibility(View.INVISIBLE);

        listOfResults.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(SearchActivity.this, MediaInfoActivity.class);
            String selectedItem = adapterView.getItemAtPosition(i).toString();
            for (TMDB m : mediaList.getMedia()) {
                if (m.getMediaType().equals("tv")) {
                    if (m.getTvShowName().equals(selectedItem)) {
                        intent.putExtra("name", m.getTvShowName());
                        intent.putExtra("imageURL", m.getImageURL());
                        intent.putExtra("id", m.getId());
                        break;
                    }
                } else if (m.getMediaType().equals("movie")) {
                    if (m.getMovieName().equals(selectedItem)) {
                        intent.putExtra("name", m.getMovieName());
                        intent.putExtra("imageURL", m.getImageURL());
                        intent.putExtra("id", m.getId());
                        break;
                    }
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
                    mediaList = response.body();
                    List<String> moviesOrShows = new ArrayList<>();
                    for (TMDB m : mediaList.getMedia()) {
                        if (m.getMediaType().equals("tv")) {
                            moviesOrShows.add(m.getTvShowName());
                        } else if (m.getMediaType().equals("movie")) {
                            moviesOrShows.add(m.getMovieName());
                        }
                    }
                    createAdapter(moviesOrShows);
                }
            }

            @Override
            public void onFailure(Call<TMDBList> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.getStackTrace();
            }
        });

    }

    private void createAdapter(List<String> moviesOrShows) {
        ListAdapter adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_list_item_1, moviesOrShows) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
                tv.setTypeface(typeface);

                // Return the view
                return view;
            }
        };
        listOfResults.setVisibility(View.VISIBLE);
        listOfResults.setAdapter(adapter);
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navLlistener = new BottomNavigationView.OnNavigationItemSelectedListener() {
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
}