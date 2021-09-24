package com.maxclub.easyweather;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.maxclub.easyweather.api.model.OneCallWeatherData;

import org.jetbrains.annotations.NotNull;

public class DailyWeatherDialogFragment extends DialogFragment {

    private static final String ARG_DAILY_WEATHER = "daily_weather";

    public static DailyWeatherDialogFragment newInstance(OneCallWeatherData.Daily dailyWeather) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_DAILY_WEATHER, dailyWeather);

        DailyWeatherDialogFragment fragment = new DailyWeatherDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        OneCallWeatherData.Daily dailyWeather = (OneCallWeatherData.Daily) getArguments().get(ARG_DAILY_WEATHER);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_daily_weather, null);

        //

        Dialog dialog = new AlertDialog.Builder(getActivity()).setView(view).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }
}
