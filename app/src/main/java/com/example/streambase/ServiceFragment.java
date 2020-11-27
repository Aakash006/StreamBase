package com.example.streambase;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.streambase.adapters.MediaServiceProviderAdapter;
import com.example.streambase.model.MediaCollection;
import com.example.streambase.model.MediaContentProvider;
import com.example.streambase.model.TMDB;
import com.example.streambase.services.OnCallbackReceived;
import com.example.streambase.services.UTellyAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class ServiceFragment extends Fragment {
    private static final String TAG = "ServiceFragment";

    ListView services;
    private MediaServiceProviderAdapter mMediaServiceProviderAdapter;

    private TMDB mMedia;
    private List<String> mMediaServiceProviderList;
    private List<String> mMediaServiceProviderUrlList;

    OnCallbackReceived mCallback;

    public ServiceFragment(){
        Bundle args = new Bundle();
        this.setArguments(args);
    }

    private void setMedia(TMDB mMedia){
        this.mMedia = mMedia;
    }

    public static ServiceFragment newInstance(TMDB mMedia) {
        ServiceFragment fragment = new ServiceFragment();
        fragment.setMedia(mMedia);

        Bundle args = new Bundle();
        args.putParcelable("data", mMedia);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if(savedInstanceState != null){
            super.onCreate(savedInstanceState);
            this.mMedia = savedInstanceState.getParcelable("data");
        }

        View root = inflater.inflate(R.layout.services_fragment, container, false);
        services = (ListView) root.findViewById(R.id.services);
        services.setNestedScrollingEnabled(true);

        mMediaServiceProviderList = new ArrayList<>();

        mMediaServiceProviderList = new ArrayList<>();
        mMediaServiceProviderUrlList = new ArrayList<>();

        fetchMediaContentProviderList(mMedia.getId());
        mMediaServiceProviderAdapter = new MediaServiceProviderAdapter(getActivity().getApplicationContext(), R.id.services, mMediaServiceProviderList, mMediaServiceProviderUrlList);
        mCallback.Update(mMediaServiceProviderList);

        return root;
    }

    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("data", this.mMedia);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (OnCallbackReceived) activity;
        } catch (ClassCastException e) {

        }
    }

    public void fetchMediaContentProviderList(int id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.utelly_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Map<String, String> queryParametersMap = new HashMap<>();
        queryParametersMap.put("source_id", String.valueOf(id));
        queryParametersMap.put("source", "tmdb");

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("x-rapidapi-host", getString(R.string.host));
        headersMap.put("x-rapidapi-key", getString(R.string.utelly_key));

        UTellyAPI api = retrofit.create(UTellyAPI.class);
        Call<MediaCollection> call = api.getMediaList(queryParametersMap, headersMap);
        call.enqueue(new Callback<MediaCollection>() {
            @Override
            public void onResponse(Call<MediaCollection> call, Response<MediaCollection> response) {
                if (response.isSuccessful()) {
                    ArrayList<MediaContentProvider> mediaContentProviders = response.body().getMedia().getMediaContentProviderList();
                    if (mediaContentProviders != null) {
                        for (MediaContentProvider mc : mediaContentProviders) {
                            mMediaServiceProviderList.add(mc.getMediaContentProviderName());
                            mMediaServiceProviderUrlList.add(mc.getStreamURL());
                        }
                        services.setAdapter(mMediaServiceProviderAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<MediaCollection> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                t.getStackTrace();
            }
        });
    }

}