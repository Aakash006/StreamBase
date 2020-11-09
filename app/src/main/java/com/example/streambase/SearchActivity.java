package com.example.streambase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    EditText search;
    ImageButton searchBtn;
    ListView listOfResults;
    Button backBtn;
    private RequestQueue queue;
    private JSONArray cache;
    private Typeface typeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        search = (EditText) findViewById(R.id.search);
        searchBtn = (ImageButton) findViewById(R.id.searchBtn);
        backBtn = (Button) findViewById(R.id.backBtn);
        listOfResults = (ListView) findViewById(R.id.listOfResults);
        queue = Volley.newRequestQueue(this);
        typeface = getResources().getFont(R.font.palanquin_regular);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!search.getText().toString().equals("")) {
                    StringRequest getMovie = searchNameStringRequest(search.getText().toString());
                    queue.add(getMovie);
                } else {
                    Toast.makeText(SearchActivity.this, "No movie specified", Toast.LENGTH_LONG).show();
                }

            }
        });

        listOfResults.setVisibility(View.INVISIBLE);

        listOfResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    JSONObject selected = cache.getJSONObject(position);
                    Intent intent = new Intent(SearchActivity.this, MediaInfoActivity.class);
                    intent.putExtra("selected", selected.toString());
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private StringRequest searchNameStringRequest(String nameSearch) {
        String url = getString(R.string.url) + nameSearch + "&country=ca";
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);
                            cache = result.getJSONArray("results");
                            JSONArray results = result.getJSONArray("results");
                            ArrayList<String> moviesOrShows = new ArrayList<>();
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject movieOrShow = results.getJSONObject(i);
                                moviesOrShows.add(movieOrShow.getString("name"));
                            }

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

                        } catch (JSONException e) {
                            Toast.makeText(SearchActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(SearchActivity.this, "Utelly is not responding", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                headers.put("x-rapidapi-host", getString(R.string.host));
                headers.put("x-rapidapi-key", getString(R.string.key));
                return headers;
            }
        };
    }

    public void backHome(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}