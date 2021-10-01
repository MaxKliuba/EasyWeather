package com.maxclub.easyweather;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.maxclub.easyweather.api.model.OneCallWeatherData;
import com.maxclub.easyweather.utils.DateTimeHelper;
import com.maxclub.easyweather.utils.StringHelper;

import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class DailyWeatherDialogFragment extends DialogFragment {

    private static final String ARG_DAILY_WEATHER = "daily_weather";
    private static final String ARG_TIMEZONE_OFFSET = "timezone_offset";

    private TextView mDateTextView;
    private ImageView mIconImageView;
    private TextView mTempMaxMinTextView;
    private TextView mDescriptionTextView;
    private TextView mTempNightTextView;
    private TextView mTempMorningTextView;
    private TextView mTempDayTextView;
    private TextView mTempEveningTextView;
    private TextView mWindTextView;
    private TextView mPopTextView;
    private TextView mUviTextView;
    private TextView mDewPointTextView;
    private TextView mHumidityTextView;
    private TextView mPressureTextView;
    private TextView mSunriseTextView;
    private TextView mSunsetTextView;

    public static DailyWeatherDialogFragment newInstance(OneCallWeatherData.Daily dailyWeather, int timezoneOffset) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_DAILY_WEATHER, dailyWeather);
        args.putInt(ARG_TIMEZONE_OFFSET, timezoneOffset);

        DailyWeatherDialogFragment fragment = new DailyWeatherDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        OneCallWeatherData.Daily dailyWeather = (OneCallWeatherData.Daily) getArguments().get(ARG_DAILY_WEATHER);
        int timezoneOffset = (int) getArguments().getInt(ARG_TIMEZONE_OFFSET);

        WeatherDrawableManager weatherDrawableManager = new WeatherDrawableManager(getActivity());

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_daily_weather, null);

        mDateTextView = (TextView) view.findViewById(R.id.dialog_daily_date_text_view);
        mIconImageView = (ImageView) view.findViewById(R.id.dialog_daily_icon_image_view);
        mDescriptionTextView = (TextView) view.findViewById(R.id.dialog_daily_description_text_view);
        mTempMaxMinTextView = (TextView) view.findViewById(R.id.dialog_daily_temp_text_view);
        mTempNightTextView = (TextView) view.findViewById(R.id.dialog_daily_night_temp_text_view);
        mTempMorningTextView = (TextView) view.findViewById(R.id.dialog_daily_morning_temp_text_view);
        mTempDayTextView = (TextView) view.findViewById(R.id.dialog_daily_day_temp_text_view);
        mTempEveningTextView = (TextView) view.findViewById(R.id.dialog_daily_evening_temp_text_view);
        mWindTextView = (TextView) view.findViewById(R.id.dialog_daily_wind_text_view);
        mPopTextView = (TextView) view.findViewById(R.id.dialog_daily_pop_text_view);
        mUviTextView = (TextView) view.findViewById(R.id.dialog_daily_uvi_text_view);
        mDewPointTextView = (TextView) view.findViewById(R.id.dialog_daily_dew_point_text_view);
        mHumidityTextView = (TextView) view.findViewById(R.id.dialog_daily_humidity_text_view);
        mPressureTextView = (TextView) view.findViewById(R.id.dialog_daily_pressure_text_view);
        mSunriseTextView = (TextView) view.findViewById(R.id.dialog_daily_sunrise_text_view);
        mSunsetTextView = (TextView) view.findViewById(R.id.dialog_daily_sunset_text_view);

        mDateTextView.setText(StringHelper.capitalize(
                DateTimeHelper.getFormattedFullDate(getActivity(),
                        new Date((dailyWeather.dt + timezoneOffset) * 1000L))));
        mIconImageView.setImageDrawable(
                weatherDrawableManager.getDrawableByName(dailyWeather.weather.get(0).icon)
        );
        mDescriptionTextView.setText(
                StringHelper.capitalize(dailyWeather.weather.get(0).description)
        );

        String[] windDirections = getResources().getStringArray(R.array.wind_directions);
        int index = Math.round(dailyWeather.windDeg / 45.0f);
        String windDirection = windDirections[index >= windDirections.length ? 0 : index];
        switch (SettingsPreferences.getUnits(getActivity())) {
            case SettingsPreferences.IMPERIAL:
                mTempMaxMinTextView.setText(getString(R.string.temp_max_min_f_label,
                        dailyWeather.temp.max, dailyWeather.temp.min));
                mTempNightTextView.setText(getString(R.string.temp_and_feels_like_f_label,
                        dailyWeather.temp.night, dailyWeather.feelsLike.night));
                mTempMorningTextView.setText(getString(R.string.temp_and_feels_like_f_label,
                        dailyWeather.temp.morn, dailyWeather.feelsLike.morn));
                mTempDayTextView.setText(getString(R.string.temp_and_feels_like_f_label,
                        dailyWeather.temp.day, dailyWeather.feelsLike.day));
                mTempEveningTextView.setText(getString(R.string.temp_and_feels_like_f_label,
                        dailyWeather.temp.eve, dailyWeather.feelsLike.eve));
                mWindTextView.setText(getString(R.string.wind_mph_label,
                        dailyWeather.windSpeed, windDirection));
                mDewPointTextView.setText(getString(R.string.temp_f_label,
                        dailyWeather.dewPoint));
                break;
            case SettingsPreferences.STANDARD:
                mTempMaxMinTextView.setText(getString(R.string.temp_max_min_k_label,
                        dailyWeather.temp.max, dailyWeather.temp.min));
                mTempNightTextView.setText(getString(R.string.temp_and_feels_like_k_label,
                        dailyWeather.temp.night, dailyWeather.feelsLike.night));
                mTempMorningTextView.setText(getString(R.string.temp_and_feels_like_k_label,
                        dailyWeather.temp.morn, dailyWeather.feelsLike.morn));
                mTempDayTextView.setText(getString(R.string.temp_and_feels_like_k_label,
                        dailyWeather.temp.day, dailyWeather.feelsLike.day));
                mTempEveningTextView.setText(getString(R.string.temp_and_feels_like_k_label,
                        dailyWeather.temp.eve, dailyWeather.feelsLike.eve));
                mWindTextView.setText(getString(R.string.wind_m_s_label,
                        dailyWeather.windSpeed, windDirection));
                mDewPointTextView.setText(getString(R.string.temp_k_label,
                        dailyWeather.dewPoint));
                break;
            default:
                mTempMaxMinTextView.setText(getString(R.string.temp_max_min_c_label,
                        dailyWeather.temp.max, dailyWeather.temp.min));
                mTempNightTextView.setText(getString(R.string.temp_and_feels_like_c_label,
                        dailyWeather.temp.night, dailyWeather.feelsLike.night));
                mTempMorningTextView.setText(getString(R.string.temp_and_feels_like_c_label,
                        dailyWeather.temp.morn, dailyWeather.feelsLike.morn));
                mTempDayTextView.setText(getString(R.string.temp_and_feels_like_c_label,
                        dailyWeather.temp.day, dailyWeather.feelsLike.day));
                mTempEveningTextView.setText(getString(R.string.temp_and_feels_like_c_label,
                        dailyWeather.temp.eve, dailyWeather.feelsLike.eve));
                mWindTextView.setText(getString(R.string.wind_m_s_label,
                        dailyWeather.windSpeed, windDirection));
                mDewPointTextView.setText(getString(R.string.temp_c_label,
                        dailyWeather.dewPoint));
                break;
        }
        mPopTextView.setText(getString(R.string.pop_label, (int) (dailyWeather.pop * 100)));
        mUviTextView.setText(getString(R.string.uvi_label, dailyWeather.uvi));
        mHumidityTextView.setText(getString(R.string.humidity_label, dailyWeather.humidity));
        mPressureTextView.setText(getString(R.string.pressure_label, dailyWeather.pressure));
        mSunriseTextView.setText(getString(R.string.sunrise_label,
                DateTimeHelper.getFormattedTime(getActivity(),
                        new Date((dailyWeather.sunrise + timezoneOffset) * 1000L))));
        mSunsetTextView.setText(getString(R.string.sunset_label,
                DateTimeHelper.getFormattedTime(getActivity(),
                        new Date((dailyWeather.sunset + timezoneOffset) * 1000L))));

        Dialog dialog = new AlertDialog.Builder(getActivity()).setView(view).create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return dialog;
    }
}
