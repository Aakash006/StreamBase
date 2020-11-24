package com.example.streambase;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.streambase.model.PageViewModel;
import com.example.streambase.model.TMDB;

/**
 * A placeholder fragment containing a simple view.
 */
public class SummaryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;

    private TMDB mMedia;

    private TextView summaryLabel;

    public SummaryFragment(TMDB mMedia){
        this.mMedia = mMedia;
    }

    public static SummaryFragment newInstance(TMDB mMedia) {
        SummaryFragment fragment = new SummaryFragment(mMedia);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
//        int index = 1;
//        if (getArguments() != null) {
//            index = getArguments().getInt(ARG_SECTION_NUMBER);
//        }
//        pageViewModel.setIndex(index);
//
//        mMedia = savedInstanceState.getParcelable("data");
//    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        //mMedia = this.getArguments().getParcelable("data");

        View root = inflater.inflate(R.layout.overview_fragment, container, false);
        summaryLabel = root.findViewById(R.id.summary_label);
//        pageViewModel.getText().observe(this.getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//
//            }
//        });

        summaryLabel.setText(mMedia.getOverview());



        return root;
    }

}