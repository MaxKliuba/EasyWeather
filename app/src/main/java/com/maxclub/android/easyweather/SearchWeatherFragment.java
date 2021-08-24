package com.maxclub.android.easyweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

    private WeatherApi mWeatherApi = WeatherApi.Instance.getApi();
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

        getWeatherByCityName("Київ");
//        getWeatherByLocation(50.4501f, 30.5241f);

        return view;
    }

    @SuppressLint("CheckResult")
    private void getWeatherByCityName(String city) {
        mWeatherApi.getWeatherData(city, WeatherApi.API_KEY, 40, "metric", "en")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<WeatherData>() {
                    @Override
                    public void onNext(@NotNull WeatherData weatherData) {
                        mTextView.setText(weatherData.getCity().getName()
                                + " " + weatherData.getList().get(0).getMain().getTemp()
                                + " " + weatherData.getList().get(0).getWeather().get(0).getMain());
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("TAG", e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getWeatherByLocation(float lat, float lon) {
        mWeatherApi.getWeatherData(lat, lon, WeatherApi.API_KEY, 40, "metric", "en")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<WeatherData>() {
                    @Override
                    public void onNext(@NotNull WeatherData weatherData) {
                        mTextView.setText(weatherData.getCity().getName()
                                + " " + weatherData.getList().get(0).getMain().getTemp()
                                + " " + weatherData.getList().get(0).getWeather().get(0).getMain());
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("TAG", e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}