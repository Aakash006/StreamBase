package com.example.streambase;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MediaInfoActivity extends AppCompatActivity {
    private static final String TAG = "MediaInfoActivity";

    private TextView name;
    private ImageView image;
    private ListView services;
    private Typeface typeface;
    private ArrayAdapter mAdapter;
    private BottomNavigationView nav;
    private String mMediaName;
    private String mImageURL;
    ArrayList<String> mList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_info_activity);

        Intent intent = getIntent();
        mMediaName = intent.getStringExtra("name");
        mImageURL = intent.getStringExtra("imageURL");
        mList = (ArrayList<String>) intent.getStringArrayListExtra("list");

        name = (TextView) findViewById(R.id.mediaName);
        image = (ImageView) findViewById(R.id.mediaImage);
        services = (ListView) findViewById(R.id.services);

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

        services.setAdapter(mAdapter);
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
        name.setText(mediaName);

        Glide.with(getApplicationContext()).load(imageURL).into(image);
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
        outState.putStringArrayList("mList", this.mList);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mMediaName = savedInstanceState.getString("mMediaName");
        this.mImageURL = savedInstanceState.getString("mImageURL");
        this.mList = savedInstanceState.getStringArrayList("mList");
    }
}
