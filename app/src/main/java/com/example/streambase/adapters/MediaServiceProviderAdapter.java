package com.example.streambase.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.streambase.R;

import java.util.List;

public class MediaServiceProviderAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> mediaServiceProviderList;

    public MediaServiceProviderAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mediaServiceProviderList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.services_cards, parent, false);
        String mediaServiceProvider = mediaServiceProviderList.get(position);

        TextView serviceTxt = convertView.findViewById(R.id.service_name);
        serviceTxt.setText(mediaServiceProvider.equals("Prime Video") || mediaServiceProvider.equals("Amazon Instant Video") ? "Amazon Prime Video" : mediaServiceProvider);

        ImageView serviceIcon = convertView.findViewById(R.id.service_icon);
        Log.d("TAG", "getView: " + mediaServiceProvider);
        int icon = getServiceIcon(mediaServiceProvider);
        serviceIcon.setImageResource(icon);

        return convertView;
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
            case "amazon instant video":
            case "prime video":
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
}
