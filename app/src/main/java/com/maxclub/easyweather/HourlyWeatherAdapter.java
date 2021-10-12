package com.maxclub.easyweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maxclub.easyweather.api.model.OneCallWeatherData;
import com.maxclub.easyweather.utils.DateTimeHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherHolder> {

    private final Context mContext;
    private final WeatherDrawableManager mWeatherDrawableManager;
    private List<OneCallWeatherData.Hourly> mItems = new ArrayList<>();
    private long timezoneOffset;
    private final String[] windDirections;

    public HourlyWeatherAdapter(Context context) {
        mContext = context;
        mWeatherDrawableManager = new WeatherDrawableManager(mContext);
        windDirections = mContext.getResources().getStringArray(R.array.wind_direction_arrows);
    }

    public List<OneCallWeatherData.Hourly> getItems() {
        return mItems;
    }

    public void setItems(OneCallWeatherData oneCallWeatherData) {
        mItems = oneCallWeatherData.hourly;
        timezoneOffset = oneCallWeatherData.timezoneOffset;
    }

    @NonNull
    @NotNull
    @Override
    public HourlyWeatherHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_hourly_weather, parent, false);

        return new HourlyWeatherHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HourlyWeatherHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class HourlyWeatherHolder extends RecyclerView.ViewHolder {

        private final TextView mTimeTextView;
        private final ImageView mIconImageView;
        private final TextView mTempTextView;
        private final TextView mPopTextView;
        private final TextView mUviTextView;
        private final TextView mWindTextView;
        private OneCallWeatherData.Hourly mHourlyWeather;

        public HourlyWeatherHolder(@NonNull @NotNull View itemView, int viewType) {
            super(itemView);

            mTimeTextView = (TextView) itemView.findViewById(R.id.hourly_time_text_view);
            mIconImageView = (ImageView) itemView.findViewById(R.id.hourly_icon_image_view);
            mTempTextView = (TextView) itemView.findViewById(R.id.hourly_temp_text_view);
            mPopTextView = (TextView) itemView.findViewById(R.id.hourly_pop_text_view);
            mUviTextView = (TextView) itemView.findViewById(R.id.hourly_uvi_text_view);
            mWindTextView = (TextView) itemView.findViewById(R.id.hourly_wind_text_view);
        }

        public void bind(OneCallWeatherData.Hourly hourlyWeather) {
            mHourlyWeather = hourlyWeather;

            if (getBindingAdapterPosition() == 0) {
                mTimeTextView.setText(mContext.getString(R.string.now_label));
            } else {
                mTimeTextView.setText(DateTimeHelper.getFormattedTime(mContext,
                        new Date((mHourlyWeather.dt + timezoneOffset) * 1000L)));
            }

            mIconImageView.setImageDrawable(
                    mWeatherDrawableManager.getDrawableByName(mHourlyWeather.weather.get(0).icon)
            );

            int index = Math.round(mHourlyWeather.windDeg / 45.0f);
            String windDirection = windDirections[index >= windDirections.length ? 0 : index];

            switch (SettingsPreferences.getUnits(mContext)) {
                case SettingsPreferences.IMPERIAL:
                    mTempTextView.setText(mContext.getString(R.string.temp_and_feels_like_f_label,
                            Math.round(mHourlyWeather.temp), Math.round(mHourlyWeather.feelsLike)));
                    mWindTextView.setText(mContext.getString(R.string.wind_mph_label,
                            mHourlyWeather.windSpeed, windDirection));
                    break;
                case SettingsPreferences.STANDARD:
                    mTempTextView.setText(mContext.getString(R.string.temp_and_feels_like_k_label,
                            Math.round(mHourlyWeather.temp), Math.round(mHourlyWeather.feelsLike)));
                    mWindTextView.setText(mContext.getString(R.string.wind_m_s_label,
                            mHourlyWeather.windSpeed, windDirection));
                    break;
                default:
                    mTempTextView.setText(mContext.getString(R.string.temp_and_feels_like_c_label,
                            Math.round(mHourlyWeather.temp), Math.round(mHourlyWeather.feelsLike)));
                    mWindTextView.setText(mContext.getString(R.string.wind_m_s_label,
                            mHourlyWeather.windSpeed, windDirection));
                    break;
            }

            mPopTextView.setText(mContext.getString(R.string.pop_label,
                    (int) (mHourlyWeather.pop * 100)));
            mUviTextView.setText(mContext.getString(R.string.uvi_label,
                    mHourlyWeather.uvi));
        }
    }
}