package com.example.streambase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private Typeface typeface;
    private Retrofit retrofit;
    private MediaList mediaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        search = (EditText) findViewById(R.id.search);
        listOfResults = (ListView) findViewById(R.id.listOfResults);
        typeface = getResources().getFont(R.font.roboto_medium2);

        search.setOnKeyListener((view, i, keyEvent) -> {
            // if Enter key is pressed invoke Volley
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(getString(R.string.base_url))
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
        headersMap.put("x-rapidapi-key", getString(R.string.key));

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

    public void createAdapter(List<String> moviesOrShows) {
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
}