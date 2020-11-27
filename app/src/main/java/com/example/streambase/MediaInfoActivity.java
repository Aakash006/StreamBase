package com.example.streambase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.streambase.adapters.MediaInfoPagerAdapter;
import com.example.streambase.adapters.MediaServiceProviderAdapter;
import com.example.streambase.model.TMDB;
import com.example.streambase.services.OnCallbackReceived;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.List;
import java.util.Objects;

public class MediaInfoActivity extends AppCompatActivity implements OnCallbackReceived {

    private static final String TAG = "MediaInfoActivity";

    private ListView services;
    private MediaServiceProviderAdapter mMediaServiceProviderAdapter;

    private TMDB mMedia;
    private List<String> mMediaServiceProviderList;
    private boolean isFavourite = false;
    private StreamBaseDB mDB;
    private static final String REMOVE_FAV = "Remove Favourite";
    private static final String ADD_FAV = "Add to Favourite";

    SummaryFragment summaryFragment;
    ServiceFragment serviceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_info_activity);

        if (savedInstanceState != null) {
            summaryFragment = (SummaryFragment) getSupportFragmentManager().getFragment(savedInstanceState, "summaryFragment");
            serviceFragment = (ServiceFragment) getSupportFragmentManager().getFragment(savedInstanceState, "serviceFragment");

        }

        mDB = new StreamBaseDB(getApplicationContext(), null, null, 0);

        Intent intent = getIntent();
        mMedia = intent.getParcelableExtra("data");

        MaterialToolbar actionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBarToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(mMedia.getMovieName() == null ? mMedia.getTvShowName() : mMedia.getMovieName());

        ImageView posterImgView = findViewById(R.id.mediaImage);
        FloatingActionButton favoriteBtn = findViewById(R.id.fav_add_to_db);
        if (mDB.isFavourite(mMedia.getId())) {
            isFavourite = true;
            favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_red_24);
        } else {
            favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_grey_24);
        }


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.nav_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        ViewPager viewPager = findViewById(R.id.view_pager);

        MediaInfoPagerAdapter mediaInfoPagerAdapter = new MediaInfoPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        summaryFragment = SummaryFragment.newInstance(mMedia);
        serviceFragment = ServiceFragment.newInstance(mMedia);
        mediaInfoPagerAdapter.addFragment(summaryFragment, getString(R.string.tab_1));
        mediaInfoPagerAdapter.addFragment(serviceFragment, getString(R.string.tab_2));
        viewPager.setAdapter(mediaInfoPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(getColor(R.color.colorSecondary));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Glide.with(getApplicationContext()).load(getString(R.string.image_tmdb_url_original) + mMedia.getImageURLBackdrop()).into(posterImgView);


        favoriteBtn.setOnClickListener(view -> {
            final View snackBarPos = findViewById(R.id.snackbar_pos);

            if (!isFavourite) {
                favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_red_24);

                mDB.addRecord(mMedia, mMediaServiceProviderList);
                Snackbar.make(snackBarPos, "Success media added!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                isFavourite = true;
            } else {
                boolean removed = mDB.removeFavourite(mMedia.getId());
                if (removed) {
                    favoriteBtn.setImageResource(R.drawable.ic_baseline_favorite_grey_24);
                    Snackbar.make(snackBarPos, "Media successfully removed from favourites!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(snackBarPos, "Media could not removed from favourites!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                isFavourite = false;
            }
        });
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("data", this.mMedia);
        if (summaryFragment.isAdded() && serviceFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "summaryFragment", summaryFragment);
            getSupportFragmentManager().putFragment(outState, "serviceFragment", serviceFragment);
        }
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mMedia = savedInstanceState.getParcelable("data");
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

    @Override
    public void Update(List<String> mMediaServiceProviderList) {
        this.mMediaServiceProviderList = mMediaServiceProviderList;
    }
}