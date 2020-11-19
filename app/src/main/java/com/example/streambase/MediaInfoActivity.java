package com.example.streambase;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class MediaInfoActivity extends AppCompatActivity {
    private TextView name;
    private ImageView image;
    private ListView services;
    private Typeface typeface;
    private ArrayAdapter mAdapter;
    private ArrayList<String> serviceList;
    private ArrayList<String> serviceIconUrls;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_info_activity);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        if (mActionBarToolbar != null) {
            getSupportActionBar().setTitle(R.string.app_name);
        }

        Intent intent = getIntent();
        String selected = intent.getStringExtra("selected");

        name = (TextView) findViewById(R.id.mediaName);
        image = (ImageView) findViewById(R.id.mediaImage);
        services = (ListView) findViewById(R.id.services);

        Activity mActivity = this;
        typeface = getResources().getFont(R.font.roboto_black);
        setServices(selected);
        mAdapter = new ArrayAdapter<String>(this, R.layout.services_cards, serviceList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the view
                LayoutInflater inflater = mActivity.getLayoutInflater();
                View itemView = inflater.inflate(R.layout.services_cards, null, true);

                // Get current package name
                String serviceName = serviceList.get(position);

                // Get the relative layout
                LinearLayout relativeLayout = (LinearLayout) itemView.findViewById(R.id.rl);

                // Display the app package name
                TextView serviceTxt = (TextView) itemView.findViewById(R.id.service_name);
                if (serviceName.equals("Amazon Prime Video")) {
                    serviceTxt.setText("Prime Video");
                } else {
                    serviceTxt.setText(serviceName);
                }

                // Get the card view
                CardView cardView = (CardView) itemView.findViewById(R.id.card_view);

                ImageView serviceIcon = (ImageView) itemView.findViewById(R.id.service_icon);
                //String imageUrl = serviceIconUrls.get(position);
                //setAppIcon(serviceIcon, imageUrl);
                int icon = getServiceIcon(serviceName);
                serviceIcon.setImageResource(icon);
                return itemView;
            }
        };

        services.setAdapter(mAdapter);
    }

    public void setServices(String selected) {
        String title = "";
        String imageUrl = "";
        serviceList = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(selected);
            title = object.getString("name");
            imageUrl = object.getString("picture");

            JSONArray servicesArray = object.getJSONArray("locations");
            for (int i = 0; i < servicesArray.length(); i++) {
                JSONObject movieOrShow = servicesArray.getJSONObject(i);
                serviceList.add(movieOrShow.getString("display_name"));
            }

            ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, serviceList) {
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
            //services.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        name.setText(title);
        SetImageTask task = new SetImageTask();
        task.execute(imageUrl);
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
            case "hulu":
                icon = R.drawable.ic_hulu;
                break;
            default:
                icon = R.drawable.ic_not_found;
                break;
        }
        return icon;
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
