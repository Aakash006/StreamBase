package com.example.streambase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
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
        Call<TMDBList> call = api.getTrendingList(queryParametersMap);

        call.enqueue(new Callback<TMDBList>() {
            @Override
            public void onResponse(Call<TMDBList> call, Response<TMDBList> response) {
                if(response.isSuccessful()) {
                    mediaList = response.body();
                    ArrayList<String> mediaNames = new ArrayList<>();
                    ArrayList<String> mediaIcons = new ArrayList<>();
                    for (TMDBModel m: mediaList.getMedia()) {
                        if (m.getName() != null) {
                            mediaNames.add(m.getName());
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

    public void setCards(List<String> mediaNames, List<String> mediaIcons) {
        Activity mActivity = this;
        mAdapter = new ArrayAdapter<String>(this, R.layout.trending_cards, mediaNames) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the view
                LayoutInflater inflater = mActivity.getLayoutInflater();
                View itemView = inflater.inflate(R.layout.trending_cards, null, true);

                // Get media name
                String mediaName = mediaNames.get(position);
                String mediaIcon = mediaIcons.get(position);


                // Get the relative layout
                LinearLayout relativeLayout = (LinearLayout) itemView.findViewById(R.id.rl);

                // Display the media name
                TextView mediaTxt = (TextView) itemView.findViewById(R.id.media_name);
                mediaTxt.setText(mediaName);

                System.out.println("set");

                // Get the card view
                CardView cardView = (CardView) itemView.findViewById(R.id.card_view);

                ImageView serviceIcon = (ImageView) itemView.findViewById(R.id.media_icon);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.image_tmdb_url))
                        .build();
                ImageTMDBAPI api = retrofit.create(ImageTMDBAPI.class);
                Call<ResponseBody> call = api.getImage(getString(R.string.image_tmdb_url) + mediaIcon);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()) {
                            Bitmap bitmap  = BitmapFactory.decodeStream(response.body().byteStream());
                            serviceIcon.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
                return itemView;
            }
        };

        trends.setAdapter(mAdapter);
    }

}