package com.maxclub.android.easyweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.maxclub.android.easyweather.api.WeatherApi;
import com.maxclub.android.easyweather.data.WeatherData;

import org.jetbrains.annotations.NotNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchWeatherFragment extends Fragment {

    private static final String TAG = "SearchWeatherFragment";

    private WeatherApi mWeatherApi = WeatherApi.Instance.getApi();
    private WeatherData mWeatherData;
    private TextView mTextView;

    public static SearchWeatherFragment newInstance() {
        return new SearchWeatherFragment();
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        fetchWeatherByCityName("Київ");
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container, false);

        mTextView = (TextView) view.findViewById(R.id.weather_textview);

        updateUserInterface();

        return view;
    }

    @SuppressLint("CheckResult")
    private void fetchWeatherByCityName(String city) {
        mWeatherApi.getWeatherData(city, WeatherApi.API_KEY, 40, "metric", "en")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<WeatherData>() {
                    @Override
                    public void onNext(@NotNull WeatherData weatherData) {
                        mWeatherData = weatherData;
                        updateUserInterface();
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        if (!Utils.isNetworkAvailableAndConnected(getActivity())) {
                            Log.e(TAG, "No Internet connection", e);
                            // вивід екрану із повідомленням про відсутність інтернет з'єднання
                        } else {
                            Log.e(TAG, e.getMessage(), e);
                            // вивід повідомлення що щось пішло не так
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void updateUserInterface() {
        if (mWeatherData != null) {
            mTextView.setText(mWeatherData.getCity().getName()
                    + " " + mWeatherData.getList().get(0).getMain().getTemp()
                    + " " + mWeatherData.getList().get(0).getWeather().get(0).getMain());
        }
    }
}