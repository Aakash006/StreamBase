package com.example.streambase;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class FavouriteActivity extends AppCompatActivity {
    private BottomNavigationView nav;
    private StreamBaseDB db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_activity);

        nav = findViewById(R.id.bottom_nav);
        nav.setSelectedItemId(R.id.nav_fav);
        nav.setOnNavigationItemSelectedListener(navLlistener);

        db = new StreamBaseDB(this,null,null,1);
        getFavourites();
    }

    public void getFavourites() {
        Cursor data = db.getFavourites();
        ArrayList<String> notesList = new ArrayList<>();
        while (data.moveToNext()) {
            notesList.add(data.getString(0));
            System.out.println(data.getString(0));
        }
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
