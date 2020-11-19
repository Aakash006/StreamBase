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
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";

    EditText search;
    ListView listOfResults;
    private RequestQueue queue;
    private JSONArray cache;
    private Typeface typeface;
    private Retrofit retrofit;
    private MediaList mediaList;
    private BottomNavigationView nav;

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

        search.setOnKeyListener((view, i, keyEvent) -> {
            // if Enter key is pressed invoke Volley
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.utelly_base_url))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                fetchMedia(search.getText().toString());
                return true;
            }
            return false;
        });


        listOfResults.setVisibility(View.INVISIBLE);

        listOfResults.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(SearchActivity.this, MediaInfoActivity.class);
            String selectedItem = adapterView.getItemAtPosition(i).toString();
            ArrayList<String> providers = new ArrayList<>();
            for (Media m : mediaList.getMedia()) {
                if(m.getName().equals(selectedItem)) {
                    intent.putExtra("name", m.getName());
                    intent.putExtra("imageURL", m.getImageURL());
                    for(MediaContentProvider mc : m.getMediaContentProviderList()) providers.add(mc.getMediaContentProviderName());
                    intent.putExtra("list", providers);
                    break;
                }
            }
            startActivity(intent);
        });
    }

    private void fetchMedia(String mediaName) {
        Map<String, String> queryParametersMap = new HashMap<>();
        queryParametersMap.put("term", mediaName);
        queryParametersMap.put("country", "ca");

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("x-rapidapi-host", getString(R.string.host));
        headersMap.put("x-rapidapi-key", getString(R.string.utelly_key));

        UTellyAPI api = retrofit.create(UTellyAPI.class);
        Call<MediaList> call = api.getMediaList(queryParametersMap, headersMap);

        call.enqueue(new Callback<MediaList>() {
            @Override
            public void onResponse(Call<MediaList> call, retrofit2.Response<MediaList> response) {
                if(response.isSuccessful()) {
                    mediaList = response.body();
                    List<String> moviesOrShows = new ArrayList<>();
                    for (Media m : mediaList.getMedia()) {
                        moviesOrShows.add(m.getName());
                    }
                    createAdapter(moviesOrShows);
                }
            }

            @Override
            public void onFailure(Call<MediaList> call, Throwable t) {
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
}