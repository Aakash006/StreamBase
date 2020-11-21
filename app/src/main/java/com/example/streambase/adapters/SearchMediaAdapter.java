package com.example.streambase.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.streambase.R;

import java.util.List;

public class SearchMediaAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private List<String> searchResults;

    public SearchMediaAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.searchResults = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);

        Typeface typeface = mContext.getResources().getFont(R.font.roboto_medium2);

        TextView searchTxt = convertView.findViewById(android.R.id.text1);

        searchTxt.setTypeface(typeface);
        searchTxt.setText(searchResults.get(position));
        searchTxt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        return convertView;
    }
}
