package com.example.streambase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.streambase.model.TMDB;

/**
 * A placeholder fragment containing a simple view.
 */
public class SummaryFragment extends Fragment {

    private TMDB mMedia;
    private NestedScrollView mNestedScrollView;
    private TextView summaryLabel;

    public SummaryFragment(){
        Bundle args = new Bundle();
        args.putParcelable("data", this.mMedia);
        this.setArguments(args);
    }

    private void setMedia(TMDB mMedia){
        this.mMedia = mMedia;
    }

    public static SummaryFragment newInstance(TMDB mMedia) {
        SummaryFragment fragment = new SummaryFragment();
        fragment.setMedia(mMedia);

        Bundle args = new Bundle();
        args.putParcelable("data", mMedia);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            super.onCreate(savedInstanceState);
            this.mMedia = savedInstanceState.getParcelable("data");
        }

        View root = inflater.inflate(R.layout.summary_fragment, container, false);
        summaryLabel = root.findViewById(R.id.summary_label);

        mNestedScrollView = root.findViewById(R.id.nester_scroll_view1);

        summaryLabel.setText(mMedia.getOverview());

        return root;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("data", this.mMedia);
    }
}