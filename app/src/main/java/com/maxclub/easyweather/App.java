package com.maxclub.easyweather;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.room.Room;

import com.maxclub.easyweather.database.CityDao;
import com.maxclub.easyweather.database.CityDatabase;
import com.maxclub.easyweather.database.model.City;

public class App extends Application {

    private static App sApp;

    private CityDatabase mCityDatabase;
    private CityDao mCityDao;

    @Override
    public void onCreate() {
        super.onCreate();

        sApp = this;

        mCityDatabase = Room.databaseBuilder(getApplicationContext(), CityDatabase.class, "city-db")
                .allowMainThreadQueries()
                .build();

        mCityDao = mCityDatabase.cityDao();

        City currentLocationCity = new City();
        currentLocationCity.order = 0;
        currentLocationCity.id = 0;
        currentLocationCity.name = getString(R.string.current_location);

        mCityDao.insert(currentLocationCity);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public static App getInstance() {
        return sApp;
    }

    public CityDatabase getCityDatabase() {
        return mCityDatabase;
    }

    public void setCityDatabase(CityDatabase cityDatabase) {
        mCityDatabase = cityDatabase;
    }

    public CityDao getCityDao() {
        return mCityDao;
    }

    public void setCityDao(CityDao cityDao) {
        mCityDao = cityDao;
    }
}
