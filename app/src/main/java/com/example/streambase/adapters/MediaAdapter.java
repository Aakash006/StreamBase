package com.example.streambase.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.streambase.MediaInfoActivity;
import com.example.streambase.R;
import com.example.streambase.model.TMDB;

import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {

    private Context mContext;
    private List<TMDB> mediaList;

    public MediaAdapter(Context context, List<TMDB> mediaList) {
        this.mContext = context;
        this.mediaList = mediaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trending_cards, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mediaList.get(position).getMediaType().equals("tv")) {
            holder.mediaName.setText(mediaList.get(position).getTvShowName());
        } else if (mediaList.get(position).getMediaType().equals("movie")) {
            holder.mediaName.setText(mediaList.get(position).getMovieName());
        }
        Glide.with(mContext)
                .load(mContext.getString(R.string.image_tmdb_url) + mediaList.get(position).getImageURL())
                .into(holder.mediaImage);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mediaName;
        public ImageView mediaImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mediaName = itemView.findViewById(R.id.media_name);
            mediaImage = itemView.findViewById(R.id.media_icon);
            itemView.setOnClickListener(event -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    TMDB mediaItem = mediaList.get(pos);
                    Intent intent = new Intent(mContext, MediaInfoActivity.class);

                    if (mediaItem.getMediaType().equals("tv"))
                        intent.putExtra("name", mediaItem.getTvShowName());
                    if (mediaItem.getMediaType().equals("movie"))
                        intent.putExtra("name", mediaItem.getMovieName());
                    intent.putExtra("imageURL", mediaItem.getImageURL());
                    intent.putExtra("id", mediaItem.getId());
                    intent.putExtra("data", mediaItem);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
