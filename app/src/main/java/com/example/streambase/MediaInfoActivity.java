package com.example.streambase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.streambase.model.MediaCollection;
import com.example.streambase.model.MediaContentProvider;
import com.example.streambase.services.UTellyAPI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MediaInfoActivity extends AppCompatActivity {
    private static final String TAG = "MediaInfoActivity";

    private ImageView image;
    private ListView services;
    private Typeface typeface;
    private ArrayAdapter mAdapter;
    private Toolbar mActionBarToolbar;
    private BottomNavigationView nav;
    private String mMediaName;
    private String mImageURL;
    private ArrayList<String> mList;
    private Button mAddToDBBtn;
    private int mId;
    private StreamBaseDB db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_info_activity);

        mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);


        db = new StreamBaseDB(getApplicationContext(), null, null, 0);
        mList = new ArrayList<>(); // Declare empty arraylist so in the background it will be updated.

        Intent intent = getIntent();
        mMediaName = intent.getStringExtra("name");
        mImageURL = intent.getStringExtra("imageURL");
        mId = intent.getIntExtra("id", 0);
        fetchMediaContentProviderList(mId);

        getSupportActionBar().setTitle(mMediaName);
        image = (ImageView) findViewById(R.id.mediaImage);
        services = (ListView) findViewById(R.id.services);
        mAddToDBBtn = (Button) findViewById(R.id.add_to_db);

        nav = findViewById(R.id.bottom_nav);
        nav.setSelectedItemId(R.id.nav_search);
        nav.setOnNavigationItemSelectedListener(navLlistener);

        setLayout(mMediaName, mImageURL, mList);

        Activity mActivity = this;
        typeface = getResources().getFont(R.font.roboto_black);
        mAdapter = new ArrayAdapter<String>(this, R.layout.services_cards, mList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the view
                LayoutInflater inflater = mActivity.getLayoutInflater();
                View itemView = inflater.inflate(R.layout.services_cards, null, true);

                // Get service name
                String serviceName = mList.get(position);

                // Get the relative layout
                LinearLayout relativeLayout = (LinearLayout) itemView.findViewById(R.id.rl);

                // Display the service name
                TextView serviceTxt = (TextView) itemView.findViewById(R.id.service_name);
                if (serviceName.equals("Amazon Prime Video")) {
                    serviceTxt.setText("Prime Video");
                } else {
                    serviceTxt.setText(serviceName);
                }

                // Get the card view
                CardView cardView = (CardView) itemView.findViewById(R.id.card_view);

                ImageView serviceIcon = (ImageView) itemView.findViewById(R.id.service_icon);

                int icon = getServiceIcon(serviceName);
                serviceIcon.setImageResource(icon);
                return itemView;
            }
        };


        mAddToDBBtn.setOnClickListener(e -> {
//            Log.d(TAG, "onCreate: " + mMediaName);
//            Log.d(TAG, "onCreate: " + mImageURL);
//            Log.d(TAG, "onCreate: " + mId);
//            Log.d(TAG, "onCreate: " + mList.toString());
            db.addRecord(mId, mMediaName, mImageURL, mList);
        });
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
                            mList.add(mc.getMediaContentProviderName());
                            services.setAdapter(mAdapter);
                        }
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

    public void setLayout(String mediaName, String imageURL, ArrayList<String> list) {
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                /// Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text size 20 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                tv.setTextColor(Color.parseColor("#ffffff"));
                tv.setTypeface(typeface);

                // Return the view
                return view;
            }
        };

        Glide.with(getApplicationContext()).load(getString(R.string.image_tmdb_url) + imageURL).into(image);
    }

    public int getServiceIcon(String name) {
        int icon = 0;
        name = name.toLowerCase();
        switch (name) {
            case "netflix":
                icon = R.drawable.ic_netflix;
                break;
            case "itunes":
                icon = R.drawable.ic_itunes;
                break;
            case "amazon prime video":
                icon = R.drawable.ic_prime_video;
                break;
            case "google play":
                icon = R.drawable.ic_google_play;
                break;
            case "disney+":
                icon = R.drawable.ic_disney_plus;
                break;
            case "appletv+":
                icon = R.drawable.ic_apple_tv_plus;
                break;
            case "hulu":
                icon = R.drawable.ic_hulu;
                break;
            default:
                icon = R.drawable.ic_not_found;
                break;
        }
        return icon;
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


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mMediaName", this.mMediaName);
        outState.putString("mImageURL", this.mImageURL);
        outState.putInt("id", this.mId);
        outState.putStringArrayList("mList", this.mList);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mMediaName = savedInstanceState.getString("mMediaName");
        this.mImageURL = savedInstanceState.getString("mImageURL");
        this.mId = savedInstanceState.getInt("id");
        this.mList = savedInstanceState.getStringArrayList("mList");
    }
}
