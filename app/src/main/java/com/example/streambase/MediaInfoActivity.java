package com.example.streambase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MediaInfoActivity extends AppCompatActivity {
    private TextView name;
    private ImageView image;
    private ListView services;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_info_activity);

        Intent intent = getIntent();
        String selected = intent.getStringExtra("selected");

        name = (TextView) findViewById(R.id.mediaName);
        image = (ImageView) findViewById(R.id.mediaImage);
        services = (ListView) findViewById(R.id.services);
        String title = "";
        String imageUrl = "";

        try {
            JSONObject object = new JSONObject(selected);
            title = object.getString("name");
            imageUrl = object.getString("picture");

            JSONArray servicesArray = object.getJSONArray("locations");
            ArrayList<String> serviceList = new ArrayList<>();
            for (int i = 0; i < servicesArray.length(); i++) {
                JSONObject movieOrShow = servicesArray.getJSONObject(i);
                serviceList.add(movieOrShow.getString("display_name"));
            }

            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, serviceList);
            services.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        name.setText(title);
        SetImageTask task = new SetImageTask();
        task.execute(imageUrl);
    }

    class SetImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            image.setImageBitmap(result);
        }
    }
}
