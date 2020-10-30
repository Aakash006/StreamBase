package com.example.streambase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText search;
    Button searchBtn;
    TextView service;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = (EditText) findViewById(R.id.search);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        service = (TextView) findViewById(R.id.service);
        queue = Volley.newRequestQueue(this);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!search.getText().toString().equals("")) {
                    StringRequest getMovie = searchNameStringRequest(search.getText().toString());
                    queue.add(getMovie);
                } else {
                    Toast.makeText(MainActivity.this, "No movie specified", Toast.LENGTH_LONG).show();
                }

            }
        });
//        HttpResponse<String> response = Unirest.get("https://utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com/lookup?term=bojack&country=uk")
//                .header("x-rapidapi-host", "utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com")
//                .header("x-rapidapi-key", "402dce7785mshe730c925b64cc64p13f8d3jsn8ebc10c3980e")
//                .asString();
    }

    private StringRequest searchNameStringRequest(String nameSearch) {
        String url = "https://utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com/lookup?term=" + nameSearch + "&country=ca";
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    // SUCCESS
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject result = new JSONObject(response);
                            JSONArray jsonArray = result.getJSONArray("results");
                            JSONObject info = jsonArray.getJSONObject(0);
                            JSONArray locations = info.getJSONArray("locations");
                            JSONObject first = locations.getJSONObject(0);
                            String url = first.getString("display_name");

                            service.setText(url);
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    // ERROR
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // display a simple message on the screen
                        Toast.makeText(MainActivity.this, "Utelly is not responsing", Toast.LENGTH_LONG).show();
                    }
                }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String>  headers = new HashMap<String, String>();
                    headers.put("x-rapidapi-host", "utelly-tv-shows-and-movies-availability-v1.p.rapidapi.com");
                    headers.put("x-rapidapi-key", "402dce7785mshe730c925b64cc64p13f8d3jsn8ebc10c3980e");

                    return headers;
                }
        };
    }
}