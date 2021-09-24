package com.maxclub.easyweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maxclub.easyweather.api.model.OneCallWeatherData;
import com.maxclub.easyweather.utils.DateTimeHelper;
import com.maxclub.easyweather.utils.LocaleHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.DailyWeatherHolder> {

    private final Context mContext;
    private final WeatherDrawableManager mWeatherDrawableManager;
    private List<OneCallWeatherData.Daily> mItems = new ArrayList<>();
    private long timezoneOffset;

    public DailyWeatherAdapter(Context context) {
        mContext = context;
        mWeatherDrawableManager = new WeatherDrawableManager(mContext);
    }

    public List<OneCallWeatherData.Daily> getItems() {
        return mItems;
    }

    public void setItems(OneCallWeatherData oneCallWeatherData) {
        mItems = oneCallWeatherData.daily;
        timezoneOffset = oneCallWeatherData.timezoneOffset;
    }

    @NonNull
    @NotNull
    @Override
    public DailyWeatherHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.list_item_daily_weather, parent, false);

        return new DailyWeatherHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DailyWeatherAdapter.DailyWeatherHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class DailyWeatherHolder extends RecyclerView.ViewHolder {

        private static final String DIALOG_DAILY_WEATHER = "DialogDailyWeather";

        private final TextView mDateTextView;
        private final ImageView mIconImageView;
        private final TextView mTempTextView;
        private OneCallWeatherData.Daily mDailyWeather;

        public DailyWeatherHolder(@NonNull @NotNull View itemView, int viewType) {
            super(itemView);

            mDateTextView = (TextView) itemView.findViewById(R.id.daily_date_text_view);
            mIconImageView = (ImageView) itemView.findViewById(R.id.daily_icon_image_view);
            mTempTextView = (TextView) itemView.findViewById(R.id.daily_temp_text_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentManager fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
                    DailyWeatherDialogFragment dialog = DailyWeatherDialogFragment.newInstance(mDailyWeather);
                    dialog.show(fragmentManager, DIALOG_DAILY_WEATHER);
                }
            });
        }

        public void bind(OneCallWeatherData.Daily dailyWeather) {
            mDailyWeather = dailyWeather;

            mDateTextView.setText(DateTimeHelper.getFormattedDate(mContext,
                    new Date((mDailyWeather.dt + timezoneOffset) * 1000L)));
            mIconImageView.setImageDrawable(
                    mWeatherDrawableManager.getDrawableByName(mDailyWeather.weather.get(0).icon)
            );

            switch (LocaleHelper.getUnits()) {
                case LocaleHelper.IMPERIAL:
                    mTempTextView.setText(mContext.getString(R.string.temp_min_max_f_label,
                            mDailyWeather.temp.max, mDailyWeather.temp.min));
                    break;
                case LocaleHelper.STANDARD:
                    mTempTextView.setText(mContext.getString(R.string.temp_min_max_k_label,
                            mDailyWeather.temp.max, mDailyWeather.temp.min));
                    break;
                default:
                    mTempTextView.setText(mContext.getString(R.string.temp_min_max_c_label,
                            mDailyWeather.temp.max, mDailyWeather.temp.min));
                    break;
            }
        }
    }
}