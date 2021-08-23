package com.maxclub.android.easyweather;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.maxclub.android.easyweather.api.CitySearchApi;
import com.maxclub.android.easyweather.api.WeatherApi;
import com.maxclub.android.easyweather.api.WeatherApi2;
import com.maxclub.android.easyweather.data.SearchData;
import com.maxclub.android.easyweather.data.WeatherData;
import com.maxclub.android.easyweather.data.WeatherData2;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchWeatherFragment extends Fragment {

    private WeatherApi mWeatherApi = WeatherApi.Instance.getApi();
    private CitySearchApi mCitySearchApi = CitySearchApi.Instance.getApi();
    private WeatherApi2 mWeatherApi2 = WeatherApi2.Instance.getApi();
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

        searchCityByName("Вільшаниця");
//        getWeatherByCityName("Вільшаниця");

        return view;
    }

    @SuppressLint("CheckResult")
    private void searchCityByName(String city) {
        mCitySearchApi.getWeatherDataByCity(CitySearchApi.API_KEY, city, "uk-UA", true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<List<SearchData>>() {
                    @Override
                    public void onNext(@NotNull List<SearchData> searchData) {
                        SearchData data = searchData.get(0);
                        getWeatherByLocationKey(data.getKey());
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getWeatherByLocationKey(String locationKey) {
        mWeatherApi2.getWeatherDataByCity(WeatherApi2.API_KEY, "uk-UA", true, true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<WeatherData2>() {
                    @Override
                    public void onNext(@NotNull WeatherData2 weatherData2) {
                        mTextView.setText(weatherData2.getHeadline().getText());
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void getWeatherByCityName(String cityName) {
        mWeatherApi.getWeatherDataByCity(cityName, WeatherApi.API_KEY, "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<WeatherData>() {
                    @Override
                    public void onNext(@NotNull WeatherData weatherData) {
                        mTextView.setText(weatherData.getName() + " " + weatherData.getMain().getTemp());
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
