package com.maxclub.android.easyweather;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.NotNull;

public class SearchWeatherFragment extends Fragment {

    private TextView mTextView;

    public static SearchWeatherFragment newInstance() {
        return new SearchWeatherFragment();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_weather_fragment, container, false);

        mTextView = (TextView) view.findViewById(R.id.weather_textview);

        return view;
    }
}
