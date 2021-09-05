package com.maxclub.easyweather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.maxclub.easyweather.database.model.City;

import java.util.List;

public class CityViewModel extends ViewModel {
    private final LiveData<List<City>> mCityLiveData = App.getInstance().getCityDao().getAllLiveData();

    public LiveData<List<City>> getCityLiveData() {
        return mCityLiveData;
    }
}
