package com.maxclub.easyweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.maxclub.easyweather.database.model.City;

import java.util.List;

public class CityViewModel extends ViewModel {
    private final LiveData<List<City>> mCityLiveData = EasyWeatherApp.getInstance().getCityDao().getAllLiveData();
    private final City mCurrentLocationCity = new City(0, EasyWeatherApp.getInstance().getString(R.string.current_location), null);

    public LiveData<List<City>> getCityLiveData() {
        return mCityLiveData;
    }

    public City getCurrentLocationCity() {
        return mCurrentLocationCity;
    }
}
